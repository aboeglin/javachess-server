package com.javachess.server;

import com.google.gson.Gson;
import com.javachess.logic.Game;
import com.javachess.logic.GameOrchestrator;
import com.javachess.logic.Player;
import com.javachess.server.message.LookingForGameIn;
import com.javachess.server.message.LookingForGameOut;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

  private GameOrchestrator orchestrator;

  public WebSocketController() {
    this.orchestrator = new GameOrchestrator();
  }

  @MessageMapping("/lfg")
  @SendToUser("/queue/looking")
  public String processMessageFromClient(
    @Payload String messageString
  ) throws Exception {
    Gson gson = new Gson();
    LookingForGameIn input = gson.fromJson(messageString, LookingForGameIn.class);

    Game game = orchestrator.registerPlayer(Player.of(input.getEmail()));
    // Add logic for assigning the user
    // Check if that email is already assigned to a game, if yes, reconnect him by sending data of that game
    // Otherwise assign him on a game with creating status, or a new game

    LookingForGameOut output = new LookingForGameOut(
      String.format("Hi %s, looking for opponent ...", input.getEmail()),
      game.getId()
    );

    return gson.toJson(output);
  }

  @MessageExceptionHandler
  @SendToUser("/queue/errors")
  public String handleException(Throwable exception) {
    return exception.getMessage();
  }

}
