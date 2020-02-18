package com.javachess.logic;

public enum Color {
  BLACK,
  WHITE;

  public static Color getRandomColor() {
    return Color.WHITE;
  }

  public static Color getComplementColor(Color c) {
    return c == Color.WHITE ? Color.BLACK : Color.WHITE;
  }
}
