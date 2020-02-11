package com.javachess.logic;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class BoardTests {
  @Test
  @DisplayName("create should return a fresh board with 16 pawns")
  public void createPawns() {
    Board b = Board.create();
    assertEquals(Board.getPieceAt("a", "2", b).get(), Piece.of("a", "2", Color.WHITE, PieceType.PAWN));
    assertEquals(Board.getPieceAt("b", "2", b).get(), Piece.of("b", "2", Color.WHITE, PieceType.PAWN));
    assertEquals(Board.getPieceAt("c", "2", b).get(), Piece.of("c", "2", Color.WHITE, PieceType.PAWN));
    assertEquals(Board.getPieceAt("d", "2", b).get(), Piece.of("d", "2", Color.WHITE, PieceType.PAWN));
    assertEquals(Board.getPieceAt("e", "2", b).get(), Piece.of("e", "2", Color.WHITE, PieceType.PAWN));
    assertEquals(Board.getPieceAt("f", "2", b).get(), Piece.of("f", "2", Color.WHITE, PieceType.PAWN));
    assertEquals(Board.getPieceAt("g", "2", b).get(), Piece.of("g", "2", Color.WHITE, PieceType.PAWN));
    assertEquals(Board.getPieceAt("h", "2", b).get(), Piece.of("h", "2", Color.WHITE, PieceType.PAWN));

    assertEquals(Board.getPieceAt("a", "7", b).get(), Piece.of("a", "7", Color.BLACK, PieceType.PAWN));
    assertEquals(Board.getPieceAt("b", "7", b).get(), Piece.of("b", "7", Color.BLACK, PieceType.PAWN));
    assertEquals(Board.getPieceAt("c", "7", b).get(), Piece.of("c", "7", Color.BLACK, PieceType.PAWN));
    assertEquals(Board.getPieceAt("d", "7", b).get(), Piece.of("d", "7", Color.BLACK, PieceType.PAWN));
    assertEquals(Board.getPieceAt("e", "7", b).get(), Piece.of("e", "7", Color.BLACK, PieceType.PAWN));
    assertEquals(Board.getPieceAt("f", "7", b).get(), Piece.of("f", "7", Color.BLACK, PieceType.PAWN));
    assertEquals(Board.getPieceAt("g", "7", b).get(), Piece.of("g", "7", Color.BLACK, PieceType.PAWN));
    assertEquals(Board.getPieceAt("h", "7", b).get(), Piece.of("h", "7", Color.BLACK, PieceType.PAWN));
  }

  @Test
  @DisplayName("create should return a fresh board with 4 rooks")
  public void createRooks() {
    Board b = Board.create();
    assertEquals(Board.getPieceAt("a", "1", b).get(), Piece.of("a", "1", Color.WHITE, PieceType.ROOK));
    assertEquals(Board.getPieceAt("h", "1", b).get(), Piece.of("h", "1", Color.WHITE, PieceType.ROOK));

    assertEquals(Board.getPieceAt("a", "8", b).get(), Piece.of("a", "8", Color.BLACK, PieceType.ROOK));
    assertEquals(Board.getPieceAt("h", "8", b).get(), Piece.of("h", "8", Color.BLACK, PieceType.ROOK));
  }

  @Test
  @DisplayName("create should return a fresh board with 4 rooks")
  public void createBishops() {
    Board b = Board.create();
    assertEquals(Board.getPieceAt("c", "1", b).get(), Piece.of("c", "1", Color.WHITE, PieceType.BISHOP));
    assertEquals(Board.getPieceAt("f", "1", b).get(), Piece.of("f", "1", Color.WHITE, PieceType.BISHOP));

    assertEquals(Board.getPieceAt("c", "8", b).get(), Piece.of("c", "8", Color.BLACK, PieceType.BISHOP));
    assertEquals(Board.getPieceAt("f", "8", b).get(), Piece.of("f", "8", Color.BLACK, PieceType.BISHOP));
  }

  @Test
  @DisplayName("create should return a fresh board with 4 knights")
  public void createKnights() {
    Board b = Board.create();
    assertEquals(Board.getPieceAt("b", "1", b).get(), Piece.of("b", "1", Color.WHITE, PieceType.KNIGHT));
    assertEquals(Board.getPieceAt("g", "1", b).get(), Piece.of("g", "1", Color.WHITE, PieceType.KNIGHT));

    assertEquals(Board.getPieceAt("b", "8", b).get(), Piece.of("b", "8", Color.BLACK, PieceType.KNIGHT));
    assertEquals(Board.getPieceAt("g", "8", b).get(), Piece.of("g", "8", Color.BLACK, PieceType.KNIGHT));
  }

  @Test
  @DisplayName("create should return a fresh board with 2 queens")
  public void createQueens() {
    Board b = Board.create();
    assertEquals(Board.getPieceAt("d", "1", b).get(), Piece.of("d", "1", Color.WHITE, PieceType.QUEEN));

    assertEquals(Board.getPieceAt("d", "8", b).get(), Piece.of("d", "8", Color.BLACK, PieceType.QUEEN));
  }

  @Test
  @DisplayName("create should return a fresh board with 2 kings")
  public void createKings() {
    Board b = Board.create();
    assertEquals(Board.getPieceAt("e", "1", b).get(), Piece.of("e", "1", Color.WHITE, PieceType.KING));

    assertEquals(Board.getPieceAt("e", "8", b).get(), Piece.of("e", "8", Color.BLACK, PieceType.KING));
  }

  @Test
  @DisplayName("executeMove should return a new board the piece at the new position")
  public void executeMove() {
    Board b = Board.create();
    Board result = Board.executeMove("b", "7", "b", "3", b);
    Piece movedPiece = Board.getPieceAt("b", "3", result).get();
    assertEquals(Piece.of("b", "3", Color.BLACK, PieceType.PAWN), movedPiece);
  }

  @Test
  @DisplayName("getPossibleMoves should return a list of possible moves")
  public void getPossibleMoves() {
    Board b = Board.create();

    // White Pawn at start of game should have two options
    Position[] result = Board.getPossibleMoves("b", "2", b);
    List<Position> expected = new ArrayList<>();
    expected.add(Position.of("b", "3"));
    expected.add(Position.of("b", "4"));
    assertArrayEquals(expected.toArray(), result);
  }
}
