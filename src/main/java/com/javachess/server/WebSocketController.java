package com.javachess.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

  @Autowired
  private SimpMessageSendingOperations messagingTemplate;

  @MessageMapping("/message")
  public void processMessageFromClient(@Payload String message) throws Exception {
    if (Math.random() > 0.8) {
      throw new Exception("something went wrong !");
    }
    this.messagingTemplate.convertAndSend("/game-1", "Welcome !");
  }

  @MessageExceptionHandler
  public void handleException(Throwable exception) {
    this.messagingTemplate.convertAndSend("/errors", exception.getMessage());
  }

}
