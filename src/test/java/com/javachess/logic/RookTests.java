package com.javachess.logic;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class RookTests {

  @Test
  @DisplayName("computeTrajectory should work for left direction")
  public void computeTrajectoryLeft() {
    Position from = Position.of("e", "4");
    Position to = Position.of("a", "4");
    List<Position> expected = Arrays.asList(new Position[]{
      Position.of("d", "4"),
      Position.of("c", "4"),
      Position.of("b", "4"),
    });

    List<Position> actual = Rook.computeTrajectory(from, to);
    assertArrayEquals(expected.toArray(), actual.toArray());
  }

  @Test
  @DisplayName("computeTrajectory should work for right direction")
  public void computeTrajectoryRight() {
    Position from = Position.of("a", "4");
    Position to = Position.of("e", "4");
    List<Position> expected = Arrays.asList(new Position[]{
      Position.of("b", "4"),
      Position.of("c", "4"),
      Position.of("d", "4"),
    });

    List<Position> actual = Rook.computeTrajectory(from, to);
    assertArrayEquals(expected.toArray(), actual.toArray());
  }

  @Test
  @DisplayName("computeTrajectory should work for bottom direction")
  public void computeTrajectoryBottom() {
    Position from = Position.of("a", "5");
    Position to = Position.of("a", "1");
    List<Position> expected = Arrays.asList(new Position[]{
      Position.of("a", "4"),
      Position.of("a", "3"),
      Position.of("a", "2"),
    });

    List<Position> actual = Rook.computeTrajectory(from, to);
    assertArrayEquals(expected.toArray(), actual.toArray());
  }

  @Test
  @DisplayName("computeTrajectory should work for top direction")
  public void computeTrajectoryTop() {
    Position from = Position.of("a", "1");
    Position to = Position.of("a", "5");
    List<Position> expected = Arrays.asList(new Position[]{
      Position.of("a", "2"),
      Position.of("a", "3"),
      Position.of("a", "4"),
    });

    List<Position> actual = Rook.computeTrajectory(from, to);
    assertArrayEquals(expected.toArray(), actual.toArray());
  }

  @Test
  @DisplayName("canMoveTo should return true if the move is straight and there's no piece in between")
  public void canMoveToNoInBetween() {
    Game g = Game.of(1, Player.of("white", Color.WHITE), Player.of("black", Color.BLACK));
    g = Game.doMove(Move.of(Position.of("a", "2"), Position.of("b", "3")), g);
    Piece rook = Game.getPieceAt("a", "1", g).get();
    assertEquals(true, Piece.canMoveTo("a", "5", Game.getPieces(g), rook));
  }

  @Test
  @DisplayName("canMoveTo should return false if the move is straight and there's a piece in between")
  public void canMoveToInBetween() {
    Game g = Game.of(1, Player.of("white", Color.WHITE), Player.of("black", Color.BLACK));
    Piece rook = Game.getPieceAt("a", "1", g).get();
    assertEquals(false, Piece.canMoveTo("a", "5", Game.getPieces(g), rook));
  }

  @Test
  @DisplayName("canMoveTo should return false if the move is not straight")
  public void canMoveToNoStraight() {
    Game g = Game.of(1, Player.of("white", Color.WHITE), Player.of("black", Color.BLACK));
    g = Game.doMove(Move.of(Position.of("a", "2"), Position.of("h", "3")), g);
    g = Game.doMove(Move.of(Position.of("b", "2"), Position.of("g", "3")), g);
    Piece rook = Game.getPieceAt("a", "1", g).get();
    assertEquals(false, Piece.canMoveTo("c", "3", Game.getPieces(g), rook));
  }

}
