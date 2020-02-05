package com.javachess;

import java.util.Map;
import java.util.HashMap;

public abstract class Piece {
  protected String x;
  protected String y;
  protected Color color;

  private final static Map<String, Integer> getXPositionsMap() {
    final Map<String, Integer> m = new HashMap<String, Integer>(8);
    m.put("a", 1);
    m.put("b", 2);
    m.put("c", 3);
    m.put("d", 4);
    m.put("e", 5);
    m.put("f", 6);
    m.put("g", 7);
    m.put("h", 8);
    return m;
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

  public abstract Piece moveTo(String x, String y);

  public abstract boolean canMoveTo(String x, String y, Board b);

  @Override
  public boolean equals(Object o) {
    if (o instanceof Piece) {
      Piece p = (Piece) o;
      return this.x == p.x && this.y == p.y && this.color == p.color;
    } else {
      return false;
    }
  }
}