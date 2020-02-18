package com.javachess.logic;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class BishopTests {

  @Test
  @DisplayName("computeTrajectory should also work for top right direction")
  public void computeTrajectoryTopRight() {
    Position from = Position.of("b", "1");
    Position to = Position.of("h", "7");
    List<Position> expected = Arrays.asList(new Position[]{
      Position.of("c", "2"),
      Position.of("d", "3"),
      Position.of("e", "4"),
      Position.of("f", "5"),
      Position.of("g", "6"),
    });

    List<Position> actual = Bishop.computeTrajectory(from, to);
    assertArrayEquals(expected.toArray(), actual.toArray());
  }

  @Test
  @DisplayName("computeTrajectory should also work for top left direction")
  public void computeTrajectoryTopLeft() {
    Position from = Position.of("a", "7");
    Position to = Position.of("g", "1");
    List<Position> expected = Arrays.asList(new Position[]{
      Position.of("b", "6"),
      Position.of("c", "5"),
      Position.of("d", "4"),
      Position.of("e", "3"),
      Position.of("f", "2"),
    });

    List<Position> actual = Bishop.computeTrajectory(from, to);
    assertArrayEquals(expected.toArray(), actual.toArray());
  }

  @Test
  @DisplayName("computeTrajectory should also work for bottom left direction")
  public void computeTrajectoryBottomLeft() {
    Position from = Position.of("g", "7");
    Position to = Position.of("a", "1");
    List<Position> expected = Arrays.asList(new Position[]{
      Position.of("f", "6"),
      Position.of("e", "5"),
      Position.of("d", "4"),
      Position.of("c", "3"),
      Position.of("b", "2"),
    });

    List<Position> actual = Bishop.computeTrajectory(from, to);
    assertArrayEquals(expected.toArray(), actual.toArray());
  }

  @Test
  @DisplayName("computeTrajectory should also work for bottom right direction")
  public void computeTrajectoryBottomRight() {
    Position from = Position.of("b", "7");
    Position to = Position.of("h", "1");
    List<Position> expected = Arrays.asList(new Position[]{
      Position.of("c", "6"),
      Position.of("d", "5"),
      Position.of("e", "4"),
      Position.of("f", "3"),
      Position.of("g", "2"),
    });

    List<Position> actual = Bishop.computeTrajectory(from, to);
    assertArrayEquals(expected.toArray(), actual.toArray());
  }
}
