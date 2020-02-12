package com.javachess.server.message;

import com.javachess.logic.Position;

public class PossibleMoves {
  private Position[] possibleMoves;

  public PossibleMoves(Position[] moves) {
    this.possibleMoves = moves;
  }

  public Position[] getPossibleMoves() {
    return possibleMoves;
  }

  public void setPossibleMoves(Position[] possibleMoves) {
    this.possibleMoves = possibleMoves;
  }
}
