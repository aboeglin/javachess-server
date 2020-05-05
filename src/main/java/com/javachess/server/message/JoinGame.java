package com.javachess.server.message;

public class JoinGame {
  private String playerId;

  public JoinGame(String playerId) {
    this.playerId = playerId;
  }

  public String getPlayerId() {
    return playerId;
  }
}
