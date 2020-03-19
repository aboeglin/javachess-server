package com.javachess.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QueenTests {

  @Test
  @DisplayName("canMoveTo should return true if the move is straight and there's no piece in between")
  public void canMoveToStraightNoInBetween() {
    Game g = Game.of(1, Player.of("white", Color.WHITE), Player.of("black", Color.BLACK));
    g = Game.doMove(Move.of(Position.of("d", "2"), Position.of("d", "6")), g);
    Piece queen = Game.getPieceAt("d", "1", g).get();
    assertEquals(true, Piece.canMoveTo("d", "5", Game.getPieces(g), queen));
  }

  @Test
  @DisplayName("canMoveTo should return false if the move is straight and there's a piece in between")
  public void canMoveToStraightInBetween() {
    Game g = Game.of(1, Player.of("white", Color.WHITE), Player.of("black", Color.BLACK));
    Piece queen = Game.getPieceAt("d", "1", g).get();
    assertEquals(false, Piece.canMoveTo("d", "5", Game.getPieces(g), queen));
  }

  @Test
  @DisplayName("canMoveTo should return true if the move is diagonal and there's no piece in between")
  public void canMoveToDiagonalNoInBetween() {
    Game g = Game.of(1, Player.of("white", Color.WHITE), Player.of("black", Color.BLACK));
    g = Game.doMove(Move.of(Position.of("e", "2"), Position.of("e", "3")), g);
    Piece queen = Game.getPieceAt("d", "1", g).get();
    assertEquals(true, Piece.canMoveTo("g", "4", Game.getPieces(g), queen));
  }

  @Test
  @DisplayName("canMoveTo should return false if the move is diagonal and there's a piece in between")
  public void canMoveToDiagonalInBetween() {
    Game g = Game.of(1, Player.of("white", Color.WHITE), Player.of("black", Color.BLACK));
    Piece queen = Game.getPieceAt("d", "1", g).get();
    assertEquals(false, Piece.canMoveTo("g", "4", Game.getPieces(g), queen));
  }

}
