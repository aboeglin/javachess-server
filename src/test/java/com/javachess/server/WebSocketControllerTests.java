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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
  @Order(2)
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
  @Order(3)
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

  @Test
  @Order(4)
  @DisplayName("handleSelectPiece should send /queue/game/{id}/possible-moves with an array of positions")
  public void selectPiece() throws Exception {
    CompletableFuture<String> lfgAckMessage = new CompletableFuture<>();
    CompletableFuture<String> readyMessage = new CompletableFuture<>();
    CompletableFuture<String> possibleMovesMessage = new CompletableFuture<>();

    this.session1.subscribe("/user/queue/lfg/ack", new TestStompFrameHandler(payload ->
      lfgAckMessage.complete(payload)
    ));
    Thread.currentThread().sleep(100);

    this.session1.send("/app/lfg", "{email: test1}");
    Thread.currentThread().sleep(100);
    this.session2.send("/app/lfg", "{email: test2}");

    Gson gson = new Gson();
    LookingForGameOut gameJoined = gson.fromJson(lfgAckMessage.get(2, TimeUnit.SECONDS), LookingForGameOut.class);
    this.session1.subscribe(String.format("/queue/game/%s/ready", gameJoined.getGameId()), new TestStompFrameHandler(payload ->
      readyMessage.complete(payload)
    ));
    this.session1.subscribe(String.format("/queue/game/%s/possible-moves", gameJoined.getGameId()), new TestStompFrameHandler(payload ->
      possibleMovesMessage.complete(payload)
    ));
    Thread.currentThread().sleep(100);

    this.session2.send(String.format("/app/game/%s/join", gameJoined.getGameId()), "{email: test2}");
    Thread.currentThread().sleep(100);
    this.session1.send(String.format("/app/game/%s/join", gameJoined.getGameId()), "{email: test1}");
    Thread.currentThread().sleep(100);

    this.session1.send(String.format("/app/game/%s/select-piece", gameJoined.getGameId()), "{email: test1, x: b, y: 2}");

    SnapshotMatcher.expect(possibleMovesMessage.get(10, TimeUnit.SECONDS)).toMatchSnapshot();
  }

  @Test
  @Order(5)
  @DisplayName("performMove should send /queue/game/{id}/piece-moved with a new game state")
  public void performMove() throws Exception {
    CompletableFuture<String> lfgAckMessage = new CompletableFuture<>();
    CompletableFuture<String> readyMessage = new CompletableFuture<>();
    CompletableFuture<String> stateMessage = new CompletableFuture<>();

    this.session1.subscribe("/user/queue/lfg/ack", new TestStompFrameHandler(payload ->
      lfgAckMessage.complete(payload)
    ));
    Thread.currentThread().sleep(300);

    this.session1.send("/app/lfg", "{email: test1}");
    Thread.currentThread().sleep(300);
    this.session2.send("/app/lfg", "{email: test2}");

    Gson gson = new Gson();
    LookingForGameOut gameJoined = gson.fromJson(lfgAckMessage.get(2, TimeUnit.SECONDS), LookingForGameOut.class);

    this.session1.subscribe(String.format("/queue/game/%s/ready", gameJoined.getGameId()), new TestStompFrameHandler(payload ->
      readyMessage.complete(payload)
    ));
    this.session1.subscribe(String.format("/queue/game/%s/piece-moved", gameJoined.getGameId()), new TestStompFrameHandler(payload ->
      stateMessage.complete(payload)
    ));
    Thread.currentThread().sleep(300);

    this.session2.send(String.format("/app/game/%s/join", gameJoined.getGameId()), "{email: test2}");
    Thread.currentThread().sleep(300);
    this.session1.send(String.format("/app/game/%s/join", gameJoined.getGameId()), "{email: test1}");
    Thread.currentThread().sleep(300);

    this.session1.send(String.format("/app/game/%s/perform-move", gameJoined.getGameId()), "{email: test1, fromX: b, fromY: 2, toX: b, toY: 3}");

    SnapshotMatcher.expect(stateMessage.get(10, TimeUnit.SECONDS)).toMatchSnapshot();
  }

  @Test
  @Order(6)
  @DisplayName("performMove should send /queue/game/{id}/piece-moved with an error")
  public void performMoveNotAllowed() throws Exception {
    CompletableFuture<String> lfgAckMessage = new CompletableFuture<>();
    CompletableFuture<String> readyMessage = new CompletableFuture<>();
    CompletableFuture<String> stateMessage = new CompletableFuture<>();

    this.session1.subscribe("/user/queue/lfg/ack", new TestStompFrameHandler(payload ->
      lfgAckMessage.complete(payload)
    ));
    Thread.currentThread().sleep(300);

    this.session1.send("/app/lfg", "{email: test1}");
    Thread.currentThread().sleep(300);
    this.session2.send("/app/lfg", "{email: test2}");

    Gson gson = new Gson();
    LookingForGameOut gameJoined = gson.fromJson(lfgAckMessage.get(2, TimeUnit.SECONDS), LookingForGameOut.class);

    this.session1.subscribe(String.format("/queue/game/%s/ready", gameJoined.getGameId()), new TestStompFrameHandler(payload ->
      readyMessage.complete(payload)
    ));
    this.session1.subscribe(String.format("/queue/game/%s/piece-moved", gameJoined.getGameId()), new TestStompFrameHandler(payload ->
      stateMessage.complete(payload)
    ));
    Thread.currentThread().sleep(300);

    this.session2.send(String.format("/app/game/%s/join", gameJoined.getGameId()), "{email: test2}");
    Thread.currentThread().sleep(300);
    this.session1.send(String.format("/app/game/%s/join", gameJoined.getGameId()), "{email: test1}");
    Thread.currentThread().sleep(300);

    this.session1.send(String.format("/app/game/%s/perform-move", gameJoined.getGameId()), "{email: test1, fromX: b, fromY: 2, toX: b, toY: 5}");

    SnapshotMatcher.expect(stateMessage.get(10, TimeUnit.SECONDS)).toMatchSnapshot();
  }

  @Test
  @Order(6)
  @DisplayName("performMove should send /queue/game/{id}/piece-moved with a piece that moved twice")
  public void performMoveShouldWorkTwice() throws Exception {
    CompletableFuture<String> lfgAckMessage = new CompletableFuture<>();
    CompletableFuture<String> readyMessage = new CompletableFuture<>();
    CompletableFuture<String> stateMessage = new CompletableFuture<>();

    this.session1.subscribe("/user/queue/lfg/ack", new TestStompFrameHandler(payload ->
      lfgAckMessage.complete(payload)
    ));
    Thread.currentThread().sleep(300);

    this.session1.send("/app/lfg", "{email: test1}");
    Thread.currentThread().sleep(300);
    this.session2.send("/app/lfg", "{email: test2}");

    Gson gson = new Gson();
    LookingForGameOut gameJoined = gson.fromJson(lfgAckMessage.get(2, TimeUnit.SECONDS), LookingForGameOut.class);

    this.session1.subscribe(String.format("/queue/game/%s/ready", gameJoined.getGameId()), new TestStompFrameHandler(payload ->
      readyMessage.complete(payload)
    ));
    int[] count = {0};

    this.session1.subscribe(String.format("/queue/game/%s/piece-moved", gameJoined.getGameId()), new TestStompFrameHandler(payload -> {
      if (count[0] == 1) {
        stateMessage.complete(payload);
      }
      count[0] = count[0] + 1;
    }));
    Thread.currentThread().sleep(300);

    this.session2.send(String.format("/app/game/%s/join", gameJoined.getGameId()), "{email: test2}");
    Thread.currentThread().sleep(300);
    this.session1.send(String.format("/app/game/%s/join", gameJoined.getGameId()), "{email: test1}");
    Thread.currentThread().sleep(300);

    this.session1.send(String.format("/app/game/%s/perform-move", gameJoined.getGameId()), "{email: test1, fromX: b, fromY: 2, toX: b, toY: 3}");
    Thread.currentThread().sleep(300);
    this.session1.send(String.format("/app/game/%s/perform-move", gameJoined.getGameId()), "{email: test1, fromX: b, fromY: 3, toX: b, toY: 4}");

    SnapshotMatcher.expect(stateMessage.get(10, TimeUnit.SECONDS)).toMatchSnapshot();
  }
}
