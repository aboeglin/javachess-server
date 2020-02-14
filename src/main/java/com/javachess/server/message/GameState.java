package com.javachess.server.message;

import com.javachess.logic.Game;

public class GameState {

  private String status;

  private Game game;

  private boolean error;

  private ErrorCode errorCode;

  private String errorMessage;

  public GameState(String status, Game game) {
    this.status = status;
    this.game = game;
  }
}
