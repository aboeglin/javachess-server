package com.javachess.server.message;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageTests {
  @Test
  @DisplayName("JoinGameIn should have a playerId field")
  public void JoinGameInGetters() {
    JoinGame joinGame = new JoinGame("user@domain.tld");
    String expected = "user@domain.tld";
    String actual = joinGame.getPlayerId();

    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("SelectPiece should have playerId, x and y fields")
  public void SelectPieceGetters() {
    String playerId = "user@domain.tld";
    String x = "a";
    String y = "1";
    SelectPiece selectPiece = new SelectPiece(playerId, x, y);

    assertEquals(playerId, selectPiece.getPlayerId());
    assertEquals(x, selectPiece.getX());
    assertEquals(y, selectPiece.getY());
  }

  @Test
  @DisplayName("SelectPiece should have setters for playerId, x and y fields")
  public void SelectPieceSetters() {
    String playerId = "user@domain.tld";
    String x = "a";
    String y = "1";
    SelectPiece selectPiece = new SelectPiece("", "", "");

    selectPiece.setPlayerId(playerId);
    selectPiece.setX(x);
    selectPiece.setY(y);

    assertEquals(playerId, selectPiece.getPlayerId());
    assertEquals(x, selectPiece.getX());
    assertEquals(y, selectPiece.getY());
  }

  @Test
  @DisplayName("PerformMove should have playerId, fromX, fromY, toX, toY fields")
  public void PerformMoveGetters() {
    String playerId = "user@domain.tld";
    String fromX = "a";
    String fromY = "1";
    String toX = "b";
    String toY = "1";
    PerformMove performMove = new PerformMove(playerId, fromX, fromY, toX, toY);

    assertEquals(playerId, performMove.getPlayerId());
    assertEquals(fromX, performMove.getFromX());
    assertEquals(fromY, performMove.getFromY());
    assertEquals(toX, performMove.getToX());
    assertEquals(toY, performMove.getToY());
  }

  @Test
  @DisplayName("PerformMove should have setters for playerId, fromX, fromY, toX, toY fields")
  public void PerformMoveSetters() {
    String playerId = "user@domain.tld";
    String fromX = "a";
    String fromY = "1";
    String toX = "b";
    String toY = "1";
    PerformMove performMove = new PerformMove("", "", "", "", "");

    performMove.setPlayerId(playerId);
    performMove.setFromX(fromX);
    performMove.setFromY(fromY);
    performMove.setToX(toX);
    performMove.setToY(toY);

    assertEquals(playerId, performMove.getPlayerId());
    assertEquals(fromX, performMove.getFromX());
    assertEquals(fromY, performMove.getFromY());
    assertEquals(toX, performMove.getToX());
    assertEquals(toY, performMove.getToY());
  }
}
