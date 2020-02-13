package com.javachess.server.message;

import com.javachess.logic.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageTests {
  @Test
  @DisplayName("JoinGameIn should have an email field")
  public void JoinGameInGetters() {
    JoinGameIn joinGameIn = new JoinGameIn("user@domain.tld");
    String expected = "user@domain.tld";
    String actual = joinGameIn.getEmail();

    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("JoinGameIn should have an email setter")
  public void JoinGameInSetter() {
    JoinGameIn joinGameIn = new JoinGameIn("user@domain.tld");
    String expected = "expected@domain.tld";

    joinGameIn.setEmail(expected);
    String actual = joinGameIn.getEmail();

    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("LookingForGameIn should have an email field")
  public void LookingForGameInGetters() {
    LookingForGameIn lookingForGameIn = new LookingForGameIn("user@domain.tld");
    String expected = "user@domain.tld";
    String actual = lookingForGameIn.getEmail();

    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("LookingForGameIn should have an email setter")
  public void LookingForGameInSetter() {
    LookingForGameIn lookingForGameIn = new LookingForGameIn("user@domain.tld");
    String expected = "expected@domain.tld";

    lookingForGameIn.setEmail(expected);
    String actual = lookingForGameIn.getEmail();

    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("LookingForGameOut should have message and gameId fields")
  public void LookingForGameOutGetters() {
    LookingForGameOut lookingForGameOut = new LookingForGameOut("message", 2);

    String message = lookingForGameOut.getMessage();
    int id = lookingForGameOut.getGameId();

    assertEquals("message", message);
    assertEquals(2, id);
  }

  @Test
  @DisplayName("LookingForGameOut should have message and gameId setters")
  public void LookingForGameOutSetters() {
    LookingForGameOut lookingForGameOut = new LookingForGameOut("message", 2);

    lookingForGameOut.setMessage("universe");
    lookingForGameOut.setGameId(42);

    String message = lookingForGameOut.getMessage();
    int id = lookingForGameOut.getGameId();

    assertEquals("universe", message);
    assertEquals(42, id);
  }

  @Test
  @DisplayName("PossibleMoves should have possibleMoves field")
  public void PossibleMovesGetters() {
    Position[] expected = {Position.of("b", "3")};
    PossibleMoves possibleMoves = new PossibleMoves(expected);

    Position[] actual = possibleMoves.getPossibleMoves();

    assertArrayEquals(expected, actual);
  }

  @Test
  @DisplayName("PossibleMoves should have possibleMoves setter")
  public void PossibleMovesSetters() {
    Position[] expected = {Position.of("b", "3")};
    PossibleMoves possibleMoves = new PossibleMoves(null);
    possibleMoves.setPossibleMoves(expected);

    Position[] actual = possibleMoves.getPossibleMoves();

    assertArrayEquals(expected, actual);
  }

  @Test
  @DisplayName("SelectPiece should have email, x and y fields")
  public void SelectPieceGetters() {
    String email = "user@domain.tld";
    String x = "a";
    String y = "1";
    SelectPiece selectPiece = new SelectPiece(email, x, y);

    assertEquals(email, selectPiece.getEmail());
    assertEquals(x, selectPiece.getX());
    assertEquals(y, selectPiece.getY());
  }

  @Test
  @DisplayName("SelectPiece should have setters for email, x and y fields")
  public void SelectPieceSetters() {
    String email = "user@domain.tld";
    String x = "a";
    String y = "1";
    SelectPiece selectPiece = new SelectPiece("", "", "");

    selectPiece.setEmail(email);
    selectPiece.setX(x);
    selectPiece.setY(y);

    assertEquals(email, selectPiece.getEmail());
    assertEquals(x, selectPiece.getX());
    assertEquals(y, selectPiece.getY());
  }

  @Test
  @DisplayName("PerformMove should have email, fromX, fromY, toX, toY fields")
  public void PerformMoveGetters() {
    String email = "user@domain.tld";
    String fromX = "a";
    String fromY = "1";
    String toX = "b";
    String toY = "1";
    PerformMove performMove = new PerformMove(email, fromX, fromY, toX, toY);

    assertEquals(email, performMove.getEmail());
    assertEquals(fromX, performMove.getFromX());
    assertEquals(fromY, performMove.getFromY());
    assertEquals(toX, performMove.getToX());
    assertEquals(toY, performMove.getToY());
  }

  @Test
  @DisplayName("PerformMove should have setters for email, fromX, fromY, toX, toY fields")
  public void PerformMoveSetters() {
    String email = "user@domain.tld";
    String fromX = "a";
    String fromY = "1";
    String toX = "b";
    String toY = "1";
    PerformMove performMove = new PerformMove("", "", "", "", "");

    performMove.setEmail(email);
    performMove.setFromX(fromX);
    performMove.setFromY(fromY);
    performMove.setToX(toX);
    performMove.setToY(toY);

    assertEquals(email, performMove.getEmail());
    assertEquals(fromX, performMove.getFromX());
    assertEquals(fromY, performMove.getFromY());
    assertEquals(toX, performMove.getToX());
    assertEquals(toY, performMove.getToY());
  }
}
