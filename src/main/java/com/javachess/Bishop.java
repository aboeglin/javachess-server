package com.javachess;

public class Bishop extends Piece {
  private Bishop(String x, String y, Color c) {
    this.x = x;
    this.y = y;
    this.color = c;
  }

  public static Bishop of(String x, String y, Color c) {
    return new Bishop(x, y, c);
  }

  public Bishop moveTo(String x, String y) {
    return Bishop.of(x, y, this.getColor());
  }

  @Override
  public boolean canMoveTo(String x, String y, Board b) {
    return false;
  }

  @Override
  public boolean equals(Object o) {
    return super.equals(o) && o instanceof Bishop;
  }
}
