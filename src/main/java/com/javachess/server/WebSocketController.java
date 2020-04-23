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


    // We only start the game if the game is full
    if (Game.isComplete(g)) {
      GameMessage gm = new GameMessage(
        g.getId(),
        g.getPlayer1(),
        g.getPlayer2(),
        Game.getActivePlayer(g),
        Game.getPieces(g)
      );
      GameState gs = new GameState("READY", gm);
      return gson.toJson(gs, GameState.class);
    }
    else {
      return null;
    }
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
      Position[] moves = Game.getPossibleMoves(input.getX(), input.getY(), Game.getPieces(g));
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
      Optional<Piece> movingPiece = Game.getPieceAt(input.getFromX(), input.getFromY(), game);

      if (movingPiece.isPresent()) {
        if (Piece.canMoveTo(input.getToX(), input.getToY(), Game.getPieces(game), movingPiece.get())) {
          Game newGame = orchestrator.performMove(input.getFromX(), input.getFromY(), input.getToX(), input.getToY(), game);

          GameMessage gm = new GameMessage(
            newGame.getId(),
            newGame.getPlayer1(),
            newGame.getPlayer2(),
            Game.getActivePlayer(newGame),
            Game.getPieces(newGame)
          );

          // If it's the same reference, that means that the game was not updated and therefore
          // that the move was not allowed for some reason.
          if (newGame == game) {
            response = new GameState("UPDATE", gm, ErrorCode.MOVE_NOT_ALLOWED, "You cannot move to that position !");
          }
          else {
            response = new GameState("UPDATE", gm);
          }
        }
        else {
          // Piece can't move there
          GameMessage gm = new GameMessage(
            game.getId(),
            game.getPlayer1(),
            game.getPlayer2(),
            Game.getActivePlayer(game),
            Game.getPieces(game)
          );
          response = new GameState("UPDATE", gm, ErrorCode.MOVE_NOT_ALLOWED, "You cannot move to that position !");
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
