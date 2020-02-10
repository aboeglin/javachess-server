package com.javachess.server;

import com.google.gson.Gson;
import com.javachess.logic.Game;
import com.javachess.logic.Player;
import com.javachess.server.message.GameState;
import com.javachess.server.message.JoinGameIn;
import com.javachess.server.message.LookingForGameIn;
import com.javachess.server.message.LookingForGameOut;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

  private GameOrchestrator orchestrator;

  public WebSocketController() {
    this.orchestrator = new GameOrchestrator();
  }

  @MessageMapping("/lfg")
  @SendToUser("/queue/lfg/ack")
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

  @MessageMapping("/game/{id}/join")
  @SendTo("/queue/game/{id}/ready")
  public String handleGameComplete(
    @Payload String messageString,
    @DestinationVariable int id
  ) throws Exception {
    Gson gson = new Gson();
    JoinGameIn input = gson.fromJson(messageString, JoinGameIn.class);

    // Look up the dude, find the game, if complete, return game ready with initial board to players
    Player p = Player.of(input.getEmail());
    orchestrator.join(p);

    // Should be find game by ID and that should fix the tests as we would then return game with id 2
    Game g = orchestrator.findGameById(id);
    System.out.println("ID");
    System.out.println(g.getId());
    if (g != null && orchestrator.isGameReady(g)) {
      System.out.println("READY HERE");
      GameState gs = new GameState("READY", g);
      return gson.toJson(gs, GameState.class);
    }
    return null;
  }

  @MessageExceptionHandler
  @SendToUser("/queue/errors")
  public String handleException(Throwable exception) {
    return exception.getMessage();
  }

}
