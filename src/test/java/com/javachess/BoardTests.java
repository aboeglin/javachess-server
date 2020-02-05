package com.javachess;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BoardTests {
  @Test
  @DisplayName("create should return a fresh board with 16 pawns")
  public void createPawns() {
    Board b = Board.create();
    assertEquals(Board.getPieceAt("a", "2", b).get(), Pawn.of("a", "2", Color.WHITE));
    assertEquals(Board.getPieceAt("b", "2", b).get(), Pawn.of("b", "2", Color.WHITE));
    assertEquals(Board.getPieceAt("c", "2", b).get(), Pawn.of("c", "2", Color.WHITE));
    assertEquals(Board.getPieceAt("d", "2", b).get(), Pawn.of("d", "2", Color.WHITE));
    assertEquals(Board.getPieceAt("e", "2", b).get(), Pawn.of("e", "2", Color.WHITE));
    assertEquals(Board.getPieceAt("f", "2", b).get(), Pawn.of("f", "2", Color.WHITE));
    assertEquals(Board.getPieceAt("g", "2", b).get(), Pawn.of("g", "2", Color.WHITE));
    assertEquals(Board.getPieceAt("h", "2", b).get(), Pawn.of("h", "2", Color.WHITE));

    assertEquals(Board.getPieceAt("a", "7", b).get(), Pawn.of("a", "7", Color.BLACK));
    assertEquals(Board.getPieceAt("b", "7", b).get(), Pawn.of("b", "7", Color.BLACK));
    assertEquals(Board.getPieceAt("c", "7", b).get(), Pawn.of("c", "7", Color.BLACK));
    assertEquals(Board.getPieceAt("d", "7", b).get(), Pawn.of("d", "7", Color.BLACK));
    assertEquals(Board.getPieceAt("e", "7", b).get(), Pawn.of("e", "7", Color.BLACK));
    assertEquals(Board.getPieceAt("f", "7", b).get(), Pawn.of("f", "7", Color.BLACK));
    assertEquals(Board.getPieceAt("g", "7", b).get(), Pawn.of("g", "7", Color.BLACK));
    assertEquals(Board.getPieceAt("h", "7", b).get(), Pawn.of("h", "7", Color.BLACK));
  }

  @Test
  @DisplayName("create should return a fresh board with 4 rooks")
  public void createRooks() {
    Board b = Board.create();
    assertEquals(Board.getPieceAt("a", "1", b).get(), Rook.of("a", "1", Color.WHITE));
    assertEquals(Board.getPieceAt("h", "1", b).get(), Rook.of("h", "1", Color.WHITE));

    assertEquals(Board.getPieceAt("a", "8", b).get(), Rook.of("a", "8", Color.BLACK));
    assertEquals(Board.getPieceAt("h", "8", b).get(), Rook.of("h", "8", Color.BLACK));
  }

  @Test
  @DisplayName("create should return a fresh board with 4 rooks")
  public void createBishops() {
    Board b = Board.create();
    assertEquals(Board.getPieceAt("b", "1", b).get(), Bishop.of("b", "1", Color.WHITE));
    assertEquals(Board.getPieceAt("g", "1", b).get(), Bishop.of("g", "1", Color.WHITE));

    assertEquals(Board.getPieceAt("b", "8", b).get(), Bishop.of("b", "8", Color.BLACK));
    assertEquals(Board.getPieceAt("g", "8", b).get(), Bishop.of("g", "8", Color.BLACK));
  }

  @Test
  @DisplayName("create should return a fresh board with 4 knights")
  public void createKnights() {
    Board b = Board.create();
    assertEquals(Board.getPieceAt("c", "1", b).get(), Knight.of("c", "1", Color.WHITE));
    assertEquals(Board.getPieceAt("f", "1", b).get(), Knight.of("f", "1", Color.WHITE));

    assertEquals(Board.getPieceAt("c", "8", b).get(), Knight.of("c", "8", Color.BLACK));
    assertEquals(Board.getPieceAt("f", "8", b).get(), Knight.of("f", "8", Color.BLACK));
  }

  @Test
  @DisplayName("create should return a fresh board with 2 queens")
  public void createQueens() {
    Board b = Board.create();
    assertEquals(Board.getPieceAt("d", "1", b).get(), Queen.of("d", "1", Color.WHITE));

    assertEquals(Board.getPieceAt("d", "8", b).get(), Queen.of("d", "8", Color.BLACK));
  }

  @Test
  @DisplayName("create should return a fresh board with 2 kings")
  public void createKings() {
    Board b = Board.create();
    assertEquals(Board.getPieceAt("e", "1", b).get(), King.of("e", "1", Color.WHITE));

    assertEquals(Board.getPieceAt("e", "8", b).get(), King.of("e", "8", Color.BLACK));
  }

  @Test
  @DisplayName("executeMove should return a new board the piece at the new position")
  public void executeMove() {
    Board b = Board.create();
    Board result = Board.executeMove("b", "7", "b", "3", b);
    Piece movedPiece = Board.getPieceAt("b", "3", result).get();
    assertEquals(Pawn.of("b", "3", Color.BLACK), movedPiece);
  }
}
