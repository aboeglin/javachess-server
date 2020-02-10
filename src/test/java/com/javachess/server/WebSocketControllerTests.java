package com.javachess.server;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.Separators;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.javachess.server.message.JoinGameIn;
import com.javachess.server.message.JoinGameOut;
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

import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSocketControllerTests {

  @Value("${local.server.port}")
  private int port;

  private WebSocketStompClient client;
  private StompSession session;

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

    this.client = new WebSocketStompClient(new StandardWebSocketClient());
    this.client.setMessageConverter(new StringMessageConverter());
    this.session = this.client.connect(url, new MyStompSessionHandler()).get();
  }

  @AfterEach
  public void tearDown() throws Exception {
    this.session.disconnect();
    this.client.stop();
  }

  @Test
  public void connectsToSocket() throws Exception {
    assertEquals(this.session.isConnected(), true);
  }

  @Test
  @DisplayName("It should respond with a game id")
  public void lookForGame() throws Exception {
    CompletableFuture<String> resultKeeper = new CompletableFuture<>();

    this.session.subscribe("/user/queue/lfg/ack", new TestStompFrameHandler(payload -> {
      resultKeeper.complete(payload);
    }));
    Thread.currentThread().sleep(1000);

    this.session.send("/app/lfg", "{email: test}");

    assertEquals(resultKeeper.get(2, TimeUnit.SECONDS), "{\"message\":\"Hi test, looking for opponent ...\",\"gameId\":1}");
  }

  @Test
  @DisplayName("It respond on the endpoint of the game room")
  public void joinRoom() throws Exception {
    CompletableFuture<String> lfgKeeper = new CompletableFuture<>();
    CompletableFuture<String> resultKeeper = new CompletableFuture<>();

    this.session.subscribe("/user/queue/lfg/ack", new TestStompFrameHandler(payload ->
      lfgKeeper.complete(payload)
    ));
    Thread.currentThread().sleep(1000);

    this.session.send("/app/lfg", "{email: test}");

    Gson gson = new Gson();
    LookingForGameOut response = gson.fromJson(lfgKeeper.get(2, TimeUnit.SECONDS), LookingForGameOut.class);

    this.session.subscribe(String.format("/queue/game/%s/ready", response.getGameId()), new TestStompFrameHandler(payload ->
      resultKeeper.complete(payload)
    ));

    this.session.send(String.format("/app/game/%s/join", response.getGameId()), "{email: test}");
    SnapshotMatcher.expect(resultKeeper.get(2, TimeUnit.SECONDS)).toMatchSnapshot();
  }
}

class TestStompFrameHandler implements StompFrameHandler {

  private final Consumer<String> frameHandler;

  public TestStompFrameHandler(Consumer<String> c) {
    this.frameHandler = c;
  }

  @Override
  public Type getPayloadType(StompHeaders stompHeaders) {
    return String.class;
  }

  @Override
  public void handleFrame(StompHeaders stompHeaders, Object payload) {
    this.frameHandler.accept(payload.toString());
  }
}

class MyStompSessionHandler extends StompSessionHandlerAdapter {
  @Override
  public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
    super.afterConnected(session, connectedHeaders);
  }

  @Override
  public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
    super.handleException(session, command, headers, payload, exception);
  }
}

final class Snapshot {
  private static final ObjectMapper objectMapper = buildObjectMapper();
  private static final PrettyPrinter pp = buildDefaultPrettyPrinter();

  /**
   * Workaround for an incompatibility between latest Jackson and json-snapshot libs.
   * <p>
   * Intended to replace {@code io.github.jsonSnapshot.SnapshotMatcher#defaultJsonFunction}
   *
   * @see <a href="https://github.com/json-snapshot/json-snapshot.github.io/issues/27">Issue in json-snapshot project</a>
   */
  public static String asJsonString(Object object) {
    try {
      return objectMapper.writer(pp).writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new UncheckedIOException(e);
    }
  }

  /**
   * Unmodified copy of {@code io.github.jsonSnapshot.SnapshotMatcher#buildObjectMapper}
   */
  private static ObjectMapper buildObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

    objectMapper.setVisibility(
      objectMapper
        .getSerializationConfig()
        .getDefaultVisibilityChecker()
        .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
        .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
        .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
        .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
    return objectMapper;
  }

  /**
   * Modified copy of {@code io.github.jsonSnapshot.SnapshotMatcher#buildDefaultPrettyPrinter}
   */
  private static PrettyPrinter buildDefaultPrettyPrinter() {
    DefaultPrettyPrinter pp =
      new DefaultPrettyPrinter("") {
        @Override
        public DefaultPrettyPrinter createInstance() {
          return this;
        }

        @Override
        public DefaultPrettyPrinter withSeparators(Separators separators) {
          this._separators = separators;
          this._objectFieldValueSeparatorWithSpaces =
            separators.getObjectFieldValueSeparator() + " ";
          return this;
        }
      };
    DefaultPrettyPrinter.Indenter lfOnlyIndenter = new DefaultIndenter("  ", "\n");
    pp.indentArraysWith(lfOnlyIndenter);
    pp.indentObjectsWith(lfOnlyIndenter);
    return pp;
  }
}