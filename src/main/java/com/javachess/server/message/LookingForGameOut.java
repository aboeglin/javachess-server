package com.javachess.server.message;

public class LookingForGameOut {

  private String message;

  private int gameId;

  public LookingForGameOut(String message, int gameId) {
    this.message = message;
    this.gameId = gameId;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public int getGameId() {
    return gameId;
  }

  public void setGameId(int gameId) {
    this.gameId = gameId;
  }
}
