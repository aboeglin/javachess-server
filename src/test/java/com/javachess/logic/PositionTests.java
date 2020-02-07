package com.javachess.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PositionTests {
  @Test
  @DisplayName("yAsInt should return an integer representation of y")
  public void yAsInt() {
    assertEquals(1, Position.yAsInt("1"));
  }

  @Test
  @DisplayName("xAsInt should return an integer representation of x")
  public void xAsInt() {
    assertEquals(5, Position.xAsInt("e"));
  }
}
