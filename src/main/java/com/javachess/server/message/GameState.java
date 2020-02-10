package com.javachess.server.message;

import com.javachess.logic.Game;

public class GameState {
  private String status;

  private Game game;

  public GameState(String status, Game game) {
    this.status = status;
    this.game = game;
  }
}
