package com.javachess.server.message;

public class SelectPiece {
  private String email;
  private String x;
  private String y;

  public SelectPiece(String email, String x, String y) {
    this.email = email;
    this.x = x;
    this.y = y;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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
