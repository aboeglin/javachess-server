package com.javachess.server.message;

public class SelectPiece {
  private String playerId;
  private String x;
  private String y;

  public SelectPiece(String playerId, String x, String y) {
    this.playerId = playerId;
    this.x = x;
    this.y = y;
  }

  public String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }

  public String getX() {
    return x;
  }

  public void setX(String x) {
    this.x = x;
  }

  public String getY() {
    return y;
  }

  public void setY(String y) {
    this.y = y;
  }
}
