package com.javachess;

public class Rook extends Piece {

  private Rook(String x, String y, Color c) {
    this.x = x;
    this.y = y;
    this.color = c;
  }

  public static Rook of(String x, String y, Color c) {
    return new Rook(x, y, c);
  }

  public Queen moveTo(String x, String y) {
    return Queen.of(x, y, this.getColor());
  }

  @Override
  public boolean canMoveTo(String x, String y, Board b) {
    return false;
  }

  @Override
  public boolean equals(Object o) {
    return super.equals(o) && o instanceof Rook;
  }
}
