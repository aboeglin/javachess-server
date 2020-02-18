package com.javachess.server.message;

import com.javachess.logic.Piece;
import com.javachess.logic.Player;

import java.util.List;

public class GameMessage {

  private int id;
  private Player player1;
  private Player player2;
  private Player activePlayer;
  private List<Piece> pieces;

  public GameMessage(int id, Player p1, Player p2, Player activePlayer, List<Piece> pieces) {
    this.id = id;
    this.player1 = p1;
    this.player2 = p2;
    this.activePlayer = activePlayer;
    this.pieces = pieces;
  }

}
