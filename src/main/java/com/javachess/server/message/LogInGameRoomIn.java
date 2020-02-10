package com.javachess.server.message;

public class LogInGameRoomIn {
  private String email;

  public LogInGameRoomIn(String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
