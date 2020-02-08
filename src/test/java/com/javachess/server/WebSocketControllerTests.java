package com.javachess.server;

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

import java.lang.reflect.Type;
import java.util.function.Consumer;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSocketControllerTests {

  @Value("${local.server.port}")
  private int port;

  private WebSocketStompClient client;
  private StompSession session;

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
  @DisplayName("It should open a socket connection")
  public void connect() throws Exception {
    this.session.getSessionId();
    this.session.send("/lfg", "{email: test}");
    this.session.subscribe(this.session.getSessionId(), new TestStompFrameHandler(payload -> {
      System.out.println(payload);
    }));
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
