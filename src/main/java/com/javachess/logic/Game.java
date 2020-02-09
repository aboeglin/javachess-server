package com.javachess.logic;

public class Game {
  private int id;
  private Player player1;
  private Player player2;

  private Game(int id) {
    this.id = id;
  }

  private Game(int id, Player player1) {
    this.id = id;
    this.player1 = player1;
  }

  private Game(int id, Player player1, Player player2) {
    this.id = id;
    this.player1 = player1;
    this.player2 = player2;
  }

  public static Game of(int id) {
    return new Game(id);
  }

  public static Game of(int id, Player player1) {
    return new Game(id, player1);
  }

  public static Game of(int id, Player player1, Player player2) {
    return new Game(id, player1, player2);
  }

  public static Game addPlayer(Player player, Game game) {
    return game.getPlayer1() == null
      ? Game.of(game.getId(), player)
      : Game.of(game.getId(), game.getPlayer1(), player);
  }

  public int getId() {
    return this.id;
  }

  public Player getPlayer1() {
    return this.player1;
  }

  public Player getPlayer2() {
    return this.player2;
  }

  public boolean isComplete() {
    return this.player1 != null && this.player2 != null;
  }

  public boolean equals(Object o) {
    if (o instanceof Game) {
      Game game = (Game) o;
      return game.getId() == this.getId()
        && (
        game.getPlayer1() != null && this.getPlayer1() != null
          && game.getPlayer1().equals(this.getPlayer1())
          || game.getPlayer1() == null && this.getPlayer1() == null
      )
        && (
        game.getPlayer2() != null && this.getPlayer2() != null
          && game.getPlayer2().equals(this.getPlayer2())
          || game.getPlayer2() == null && this.getPlayer2() == null
      );
    }
    return false;
  }
}
