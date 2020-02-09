package com.javachess.logic;

import java.util.ArrayList;
import java.util.List;

public class GameOrchestrator {

  private List<Game> games;

  private int latestGameId = 0;

  public GameOrchestrator() {
    this.games = new ArrayList<>();
  }

  /**
   * TODO: Should we verify that the user is in a game already first ?
   * @param player
   * @return
   */
  public Game registerPlayer(Player player) {
    Game lastGame = games.size() > 0 ? games.get(games.size() - 1) : null;

    if (lastGame == null || lastGame.isComplete()) {
      latestGameId = latestGameId + 1;
      Game newGame = Game.of(latestGameId, player);
      games.add(newGame);
      return newGame;
    }

    Game newGame = Game.addPlayer(player, lastGame);
    games.remove(lastGame);
    games.add(newGame);
    return newGame;
  }

}
