package com.javachess.logic;

import com.javachess.server.GameOrchestrator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

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

  @Test
  @DisplayName("findGameByPlayer player should return a game when that player was already assigned one")
  public void findGameByPlayer() {
    GameOrchestrator orchestrator = new GameOrchestrator();
    Player player1 = Player.of("John");
    Game expected = orchestrator.registerPlayer(player1);
    Game actual = orchestrator.findGameByPlayer(player1);
    assertSame(expected, actual);
  }

  @Test
  @DisplayName("findGameByPlayer player should return null when that player is in no current game")
  public void findGameByPlayerNotFound() {
    GameOrchestrator orchestrator = new GameOrchestrator();
    Player player1 = Player.of("John");
    Player player2 = Player.of("Jess");
    Game expected = null;
    Game actual = orchestrator.findGameByPlayer(player2);
    assertEquals(expected, actual);
  }
}
