package com.javachess.exception;

import com.javachess.server.message.ErrorCode;

public class ChessException extends Exception {
  private ErrorCode errorCode;
  private String message;

  public ChessException(ErrorCode errorCode, String message) {
    this.errorCode = errorCode;
    this.message = message;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }

  public String getMessage() {
    return message;
  }
}
