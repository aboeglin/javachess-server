package com.javachess.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTests {
  @Test
  @DisplayName("Equals should return true if two player objects have the same id")
  public void equals() {
    Player player1 = Player.of("John");
    Player player2 = Player.of("John");
    assertEquals(true, player1.equals(player2));
  }
}
