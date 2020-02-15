package com.javachess.logic;

import java.util.List;

public class Piece {
  protected String x;
  protected String y;
  protected Color color;
  protected PieceType type;

  private Piece(String x, String y, Color c, PieceType t) {
    this.x = x;
    this.y = y;
    this.color = c;
    this.type = t;
  }

  public static Piece of(String x, String y, Color c, PieceType t) {
    return new Piece(x, y, c, t);
  }

  public static String getX(Piece p) {
    return p.x;
  }

  public static String getY(Piece p) {
    return p.y;
  }

  public Color getColor() {
    return this.color;
  }

  public PieceType getType() {
    return this.type;
  }

  public static Piece moveTo(String x, String y, Piece p) {
    return Piece.of(x, y, p.getColor(), p.getType());
  }

  public static boolean canMoveTo(String x, String y, List<Piece> pieces, Piece p) {
    switch (p.type) {
      case PAWN:
        return Pawn.canMoveTo(x, y, pieces, p);
      default:
        return false;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Piece) {
      Piece p = (Piece) o;
      return this.x.equals(p.x) && this.y.equals(p.y) && this.getColor() == p.getColor() && this.getType() == p.getType();
    } else {
      return false;
    }
  }
}
