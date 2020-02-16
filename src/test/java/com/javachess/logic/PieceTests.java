package com.javachess.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PieceTests {
  @Test
  @DisplayName("canMoveTo should not allow a move to the same color")
  public void canMoveToSameColor() {
    Game g = Game.of(1, Player.of("white", Color.WHITE), Player.of("black", Color.BLACK));
    Piece knight = Game.getPieceAt("b", "1", g).get();
    assertEquals(false, Piece.canMoveTo("d", "2", Game.getPieces(g), knight));
  }
}
