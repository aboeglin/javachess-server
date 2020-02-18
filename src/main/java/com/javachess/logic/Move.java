package com.javachess.logic;

public class Move {
  private Position from;
  private Position to;

  private Move(Position from, Position to) {
    this.from = from;
    this.to = to;
  }

  public static Move of(Position from, Position to) {
    return new Move(from, to);
  }

  public Position getFrom() {
    return from;
  }

  public Position getTo() {
    return to;
  }
}
