package com.javachess.server.message;

import com.javachess.logic.Position;

public class GameUpdate {

  private String status;

  private GameState game;

  private boolean error;

  private ErrorCode errorCode;

  private String errorMessage;

  private Position[] possibleMoves;

  public GameUpdate(String status, GameState game) {
    this.status = status;
    this.game = game;
    this.possibleMoves = new Position[]{};
  }

  public GameUpdate(String status, GameState game, ErrorCode errorCode, String errorMessage) {
    this.status = status;
    this.game = game;
    this.errorMessage = errorMessage;
    this.errorCode = errorCode;
    this.error = true;
    this.possibleMoves = new Position[]{};
  }
}
