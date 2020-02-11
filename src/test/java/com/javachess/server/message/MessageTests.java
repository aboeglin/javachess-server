package com.javachess.server.message;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
}
