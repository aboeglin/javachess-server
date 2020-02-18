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
  @DisplayName("getYAsInt should return an integer representation of y")
  public void getYAsInt() {
    assertEquals(1, Position.of("a", "1").getYAsInt());
  }

  @Test
  @DisplayName("xAsInt should return an integer representation of x")
  public void xAsInt() {
    assertEquals(5, Position.xAsInt("e"));
  }

  @Test
  @DisplayName("getXAsInt should return an integer representation of x")
  public void getXAsInt() {
    assertEquals(5, Position.of("e", "2").getXAsInt());
  }

  @Test
  @DisplayName("Position::of should also build a Position from two integers")
  public void ofInt() {
    Position p = Position.of(1, 2);
    assertEquals("a", p.getX());
    assertEquals("2", p.getY());
  }

  @Test
  @DisplayName("equals should return false if it's tested against another Class")
  public void equalsOtherClass() {
    Position p = Position.of("a", "2");
    assertEquals(false, p.equals(new String("false")));
  }
}
