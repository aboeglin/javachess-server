package com.javachess.server;

import com.google.gson.Gson;
import com.javachess.logic.Game;
import com.javachess.logic.GameOrchestrator;
import com.javachess.logic.Player;
import com.javachess.server.message.LogInGameRoomIn;
import com.javachess.server.message.LookingForGameIn;
import com.javachess.server.message.LookingForGameOut;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
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
  public String handleLFG(
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

  @MessageMapping("/game/{id}/enter-room")
  @SendTo("/queue/game/{id}/ready")
  public String handleGameComplete(
    @Payload String messageString
  ) throws Exception {
    Gson gson = new Gson();
    LogInGameRoomIn input = gson.fromJson(messageString, LogInGameRoomIn.class);

    // Look up the dude, find the game, if complete, return game ready with initial board to players


    return "In the room !\nThe game will start shortly ...";
  }

  @MessageExceptionHandler
  @SendToUser("/queue/errors")
  public String handleException(Throwable exception) {
    return exception.getMessage();
  }

}
