package com.javachess.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PawnTests {
  @Test
  @DisplayName("equals should return true if the piece has the same color and the same position")
  public void equalsTrue() {
    Piece p1 = Piece.of("a", "1", Color.WHITE, PieceType.PAWN);
    Piece p2 = Piece.of("a", "1", Color.WHITE, PieceType.PAWN);
    assertEquals(true, p1.equals(p2));
  }

  @Test
  @DisplayName("equals should return false if the piece has the same color and the same position")
  public void equalsFalse() {
    Piece p1 = Piece.of("a", "1", Color.BLACK, PieceType.PAWN);
    Piece p2 = Piece.of("a", "1", Color.WHITE, PieceType.PAWN);
    assertEquals(false, p1.equals(p2));
  }

  @Test
  @DisplayName("canMoveTo should return true if a white pawn moves one step up and no piece is at that position")
  public void canMoveToWhiteOneStepUp() {
    Board b = Board.create(); // Initial board, any pawn should be able to move one step up
    Piece pawn = Board.getPieceAt("d", "2", b).get();
    assertEquals(true, Pawn.canMoveTo("d", "3", Board.getPieces(b), pawn));
  }

  @Test
  @DisplayName("canMoveTo should return false if a white pawn moves three steps up")
  public void canMoveToWhiteThreeStepsUp() {
    Board b = Board.create(); // Initial board, any pawn should be able to move one step up
    Piece pawn = Board.getPieceAt("d", "2", b).get();
    assertEquals(false, Pawn.canMoveTo("d", "5", Board.getPieces(b), pawn));
  }

  @Test
  @DisplayName("canMoveTo should return false if a white pawn moves two steps up and it is not in its initial position anymore")
  public void canMoveToWhiteTwoStepsUpAfterInitialPosition() {
    Board b = Board.create(); // Initial board, any pawn should be able to move one step up
    b = Board.doMove(Move.of(Position.of("d", "2"), Position.of("d", "3")), b);
    Piece pawn = Board.getPieceAt("d", "3", b).get();
    assertEquals(false, Pawn.canMoveTo("d", "5", Board.getPieces(b), pawn));
  }

  @Test
  @DisplayName("canMoveTo should return false if a piece is present at destination")
  public void canMoveToPieceAtDestination() {
    Board b = Board.create(); // Initial board, any pawn should be able to move one step up
    b = Board.doMove(Move.of(Position.of("d", "7"), Position.of("d", "4")), b);
    Piece pawn = Board.getPieceAt("d", "2", b).get();
    assertEquals(false, Pawn.canMoveTo("d", "4", Board.getPieces(b), pawn));
  }

  @Test
  @DisplayName("canMoveTo should return true if a white piece moves one step in diagonal and a black piece is there")
  public void canMoveToPieceDiagonal() {
    Board b = Board.create(); // Initial board, any pawn should be able to move one step up
    b = Board.doMove(Move.of(Position.of("e", "7"), Position.of("e", "3")), b);
    b = Board.doMove(Move.of(Position.of("c", "7"), Position.of("c", "3")), b);
    Piece pawn = Board.getPieceAt("d", "2", b).get();
    assertEquals(true, Pawn.canMoveTo("e", "3", Board.getPieces(b), pawn));
    assertEquals(true, Pawn.canMoveTo("c", "3", Board.getPieces(b), pawn));
  }

  @Test
  @DisplayName("canMoveTo should return false if a white piece moves one step in diagonal and a white piece is there")
  public void canMoveToPieceDiagonalSameColor() {
    Board b = Board.create(); // Initial board, any pawn should be able to move one step up
    b = Board.doMove(Move.of(Position.of("e", "2"), Position.of("e", "3")), b);
    Piece pawn = Board.getPieceAt("d", "2", b).get();
    assertEquals(false, Pawn.canMoveTo("e", "3", Board.getPieces(b), pawn));
  }

  @Test
  @DisplayName("canMoveTo should return false if a white piece moves one step in diagonal and no piece is there")
  public void canMoveToPieceDiagonalEmptySquare() {
    Board b = Board.create(); // Initial board, any pawn should be able to move one step up
    Piece pawn = Board.getPieceAt("d", "2", b).get();
    assertEquals(false, Pawn.canMoveTo("e", "3", Board.getPieces(b), pawn));
  }

  @Test
  @DisplayName("canMoveTo should return true if a black piece moves one step down")
  public void canMoveToPieceBlackDown() {
    Board b = Board.create(); // Initial board, any pawn should be able to move one step up
    Piece pawn = Board.getPieceAt("d", "7", b).get();
    assertEquals(true, Pawn.canMoveTo("d", "6", Board.getPieces(b), pawn));
  }

  @Test
  @DisplayName("canMoveTo should return false if a black pawn moves two steps down and it is not in its initial position anymore")
  public void canMoveToBlackTwoStepsDownAfterInitialPosition() {
    Board b = Board.create(); // Initial board, any pawn should be able to move one step up
    b = Board.doMove(Move.of(Position.of("d", "7"), Position.of("d", "6")), b);
    Piece pawn = Board.getPieceAt("d", "6", b).get();
    assertEquals(false, Pawn.canMoveTo("d", "4", Board.getPieces(b), pawn));
  }

  @Test
  @DisplayName("canMoveTo should return false if a black piece moves more than two steps down")
  public void canMoveToPieceBlack3Down() {
    Board b = Board.create(); // Initial board, any pawn should be able to move one step up
    Piece pawn = Board.getPieceAt("d", "7", b).get();
    assertEquals(false, Pawn.canMoveTo("d", "4", Board.getPieces(b), pawn));
  }
}
