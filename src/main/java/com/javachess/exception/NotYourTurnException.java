package com.javachess.exception;

import com.javachess.server.message.ErrorCode;

public class NotYourTurnException extends ChessException {
  public NotYourTurnException(String message) {
    super(ErrorCode.NOT_YOUR_TURN, message);
  }
}
