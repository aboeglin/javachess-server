package com.javachess.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class KnightTests {
  @Test
  @DisplayName("canMoveTo, 2 up one right")
  public void canMoveToTwoUpOneRight() {
    Game g = Game.of(1, Player.of("white", Color.WHITE), Player.of("black", Color.BLACK));
    Piece knight = Game.getPieceAt("b", "1", g).get();
    assertEquals(true, Piece.canMoveTo("c", "3", Game.getPieces(g), knight));
  }

  @Test
  @DisplayName("canMoveTo, 2 right one up")
  public void canMoveToTwoRightOneUp() {
    Game g = Game.of(1, Player.of("white", Color.WHITE), Player.of("black", Color.BLACK));
    g = Game.doMove(Move.of(Position.of("b", "1"), Position.of("c", "3")), g);
    Piece knight = Game.getPieceAt("c", "3", g).get();
    assertEquals(true, Piece.canMoveTo("e", "4", Game.getPieces(g), knight));
  }
}
