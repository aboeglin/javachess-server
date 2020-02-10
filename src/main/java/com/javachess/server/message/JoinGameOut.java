package com.javachess.server.message;

public class JoinGameOut {
  private String message;

  public JoinGameOut(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
