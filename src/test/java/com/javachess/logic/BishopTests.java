package com.javachess.logic;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class BishopTests {

  @Test
  @DisplayName("computeTrajectory")
  public void computeTrajectory() {
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
  @DisplayName("computeTrajectory should also work for decreasing numbers")
  public void computeTrajectoryDec() {
    Position from = Position.of("h", "7");
    Position to = Position.of("b", "1");
    List<Position> expected = Arrays.asList(new Position[]{
      Position.of("g", "6"),
      Position.of("f", "5"),
      Position.of("e", "4"),
      Position.of("d", "3"),
      Position.of("c", "2"),
    });

    List<Position> actual = Bishop.computeTrajectory(from, to);
    assertArrayEquals(expected.toArray(), actual.toArray());
  }
}
