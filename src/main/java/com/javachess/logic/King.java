package com.javachess.logic;

import java.util.List;

public class King {
  private King() {
  }

  public static boolean canMoveTo(String x, String y, List<Piece> pieces, Piece piece) {
    Position from = Position.of(piece.getX(), piece.getY());
    Position to = Position.of(x, y);
    int diffX = to.getXAsInt() - from.getXAsInt();
    int diffY = to.getYAsInt() - from.getYAsInt();

    return (diffX == 0 || diffX == -1 || diffX == 1) && (diffY == 0 || diffY == 1 || diffY == -1);
  }
}
