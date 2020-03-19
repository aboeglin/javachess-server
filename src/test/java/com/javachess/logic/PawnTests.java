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
    Game g = Game.of(1, Player.of("white", Color.WHITE), Player.of("black", Color.BLACK));
    Piece pawn = Game.getPieceAt("d", "2", g).get();
    assertEquals(true, Piece.canMoveTo("d", "3", Game.getPieces(g), pawn));
  }

  @Test
  @DisplayName("canMoveTo should return false if a white pawn moves three steps up")
  public void canMoveToWhiteThreeStepsUp() {
    Game g = Game.of(1, Player.of("white", Color.WHITE), Player.of("black", Color.BLACK));
    Piece pawn = Game.getPieceAt("d", "2", g).get();
    assertEquals(false, Piece.canMoveTo("d", "5", Game.getPieces(g), pawn));
  }

  @Test
  @DisplayName("canMoveTo should return false if a white pawn moves two steps up and it is not in its initial position anymore")
  public void canMoveToWhiteTwoStepsUpAfterInitialPosition() {
    Game g = Game.of(1, Player.of("white", Color.WHITE), Player.of("black", Color.BLACK));
    g = Game.doMove(Move.of(Position.of("d", "2"), Position.of("d", "3")), g);
    Piece pawn = Game.getPieceAt("d", "3", g).get();
    assertEquals(false, Piece.canMoveTo("d", "5", Game.getPieces(g), pawn));
  }

  @Test
  @DisplayName("canMoveTo should return false if a piece is present at destination")
  public void canMoveToPieceAtDestination() {
    Game g = Game.of(1, Player.of("white", Color.WHITE), Player.of("black", Color.BLACK));
    g = Game.doMove(Move.of(Position.of("d", "7"), Position.of("d", "4")), g);
    Piece pawn = Game.getPieceAt("d", "2", g).get();
    assertEquals(false, Piece.canMoveTo("d", "4", Game.getPieces(g), pawn));
  }

  @Test
  @DisplayName("canMoveTo should return true if a white piece moves one step in diagonal and a black piece is there")
  public void canMoveToWhitePieceDiagonal() {
    Game g = Game.of(1, Player.of("white", Color.WHITE), Player.of("black", Color.BLACK));
    g = Game.doMove(Move.of(Position.of("e", "7"), Position.of("e", "3")), g);
    g = Game.doMove(Move.of(Position.of("c", "7"), Position.of("c", "3")), g);
    Piece pawn = Game.getPieceAt("d", "2", g).get();
    assertEquals(true, Piece.canMoveTo("e", "3", Game.getPieces(g), pawn));
    assertEquals(true, Piece.canMoveTo("c", "3", Game.getPieces(g), pawn));
  }

  @Test
  @DisplayName("canMoveTo should return true if a black piece moves one step in diagonal and a white piece is there")
  public void canMoveToBlackPieceDiagonal() {
    Game g = Game.of(1, Player.of("white", Color.WHITE), Player.of("black", Color.BLACK));
    g = Game.doMove(Move.of(Position.of("e", "2"), Position.of("e", "6")), g);
    g = Game.doMove(Move.of(Position.of("c", "2"), Position.of("c", "6")), g);
    Piece pawn = Game.getPieceAt("d", "7", g).get();
    assertEquals(true, Piece.canMoveTo("e", "6", Game.getPieces(g), pawn));
    assertEquals(true, Piece.canMoveTo("c", "6", Game.getPieces(g), pawn));
  }

  @Test
  @DisplayName("canMoveTo should return false if a white piece moves one step in diagonal and a white piece is there")
  public void canMoveToPieceDiagonalSameColor() {
    Game g = Game.of(1, Player.of("white", Color.WHITE), Player.of("black", Color.BLACK));
    g = Game.doMove(Move.of(Position.of("e", "2"), Position.of("e", "3")), g);
    Piece pawn = Game.getPieceAt("d", "2", g).get();
    assertEquals(false, Piece.canMoveTo("e", "3", Game.getPieces(g), pawn));
  }

  @Test
  @DisplayName("canMoveTo should return false if a white piece moves one step in diagonal and no piece is there")
  public void canMoveToPieceDiagonalEmptySquare() {
    Game g = Game.of(1, Player.of("white", Color.WHITE), Player.of("black", Color.BLACK));
    Piece pawn = Game.getPieceAt("d", "2", g).get();
    assertEquals(false, Piece.canMoveTo("e", "3", Game.getPieces(g), pawn));
  }

  @Test
  @DisplayName("canMoveTo should return true if a black piece moves one step down")
  public void canMoveToPieceBlackDown() {
    Game g = Game.of(1, Player.of("white", Color.WHITE), Player.of("black", Color.BLACK));
    Piece pawn = Game.getPieceAt("d", "7", g).get();
    assertEquals(true, Piece.canMoveTo("d", "6", Game.getPieces(g), pawn));
  }

  @Test
  @DisplayName("canMoveTo should return false if there's a piece in front of it")
  public void canMoveToPieceWhite2UpPieceInBetween() {
    Game g = Game.of(1, Player.of("white", Color.WHITE), Player.of("black", Color.BLACK));
    g = Game.doMove(Move.of(Position.of("d", "7"), Position.of("d", "3")), g);
    Piece pawn = Game.getPieceAt("d", "2", g).get();
    assertEquals(false, Piece.canMoveTo("d", "4", Game.getPieces(g), pawn));
  }

  @Test
  @DisplayName("canMoveTo should return false if there's a piece in front of it")
  public void canMoveToPieceBlack2DownPieceInBetween() {
    Game g = Game.of(1, Player.of("white", Color.WHITE), Player.of("black", Color.BLACK));
    g = Game.doMove(Move.of(Position.of("d", "2"), Position.of("d", "6")), g);
    Piece pawn = Game.getPieceAt("d", "7", g).get();
    assertEquals(false, Piece.canMoveTo("d", "5", Game.getPieces(g), pawn));
  }

  @Test
  @DisplayName("canMoveTo should return false if a black pawn moves two steps down and it is not in its initial position anymore")
  public void canMoveToBlackTwoStepsDownAfterInitialPosition() {
    Game g = Game.of(1, Player.of("white", Color.WHITE), Player.of("black", Color.BLACK));
    g = Game.doMove(Move.of(Position.of("d", "7"), Position.of("d", "6")), g);
    Piece pawn = Game.getPieceAt("d", "6", g).get();
    assertEquals(false, Piece.canMoveTo("d", "4", Game.getPieces(g), pawn));
  }

  @Test
  @DisplayName("canMoveTo should return false if a black piece moves more than two steps down")
  public void canMoveToPieceBlack3Down() {
    Game g = Game.of(1, Player.of("white", Color.WHITE), Player.of("black", Color.BLACK));
    Piece pawn = Game.getPieceAt("d", "7", g).get();
    assertEquals(false, Piece.canMoveTo("d", "4", Game.getPieces(g), pawn));
  }
}
