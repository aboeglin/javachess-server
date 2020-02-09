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

  @Test
  @DisplayName("addPlayer should add the first and then the second player when called twice")
  public void addPlayerTwoTimes() {
    Game game = Game.of(1);
    game = Game.addPlayer(Player.of("John"), game);
    game = Game.addPlayer(Player.of("Jess"), game);
    assertEquals(Player.of("John"), game.getPlayer1());
    assertEquals(Player.of("Jess"), game.getPlayer2());
  }

  @Test
  @DisplayName("equals should return true if the two games have the same id and same players")
  public void equals() {
    Game game1 = Game.of(1, Player.of("John"), Player.of("Jess"));
    Game game2 = Game.of(1, Player.of("John"), Player.of("Jess"));
    assertEquals(true, game1.equals(game2));
  }

  @Test
  @DisplayName("equals should return true even with empty players")
  public void equalsWithNullPlayer() {
    Game game1 = Game.of(1, null, Player.of("Jess"));
    Game game2 = Game.of(1, null, Player.of("Jess"));
    assertEquals(true, game1.equals(game2));

    Game game3 = Game.of(1, Player.of("John"), null);
    Game game4 = Game.of(1, Player.of("John"), null);
    assertEquals(true, game3.equals(game4));
  }
}
