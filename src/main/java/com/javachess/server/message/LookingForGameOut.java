package com.javachess.server.message;

public class LookingForGameOut {

  private String message;

  public LookingForGameOut(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
