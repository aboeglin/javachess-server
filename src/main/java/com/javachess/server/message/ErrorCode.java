package com.javachess.server.message;

public enum ErrorCode {
  MOVE_NOT_ALLOWED(661);

  public final int code;

  private ErrorCode(int code) {
    this.code = code;
  }
}