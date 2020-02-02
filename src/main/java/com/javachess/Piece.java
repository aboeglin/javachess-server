package com.javachess;

public class Piece {
  private String x;
  private String y;
  private Color c;
  private PieceType t;

  public Piece(String x, String y, Color c, PieceType t) {
    this.x = x;
    this.y = y;
    this.c = c;
    this.t = t;
  }
}