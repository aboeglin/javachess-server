package com.javachess.logic;

import java.util.List;

public class Knight {
  private Knight() {
  }

  public static boolean canMoveTo(String x, String y, List<Piece> pieces, Piece piece) {
    int offsetX = Math.abs(Position.computeXOffset(x, piece.getX()));
    int offsetY = Math.abs(Position.computeYOffset(y, piece.getY()));

    return offsetX == 1 && offsetY == 2 || offsetX == 2 && offsetY == 1;
  }
}
