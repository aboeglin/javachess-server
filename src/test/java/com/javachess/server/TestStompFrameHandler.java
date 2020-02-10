package com.javachess.server;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;
import java.util.function.Consumer;

public class TestStompFrameHandler implements StompFrameHandler {

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
