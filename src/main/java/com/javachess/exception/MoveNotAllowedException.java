package com.javachess.exception;

import com.javachess.server.message.ErrorCode;

public class MoveNotAllowedException extends ChessException {
  public MoveNotAllowedException(String message) {
    super(ErrorCode.MOVE_NOT_ALLOWED, message);
  }
}
