package com.javachess;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PawnTests {
  @Test
  @DisplayName("equals should return true if the piece has the same color and the same position")
  public void equalsTrue() {
    Piece p1 = Pawn.of("a", "1", Color.WHITE);
    Piece p2 = Pawn.of("a", "1", Color.WHITE);
    assertEquals(true, p1.equals(p2));
  }

  @Test
  @DisplayName("equals should return false if the piece has the same color and the same position")
  public void equalsFalse() {
    Piece p1 = Pawn.of("a", "1", Color.BLACK);
    Piece p2 = Pawn.of("a", "1", Color.WHITE);
    assertEquals(false, p1.equals(p2));
  }

  @Test
  @DisplayName("canMoveTo should return true if a white pawn moves one step up and no piece is at that position")
  public void canMoveToWhiteOneStepUp() {
    Board b = Board.create(); // Initial board, any pawn should be able to move one step up
    Piece pawn = Board.getPieceAt("d", "2", b).get();
    assertEquals(true, pawn.canMoveTo("d", "3", b));
  }

  @Test
  @DisplayName("canMoveTo should return false if a white pawn moves three steps up")
  public void cannotMoveToWhiteThreeStepsUp() {
    Board b = Board.create(); // Initial board, any pawn should be able to move one step up
    Piece pawn = Board.getPieceAt("d", "2", b).get();
    assertEquals(false, pawn.canMoveTo("d", "5", b));
  }
}
