package com.javachess.exception;

import com.javachess.server.message.ErrorCode;

public class PlayerNotInGameException extends ChessException {
  public PlayerNotInGameException(String message) {
    super(ErrorCode.PLAYER_NOT_IN_GAME, message);
  }
}
