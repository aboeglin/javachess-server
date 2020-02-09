package com.javachess.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameTests {
  @Test
  @DisplayName("When creating a new game without player isComplete should be false")
  public void initialGameNotComplete() {
    Game game = Game.of(1);
    assertEquals(false, game.isComplete());
  }

  @Test
  @DisplayName("When creating a new game with 2 players isComplete should be true")
  public void initialGameComplete() {
    Game game = Game.of(1, Player.of("John"), Player.of("Jess"));
    assertEquals(true, game.isComplete());
  }

  @Test
  @DisplayName("addPlayer should set the first player on an empty game")
  public void addPlayerOnEmptyGame() {
    Game game = Game.of(1);
    game = Game.addPlayer(Player.of("John"), game);
    assertEquals(Player.of("John"), game.getPlayer1());
  }
}
