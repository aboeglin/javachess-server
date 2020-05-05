package com.javachess.server.message;

public class PerformMove {

  private String playerId;
  private String fromX;
  private String fromY;
  private String toX;
  private String toY;

  public PerformMove(
    String playerId,
    String fromX, String fromY,
    String toX, String toY
  ) {
    this.playerId = playerId;
    this.fromX = fromX;
    this.fromY = fromY;
    this.toX = toX;
    this.toY = toY;
  }

  public String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }

  public String getFromX() {
    return fromX;
  }

  public void setFromX(String fromX) {
    this.fromX = fromX;
  }

  public String getFromY() {
    return fromY;
  }

  public void setFromY(String fromY) {
    this.fromY = fromY;
  }

  public String getToX() {
    return toX;
  }

  public void setToX(String toX) {
    this.toX = toX;
  }

  public String getToY() {
    return toY;
  }

  public void setToY(String toY) {
    this.toY = toY;
  }
}
