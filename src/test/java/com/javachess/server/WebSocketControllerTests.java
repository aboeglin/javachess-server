package com.javachess.server;

import com.google.gson.Gson;
import com.javachess.server.message.LookingForGameOut;
import io.github.jsonSnapshot.SnapshotMatcher;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSocketControllerTests {

  @Value("${local.server.port}")
  private int port;

  private WebSocketStompClient client1;
  private StompSession session1;

  private WebSocketStompClient client2;
  private StompSession session2;

  @BeforeAll
  public static void beforeAll() {
    SnapshotMatcher.start(Snapshot::asJsonString);
  }

  @AfterAll
  public static void afterAll() {
    SnapshotMatcher.validateSnapshots();
  }

  @BeforeEach
  public void setUp() throws Exception {
    String url = String.format("ws://localhost:%s/ws", this.port);

    this.client1 = new WebSocketStompClient(new StandardWebSocketClient());
    this.client1.setMessageConverter(new StringMessageConverter());
    this.session1 = this.client1.connect(url, new MyStompSessionHandler()).get();

    this.client2 = new WebSocketStompClient(new StandardWebSocketClient());
    this.client2.setMessageConverter(new StringMessageConverter());
    this.session2 = this.client2.connect(url, new MyStompSessionHandler()).get();
  }

  @AfterEach
  public void tearDown() throws Exception {
    this.session1.disconnect();
    this.client1.stop();

    this.session2.disconnect();
    this.client2.stop();
  }

  @Test
  @Order(1)
  public void connectsToSocket() throws Exception {
    assertEquals(this.session1.isConnected(), true);
  }

  @Test
  @Order(1)
  @DisplayName("It should respond with a game id")
  public void lookForGame() throws Exception {
    CompletableFuture<String> resultKeeper = new CompletableFuture<>();

    this.session1.subscribe("/user/queue/lfg/ack", new TestStompFrameHandler(payload -> {
      resultKeeper.complete(payload);
    }));
    Thread.currentThread().sleep(300);

    this.session1.send("/app/lfg", "{email: test1}");
    Thread.currentThread().sleep(300);
    // Needed to complete a game and reset for other tests
    this.session2.send("/app/lfg", "{email: test2}");
    Thread.currentThread().sleep(300);

    assertEquals("{\"message\":\"Hi test1, looking for opponent ...\",\"gameId\":1}", resultKeeper.get(2, TimeUnit.SECONDS));
  }

  @Test
  @Order(2)
  @DisplayName("It respond on the endpoint of the game room")
  public void joinRoom() throws Exception {
    CompletableFuture<String> lfgKeeper = new CompletableFuture<>();
    CompletableFuture<String> resultKeeper = new CompletableFuture<>();

    this.session1.subscribe("/user/queue/lfg/ack", new TestStompFrameHandler(payload ->
      lfgKeeper.complete(payload)
    ));
    Thread.currentThread().sleep(300);

    this.session1.send("/app/lfg", "{email: test1}");
    Thread.currentThread().sleep(300);
    this.session2.send("/app/lfg", "{email: test2}");

    Gson gson = new Gson();
    LookingForGameOut response = gson.fromJson(lfgKeeper.get(10, TimeUnit.SECONDS), LookingForGameOut.class);
    this.session1.subscribe(String.format("/queue/game/%s/ready", response.getGameId()), new TestStompFrameHandler(payload ->
      resultKeeper.complete(payload)
    ));
    Thread.currentThread().sleep(300);

    this.session2.send(String.format("/app/game/%s/join", response.getGameId()), "{email: test2}");
    Thread.currentThread().sleep(300);
    this.session1.send(String.format("/app/game/%s/join", response.getGameId()), "{email: test1}");
    SnapshotMatcher.expect(resultKeeper.get(10, TimeUnit.SECONDS)).toMatchSnapshot();
  }
}
