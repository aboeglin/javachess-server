package com.javachess.server;

import com.google.gson.Gson;
import com.javachess.logic.*;
import com.javachess.server.message.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class WebSocketController {

  @Autowired
  private GameOrchestrator orchestrator;

  @MessageMapping("/game/{id}/join")
  @SendTo("/queue/game/{id}/ready")
  public String handleGameComplete(@Payload String messageString, @DestinationVariable("id") int id) {
    Gson gson = new Gson();
    JoinGameIn input = gson.fromJson(messageString, JoinGameIn.class);

    // Look up the dude, find the game, if complete, return game ready with initial board to players
    Player p = Player.of(input.getEmail());
    orchestrator.join(p);

    // Should be find game by ID and that should fix the tests as we would then return game with id 2
    Game g = orchestrator.findGameById(id);


    // We only start the game if the game is full
    if (Game.isComplete(g)) {
      GameState gm = new GameState(g);
      GameUpdate gs = new GameUpdate("READY", gm);
      return gson.toJson(gs, GameUpdate.class);
    }
    else {
      return null;
    }
  }

  @MessageMapping("/game/{id}/select-piece")
  @SendTo("/queue/game/{id}/possible-moves")
  public String handleSelectPiece(@Payload String messageString, @DestinationVariable("id") int id) {
    Gson gson = new Gson();
    SelectPiece input = gson.fromJson(messageString, SelectPiece.class);
    Game g = orchestrator.findGameById(id);

    if (g != null) {
      Position[] moves = Game.getPossibleMoves(input.getX(), input.getY(), Game.getPieces(g));
      PossibleMoves response = new PossibleMoves(moves);
      return gson.toJson(response, PossibleMoves.class);
    }

    return null;
  }

  @MessageMapping("/game/{id}/perform-move")
  @SendTo("/queue/game/{id}/piece-moved")
  public String handlePerformMove(@Payload String messageString, @DestinationVariable("id") int id) {
    Gson gson = new Gson();
    PerformMove input = gson.fromJson(messageString, PerformMove.class);

    Game game = orchestrator.findGameById(id);
    GameUpdate response = null;

    if (game != null) {
      Optional<Piece> movingPiece = Game.getPieceAt(input.getFromX(), input.getFromY(), game);

      if (movingPiece.isPresent()) {
        if (Piece.canMoveTo(input.getToX(), input.getToY(), Game.getPieces(game), movingPiece.get())) {
          Game newGame = orchestrator.performMove(input.getFromX(), input.getFromY(), input.getToX(), input.getToY(), game);

          GameState gm = new GameState(newGame);

          // If it's the same reference, that means that the game was not updated and therefore
          // that the move was not allowed for some reason.
          if (newGame == game) {
            response = new GameUpdate("UPDATE", gm, ErrorCode.MOVE_NOT_ALLOWED, "You cannot move to that position !");
          }
          else {
            response = new GameUpdate("UPDATE", gm);
          }
        }
        else {
          // Piece can't move there
          GameState gm = new GameState(game);
          response = new GameUpdate("UPDATE", gm, ErrorCode.MOVE_NOT_ALLOWED, "You cannot move to that position !");
        }
      }
    }

    return gson.toJson(response, GameUpdate.class);
  }

  @MessageExceptionHandler
  @SendToUser("/queue/errors")
  public String handleException(Throwable exception) {
    exception.printStackTrace();
    return exception.getMessage();
  }

}
