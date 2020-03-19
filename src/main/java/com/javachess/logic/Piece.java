package com.javachess.logic;

import java.util.List;
import java.util.Optional;

public class Piece {
  private String x;
  private String y;
  private Color color;
  private PieceType type;

  private Piece(String x, String y, Color c, PieceType t) {
    this.x = x;
    this.y = y;
    this.color = c;
    this.type = t;
  }

  public static Piece of(String x, String y, Color c, PieceType t) {
    return new Piece(x, y, c, t);
  }

  public String getX() {
    return this.x;
  }

  public String getY() {
    return this.y;
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
    Optional<Piece> target = Game.getPieceAt(x, y, pieces);

    if (target.isPresent() && target.get().getColor() == p.getColor()) {
      return false;
    }

    switch (p.type) {
      case PAWN:
        return Pawn.canMoveTo(x, y, pieces, p);
      case KNIGHT:
        return Knight.canMoveTo(x, y, pieces, p);
      case BISHOP:
        return Bishop.canMoveTo(x, y, pieces, p);
      case ROOK:
        return Rook.canMoveTo(x, y, pieces, p);
      case QUEEN:
        return Rook.canMoveTo(x, y, pieces, p) || Bishop.canMoveTo(x, y, pieces, p);
      case KING:
        return King.canMoveTo(x, y, pieces, p);
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
