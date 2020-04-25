package com.javachess.server.message;

import com.javachess.logic.Game;
import com.javachess.logic.Piece;
import com.javachess.logic.Player;
import com.javachess.logic.Position;

import java.util.List;

public class GameUpdate {

  private String status;

  private GameState game;

  private boolean error;

  private ErrorCode errorCode;

  private String errorMessage;

  private Position[] possibleMoves;

  public GameUpdate(String status, Game game) {
    this.status = status;
    this.game = new GameState(game);
    this.possibleMoves = new Position[]{};
  }

  public GameUpdate(String status, Game game, Position[] possibleMoves) {
    this.status = status;
    this.game = new GameState(game);
    this.possibleMoves = possibleMoves;
  }

  public GameUpdate(String status, Game game, ErrorCode errorCode, String errorMessage) {
    this.status = status;
    this.game = new GameState(game);
    this.errorMessage = errorMessage;
    this.errorCode = errorCode;
    this.error = true;
    this.possibleMoves = new Position[]{};
  }
}

class GameState {

  private int id;
  private Player player1;
  private Player player2;
  private Player activePlayer;
  private List<Piece> pieces;

  public GameState(Game g) {
    this.id = g.getId();
    this.player1 = g.getPlayer1();
    this.player2 = g.getPlayer2();
    this.activePlayer = Game.getActivePlayer(g);
    this.pieces = Game.getPieces(g);
  }

}
