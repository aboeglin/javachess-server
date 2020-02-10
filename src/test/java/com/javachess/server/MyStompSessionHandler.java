package com.javachess.server;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

public class MyStompSessionHandler extends StompSessionHandlerAdapter {
  @Override
  public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
    super.afterConnected(session, connectedHeaders);
  }

  @Override
  public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
    super.handleException(session, command, headers, payload, exception);
  }
}
