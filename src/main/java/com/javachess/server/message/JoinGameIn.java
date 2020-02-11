package com.javachess.server.message;

public class JoinGameIn {
  private String email;

  public JoinGameIn(String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
