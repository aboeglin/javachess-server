package com.javachess.exception;

import com.javachess.server.message.ErrorCode;

public class GameNotFoundException extends ChessException {
  public GameNotFoundException(String message) {
    super(ErrorCode.GAME_NOT_FOUND, message);
  }
}
