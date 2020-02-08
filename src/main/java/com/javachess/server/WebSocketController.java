package com.javachess.server;

import com.google.gson.Gson;
import com.javachess.server.message.LookingForGameIn;
import com.javachess.server.message.LookingForGameOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

  @Autowired
  private SimpMessageSendingOperations messagingTemplate;

  @MessageMapping("/lfg")
  public void processMessageFromClient(
    @Payload Message messageObject,
    @Payload String messageString
  ) throws Exception {
    String sessionId = (String) messageObject.getHeaders().get("simpSessionId");
    Gson gson = new Gson();
    LookingForGameIn input = gson.fromJson(messageString, LookingForGameIn.class);

    // Add logic for assigning the user
    // Check if that email is already assigned to a game, if yes, reconnect him by sending data of that game
    // Otherwise assign him on a game with creating status, or a new game

    LookingForGameOut output = new LookingForGameOut(String.format("Hi %s, looking for opponent ...", input.getEmail()));
    this.messagingTemplate.convertAndSend( sessionId, gson.toJson(output));
  }

  @MessageExceptionHandler
  public void handleException(Throwable exception) {
    this.messagingTemplate.convertAndSend("/errors", exception.getMessage());
  }

}
