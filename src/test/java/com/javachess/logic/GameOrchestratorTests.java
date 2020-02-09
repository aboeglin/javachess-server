package com.javachess.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameOrchestratorTests {
  @Test
  @DisplayName("Register player should return a game with one player when called the first time")
  public void registerPlayerOnce() {
    GameOrchestrator orchestrator = new GameOrchestrator();
    Player player1 = Player.of("John");
    Game expected = Game.of(1, player1);
    Game received = orchestrator.registerPlayer(player1);
    assertEquals(expected, received);
  }

  @Test
  @DisplayName("Register player should return a complete game with two players when called the first time")
  public void registerPlayerCalledTwice() {
    GameOrchestrator orchestrator = new GameOrchestrator();
    Player player1 = Player.of("John");
    Player player2 = Player.of("Jess");
    Game expected = Game.of(1, player1, player2);
    Game game = orchestrator.registerPlayer(player1);
    game = orchestrator.registerPlayer(player2);
    assertEquals(expected, game);
  }
}
