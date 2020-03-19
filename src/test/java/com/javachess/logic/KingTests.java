package com.javachess.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class KingTests {

  @Test
  @DisplayName("canMoveTo should return true if the move is one step up")
  public void canMoveToOneStepUp() {
    Game g = Game.of(1, Player.of("white", Color.WHITE), Player.of("black", Color.BLACK));
    g = Game.doMove(Move.of(Position.of("e", "2"), Position.of("e", "3")), g);
    Piece king = Game.getPieceAt("e", "1", g).get();
    assertEquals(true, Piece.canMoveTo("e", "2", Game.getPieces(g), king));
  }

  @Test
  @DisplayName("canMoveTo should return true if the move is one step up")
  public void canMoveToOneStepDown() {
    Game g = Game.of(1, Player.of("white", Color.WHITE), Player.of("black", Color.BLACK));
    g = Game.doMove(Move.of(Position.of("e", "1"), Position.of("e", "5")), g);
    Piece king = Game.getPieceAt("e", "5", g).get();
    assertEquals(true, Piece.canMoveTo("e", "4", Game.getPieces(g), king));
  }

  @Test
  @DisplayName("canMoveTo should return true if the move is one step left")
  public void canMoveToOneStepLeft() {
    Game g = Game.of(1, Player.of("white", Color.WHITE), Player.of("black", Color.BLACK));
    g = Game.doMove(Move.of(Position.of("e", "1"), Position.of("e", "5")), g);
    Piece king = Game.getPieceAt("e", "5", g).get();
    assertEquals(true, Piece.canMoveTo("d", "5", Game.getPieces(g), king));
  }

  @Test
  @DisplayName("canMoveTo should return true if the move is one step right")
  public void canMoveToOneStepRight() {
    Game g = Game.of(1, Player.of("white", Color.WHITE), Player.of("black", Color.BLACK));
    g = Game.doMove(Move.of(Position.of("e", "1"), Position.of("e", "5")), g);
    Piece king = Game.getPieceAt("e", "5", g).get();
    assertEquals(true, Piece.canMoveTo("f", "5", Game.getPieces(g), king));
  }

  @Test
  @DisplayName("canMoveTo should return true if the move is one step top left")
  public void canMoveToOneStepTopLeft() {
    Game g = Game.of(1, Player.of("white", Color.WHITE), Player.of("black", Color.BLACK));
    g = Game.doMove(Move.of(Position.of("e", "1"), Position.of("e", "5")), g);
    Piece king = Game.getPieceAt("e", "5", g).get();
    assertEquals(true, Piece.canMoveTo("d", "6", Game.getPieces(g), king));
  }

  @Test
  @DisplayName("canMoveTo should return true if the move is one step top right")
  public void canMoveToOneStepTopRight() {
    Game g = Game.of(1, Player.of("white", Color.WHITE), Player.of("black", Color.BLACK));
    g = Game.doMove(Move.of(Position.of("e", "1"), Position.of("e", "5")), g);
    Piece king = Game.getPieceAt("e", "5", g).get();
    assertEquals(true, Piece.canMoveTo("f", "6", Game.getPieces(g), king));
  }

  @Test
  @DisplayName("canMoveTo should return true if the move is one step bottom left")
  public void canMoveToOneStepBottomLeft() {
    Game g = Game.of(1, Player.of("white", Color.WHITE), Player.of("black", Color.BLACK));
    g = Game.doMove(Move.of(Position.of("e", "1"), Position.of("e", "5")), g);
    Piece king = Game.getPieceAt("e", "5", g).get();
    assertEquals(true, Piece.canMoveTo("d", "4", Game.getPieces(g), king));
  }

  @Test
  @DisplayName("canMoveTo should return true if the move is one step bottom right")
  public void canMoveToOneStepBottomRight() {
    Game g = Game.of(1, Player.of("white", Color.WHITE), Player.of("black", Color.BLACK));
    g = Game.doMove(Move.of(Position.of("e", "1"), Position.of("e", "5")), g);
    Piece king = Game.getPieceAt("e", "5", g).get();
    assertEquals(true, Piece.canMoveTo("f", "4", Game.getPieces(g), king));
  }

}
