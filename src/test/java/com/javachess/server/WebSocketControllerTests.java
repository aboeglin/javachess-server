package com.javachess.server;

import com.google.gson.Gson;
import io.github.jsonSnapshot.SnapshotMatcher;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.annotation.DirtiesContext;
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

  @LocalServerPort
  private int port;

  @Autowired
  private RestController controller;

  @Autowired
  private TestRestTemplate restTemplate;

  private WebSocketStompClient client1;
  private StompSession session1;

  private WebSocketStompClient client2;
  private StompSession session2;

  @Before
  public void before() {
    /*
     * Enables PATCH requests for restTemplate
     * https://stackoverflow.com/questions/29447382/resttemplate-patch-request
     */
    restTemplate.getRestTemplate().setRequestFactory(
      new HttpComponentsClientHttpRequestFactory()
    );
  }

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

//  @Test
//  @Order(2)
//  @DisplayName("It should respond with a game id")
//  @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
//  public void lookForGame() throws Exception {
//    CompletableFuture<String> resultKeeper = new CompletableFuture<>();
//
//    this.session1.subscribe("/user/queue/lfg/ack", new TestStompFrameHandler(payload -> {
//      resultKeeper.complete(payload);
//    }));
//    Thread.currentThread().sleep(300);
//
//    this.session1.send("/app/lfg", "{email: test1}");
//    Thread.currentThread().sleep(300);
//    // Needed to complete a game and reset for other tests
//    this.session2.send("/app/lfg", "{email: test2}");
//    Thread.currentThread().sleep(300);
//
//    assertEquals("{\"message\":\"Hi test1, looking for opponent ...\",\"gameId\":1}", resultKeeper.get(2, TimeUnit.SECONDS));
//  }

  @Test
  @Order(3)
  @DisplayName("It respond on the endpoint of the game room")
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
  public void joinRoom() throws Exception {
    CompletableFuture<String> resultKeeper = new CompletableFuture<>();

    // Refactor and move this in a BeforeEach ?
    this.restTemplate.postForEntity("http://localhost:" + port + "/games", "{\"userId\":\"1\"}", String.class);
    this.restTemplate.patchForObject("http://localhost:" + port + "/games/1", "{\"userId\":\"2\"}", String.class);

    Gson gson = new Gson();
    this.session1.subscribe("/queue/game/1/ready", new TestStompFrameHandler(payload ->
      resultKeeper.complete(payload)
    ));
    Thread.currentThread().sleep(300);

    this.session2.send("/app/game/1/join", "{email: test2}");
    Thread.currentThread().sleep(300);
    this.session1.send("/app/game/1/join", "{email: test1}");

    SnapshotMatcher.expect(resultKeeper.get(10, TimeUnit.SECONDS)).toMatchSnapshot();
  }

  @Test
  @Order(4)
  @DisplayName("handleSelectPiece should send /queue/game/{id}/possible-moves with an array of positions")
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
  public void selectPiece() throws Exception {
    CompletableFuture<String> readyMessage = new CompletableFuture<>();
    CompletableFuture<String> possibleMovesMessage = new CompletableFuture<>();

    this.restTemplate.postForEntity("http://localhost:" + port + "/games", "{\"userId\":\"1\"}", String.class);
    this.restTemplate.patchForObject("http://localhost:" + port + "/games/1", "{\"userId\":\"2\"}", String.class);

    this.session1.subscribe("/queue/game/1/ready", new TestStompFrameHandler(payload ->
      readyMessage.complete(payload)
    ));
    this.session1.subscribe("/queue/game/1/possible-moves", new TestStompFrameHandler(payload ->
      possibleMovesMessage.complete(payload)
    ));
    Thread.currentThread().sleep(100);

    this.session2.send("/app/game/1/join", "{email: test2}");
    Thread.currentThread().sleep(100);
    this.session1.send("/app/game/1/join", "{email: test1}");
    Thread.currentThread().sleep(100);

    this.session1.send("/app/game/1/select-piece", "{email: test1, x: b, y: 2}");

    SnapshotMatcher.expect(possibleMovesMessage.get(10, TimeUnit.SECONDS)).toMatchSnapshot();
  }

  @Test
  @Order(5)
  @DisplayName("performMove should send /queue/game/{id}/piece-moved with a new game state")
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
  public void performMove() throws Exception {
    CompletableFuture<String> readyMessage = new CompletableFuture<>();
    CompletableFuture<String> stateMessage = new CompletableFuture<>();

    this.restTemplate.postForEntity("http://localhost:" + port + "/games", "{\"userId\":\"1\"}", String.class);
    this.restTemplate.patchForObject("http://localhost:" + port + "/games/1", "{\"userId\":\"2\"}", String.class);

    this.session1.subscribe("/queue/game/1/ready", new TestStompFrameHandler(payload -> readyMessage.complete(payload)));
    this.session1.subscribe("/queue/game/1/piece-moved", new TestStompFrameHandler(payload -> stateMessage.complete(payload)));
    Thread.currentThread().sleep(300);

    // TODO: Write a test so that it's not possible to join with players not in the game !
    this.session2.send("/app/game/1/join", "{email: test2}");
    Thread.currentThread().sleep(300);
    this.session1.send("/app/game/1/join", "{email: test1}");
    Thread.currentThread().sleep(300);

    this.session1.send("/app/game/1/perform-move", "{email: test1, fromX: b, fromY: 2, toX: b, toY: 3}");

    SnapshotMatcher.expect(stateMessage.get(10, TimeUnit.SECONDS)).toMatchSnapshot();
  }

  @Test
  @Order(6)
  @DisplayName("performMove should send /queue/game/{id}/piece-moved with an error")
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
  public void performMoveNotAllowed() throws Exception {
    CompletableFuture<String> readyMessage = new CompletableFuture<>();
    CompletableFuture<String> stateMessage = new CompletableFuture<>();

    this.restTemplate.postForEntity("http://localhost:" + port + "/games", "{\"userId\":\"1\"}", String.class);
    this.restTemplate.patchForObject("http://localhost:" + port + "/games/1", "{\"userId\":\"2\"}", String.class);

    this.session1.subscribe("/queue/game/1/ready", new TestStompFrameHandler(payload -> readyMessage.complete(payload)));
    this.session1.subscribe("/queue/game/1/piece-moved", new TestStompFrameHandler(payload -> stateMessage.complete(payload)));
    Thread.currentThread().sleep(300);

    this.session2.send("/app/game/1/join", "{email: 2}");
    Thread.currentThread().sleep(300);
    this.session1.send("/app/game/1/join", "{email: 1}");
    Thread.currentThread().sleep(300);

    this.session1.send("/app/game/1/perform-move", "{email: test1, fromX: b, fromY: 2, toX: b, toY: 5}");

    SnapshotMatcher.expect(stateMessage.get(10, TimeUnit.SECONDS)).toMatchSnapshot();
  }

  @Test
  @Order(7)
  @DisplayName("performMove should send /queue/game/{id}/piece-moved with a piece that moved twice")
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
  public void performMoveShouldWorkTwice() throws Exception {
    CompletableFuture<String> readyMessage = new CompletableFuture<>();
    CompletableFuture<String> stateMessage = new CompletableFuture<>();

    this.restTemplate.postForEntity("http://localhost:" + port + "/games", "{\"userId\":\"1\"}", String.class);
    this.restTemplate.patchForObject("http://localhost:" + port + "/games/1", "{\"userId\":\"2\"}", String.class);

    this.session1.subscribe("/queue/game/1/ready", new TestStompFrameHandler(payload -> readyMessage.complete(payload)));

    int[] count = {0};
    this.session1.subscribe("/queue/game/1/piece-moved", new TestStompFrameHandler(payload -> {
      if (count[0] == 2) {
        stateMessage.complete(payload);
      }
      count[0] = count[0] + 1;
    }));
    Thread.currentThread().sleep(300);

    this.session2.send("/app/game/1/join", "{email: test2}");
    Thread.currentThread().sleep(300);
    this.session1.send("/app/game/1/join", "{email: test1}");
    Thread.currentThread().sleep(300);

    this.session1.send("/app/game/1/perform-move", "{email: test1, fromX: b, fromY: 2, toX: b, toY: 3}");
    Thread.currentThread().sleep(300);
    this.session1.send("/app/game/1/perform-move", "{email: test1, fromX: b, fromY: 7, toX: b, toY: 6}");
    Thread.currentThread().sleep(300);
    this.session1.send("/app/game/1/perform-move", "{email: test1, fromX: b, fromY: 3, toX: b, toY: 4}");

    SnapshotMatcher.expect(stateMessage.get(10, TimeUnit.SECONDS)).toMatchSnapshot();
  }

  @Test
  @Order(8)
  @DisplayName("performMove should send to /queue/game/{id}/piece-moved a the same game object with an error message if it's not the player turn")
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
  public void performMoveNotYourTurn() throws Exception {
    CompletableFuture<String> readyMessage = new CompletableFuture<>();
    CompletableFuture<String> stateMessage = new CompletableFuture<>();

    this.restTemplate.postForEntity("http://localhost:" + port + "/games", "{\"userId\":\"1\"}", String.class);
    this.restTemplate.patchForObject("http://localhost:" + port + "/games/1", "{\"userId\":\"2\"}", String.class);


    this.session1.subscribe("/queue/game/1/ready", new TestStompFrameHandler(payload ->
      readyMessage.complete(payload)
    ));

    this.session1.subscribe("/queue/game/1/piece-moved", new TestStompFrameHandler(payload -> {
        stateMessage.complete(payload);
    }));
    Thread.currentThread().sleep(300);

    this.session2.send("/app/game/1/join", "{email: test2}");
    Thread.currentThread().sleep(300);
    this.session1.send("/app/game/1/join", "{email: test1}");
    Thread.currentThread().sleep(300);

    this.session1.send("/app/game/1/perform-move", "{email: test1, fromX: b, fromY: 7, toX: b, toY: 6}");
    Thread.currentThread().sleep(300);

    SnapshotMatcher.expect(stateMessage.get(10, TimeUnit.SECONDS)).toMatchSnapshot();
  }
}
