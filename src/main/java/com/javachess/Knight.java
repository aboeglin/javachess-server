package com.javachess;

public class Knight extends Piece {
  private Knight(String x, String y, Color c) {
    this.x = x;
    this.y = y;
    this.color = c;
  }

  public static Knight of(String x, String y, Color c) {
    return new Knight(x, y, c);
  }

  public Knight moveTo(String x, String y) {
    return Knight.of(x, y, this.getColor());
  }

  @Override
  public boolean canMoveTo(String x, String y, Board b) {
    return false;
  }

  @Override
  public boolean equals(Object o) {
    return super.equals(o) && o instanceof Knight;
  }
}
