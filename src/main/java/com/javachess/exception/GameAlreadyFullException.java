package com.javachess.exception;

public class GameAlreadyFullException extends Exception {

  private String message;

  public GameAlreadyFullException(String message) {
    this.message = message;
  }

  @Override
  public String getMessage() {
    return this.message;
  }
}
