package com.javachess.server.message;

public class LookingForGameIn {
  private String email;

  public LookingForGameIn(String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
