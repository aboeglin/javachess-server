package com.javachess.server;

import com.javachess.logic.Game;
import com.javachess.logic.Player;
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
  @DisplayName("findGameByPlayer should return a game when that player was already assigned one")
  public void findGameByPlayer() {
    GameOrchestrator orchestrator = new GameOrchestrator();
    Player player1 = Player.of("John");
    Game expected = orchestrator.registerPlayer(player1);
    Game actual = orchestrator.findGameByPlayer(player1);
    assertSame(expected, actual);
  }

  @Test
  @DisplayName("findGameByPlayer should return null when that player is in no current game")
  public void findGameByPlayerNotFound() {
    GameOrchestrator orchestrator = new GameOrchestrator();
    Player player1 = Player.of("John");
    Player player2 = Player.of("Jess");
    orchestrator.registerPlayer(player1);
    Game expected = null;
    Game actual = orchestrator.findGameByPlayer(player2);
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("findGameById player should a game with matching id")
  public void findGameById() {
    GameOrchestrator orchestrator = new GameOrchestrator();
    Player player1 = Player.of("John");
    Game expected = orchestrator.registerPlayer(player1);
    Game actual = orchestrator.findGameById(expected.getId());
    assertSame(expected, actual);
  }

  @Test
  @DisplayName("isGameReady should return true if both players joined")
  public void isGameReady() {
    GameOrchestrator orchestrator = new GameOrchestrator();
    Player player1 = Player.of("John");
    Player player2 = Player.of("Jess");
    Game game = orchestrator.registerPlayer(player1);
    game = orchestrator.registerPlayer(player2);

    orchestrator.join(player1);
    orchestrator.join(player2);

    assertEquals(true, orchestrator.isGameReady(game));
  }

  @Test
  @DisplayName("createGame should return a new game")
  public void createGame() {
    GameOrchestrator orchestrator = new GameOrchestrator();
    Player player1 = Player.of("John");
    Game expected = Game.of(1, player1);
    Game received = orchestrator.createGame(player1);
    assertEquals(expected, received);
  }

}
