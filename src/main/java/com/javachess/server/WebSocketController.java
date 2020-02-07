package com.javachess.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

  @Autowired
  private SimpMessageSendingOperations messagingTemplate;

  @MessageMapping("/message")
  @SendToUser("/queue/reply")
  public String processMessageFromClient(@Payload String message) throws Exception {
    System.out.println(message);
    return "hello !";
  }

  @MessageExceptionHandler
  @SendToUser("/queue/errors")
  public String handleException(Throwable exception) {
    return exception.getMessage();
  }

}
