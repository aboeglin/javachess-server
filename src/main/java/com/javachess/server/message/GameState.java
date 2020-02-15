package com.javachess.server.message;

import com.javachess.logic.Game;

public class GameState {

  private String status;

  private GameMessage game;

  private boolean error;

  private ErrorCode errorCode;

  private String errorMessage;

  public GameState(String status, GameMessage game) {
    this.status = status;
    this.game = game;
  }

  public GameState(String status, GameMessage game, ErrorCode errorCode, String errorMessage) {
    this.status = status;
    this.game = game;
    this.errorMessage = errorMessage;
    this.errorCode = errorCode;
    this.error = true;
  }
}
