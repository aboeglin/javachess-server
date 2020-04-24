package com.javachess.server.message;

import com.javachess.logic.Game;
import com.javachess.logic.Piece;
import com.javachess.logic.Player;

import java.util.List;

public class GameState {

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
