package com.javachess.exception;

import com.javachess.server.message.ErrorCode;

public class GameAlreadyFullException extends ChessException {
  public GameAlreadyFullException(String message) {
    super(ErrorCode.GAME_ALREADY_FULL, message);
  }
}
