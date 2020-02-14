package com.javachess.server;

import com.google.gson.Gson;
import com.javachess.logic.*;
import com.javachess.server.message.*;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.Optional;

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
    @DestinationVariable("id") int id
  ) throws Exception {
    Gson gson = new Gson();
    JoinGameIn input = gson.fromJson(messageString, JoinGameIn.class);

    // Look up the dude, find the game, if complete, return game ready with initial board to players
    Player p = Player.of(input.getEmail());
    orchestrator.join(p);

    // Should be find game by ID and that should fix the tests as we would then return game with id 2
    Game g = orchestrator.findGameById(id);

    if (g != null && orchestrator.isGameReady(g)) {
      GameState gs = new GameState("READY", g);
      return gson.toJson(gs, GameState.class);
    }
    return null;
  }

  @MessageMapping("/game/{id}/select-piece")
  @SendTo("/queue/game/{id}/possible-moves")
  public String handleSelectPiece(
    @Payload String messageString,
    @DestinationVariable("id") int id
  ) {
    Gson gson = new Gson();
    SelectPiece input = gson.fromJson(messageString, SelectPiece.class);
    Game g = orchestrator.findGameById(id);

    if (g != null) {
      Position[] moves = Board.getPossibleMoves(input.getX(), input.getY(), g.getBoard());
      PossibleMoves response = new PossibleMoves(moves);
      return gson.toJson(response, PossibleMoves.class);
    }

    return null;
  }

  @MessageMapping("/game/{id}/perform-move")
  @SendTo("/queue/game/{id}/piece-moved")
  public String handlePerformMove(
    @Payload String messageString,
    @DestinationVariable("id") int id
  ) {
    Gson gson = new Gson();
    PerformMove input = gson.fromJson(messageString, PerformMove.class);

    Game game = orchestrator.findGameById(id);
    GameState response = null;

    if (game != null) {
      Optional<Piece> movingPiece = Board.getPieceAt(input.getFromX(), input.getFromY(), game.getBoard());

      if (movingPiece.isPresent()) {
        if (Piece.canMoveTo(input.getToX(), input.getToY(), game.getBoard(), movingPiece.get())) {
          Game newGame = orchestrator.performMove(input.getFromX(), input.getFromY(), input.getToX(), input.getToY(), game);

          response = new GameState("READY", newGame);
        }
        else {
          // Piece can't move there
          response = new GameState("UPDATE", game, ErrorCode.MOVE_NOT_ALLOWED, "You cannot move to that position !");
        }
      }
    }

    return gson.toJson(response, GameState.class);

  }

  @MessageExceptionHandler
  @SendToUser("/queue/errors")
  public String handleException(Throwable exception) {
    exception.printStackTrace();
    return exception.getMessage();
  }

}
