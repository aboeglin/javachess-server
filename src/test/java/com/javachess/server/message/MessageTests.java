package com.javachess.server.message;

import com.javachess.logic.Game;
import com.javachess.logic.Player;
import com.javachess.server.GameOrchestrator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageTests {
  @Test
  @DisplayName("JoinGameIn should have an email field of type string")
  public void JoinGameIn() {
    JoinGameIn joinGameIn = new JoinGameIn("user@domain.tld");
    String expected = "user@domain.tld";
    String actual = joinGameIn.getEmail();

    assertEquals(expected, actual);
  }
}
