package com.javachess.server;

import com.javachess.logic.Game;
import com.javachess.logic.Player;
import com.javachess.util.fp.F;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameOrchestrator {

  private List<Game> games;

  private List<Player> joinedPlayers;

  private int latestGameId = 0;

  public GameOrchestrator() {
    this.games = new ArrayList<>();
    this.joinedPlayers = new ArrayList<>();
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

  public void join(Player player) {
    this.joinedPlayers.add(player);
  }

  public Game findGameByPlayer(Player p) {
    return F.pipe(
      (List<Game> l) -> l.stream(),
      F.find(g -> p.equals(g.getPlayer1()) || p.equals(g.getPlayer2())),
      o -> o.orElse(null)
    ).apply(this.games);
  }

  public Game findGameById(int id) {
    return F.pipe(
      (List<Game> l) -> l.stream(),
      F.find(g -> g.getId() == id),
      o -> o.orElse(null)
    ).apply(this.games);
  }

  public boolean isGameReady(Game g) {
    return this.joinedPlayers.contains(g.getPlayer1()) && this.joinedPlayers.contains(g.getPlayer2());
  }

}
