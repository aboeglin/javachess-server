package com.javachess.logic;

import com.javachess.util.fp.Curry;

import java.util.function.Function;

public class Game {
  private int id;
  private Player player1;
  private Player player2;
  private Player activePlayer;
  private Board board;

  private static final Board INITIAL_BOARD = Board.create();

  private Game(int id) {
    this.id = id;
    this.board = INITIAL_BOARD;
  }

  private Game(int id, Player player1) {
    this.id = id;
    this.player1 = player1;
    this.board = INITIAL_BOARD;
  }

  private Game(int id, Player player1, Player player2) {
    this.id = id;
    this.player1 = player1;
    this.player2 = player2;
    this.board = INITIAL_BOARD;
  }

  private Game(int id, Player player1, Player player2, Board board) {
    this.id = id;
    this.player1 = player1;
    this.player2 = player2;
    this.board = board;
  }

  private Game(int id, Player player1, Player player2, Board board, Player activePlayer) {
    this.id = id;
    this.player1 = player1;
    this.player2 = player2;
    this.board = board;
    this.activePlayer = activePlayer;
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

  public static Game of(int id, Player player1, Player player2, Board board) {
    return new Game(id, player1, player2, board);
  }

  public static Game of(int id, Player player1, Player player2, Board board, Player activePlayer) {
    return new Game(id, player1, player2, board, activePlayer);
  }

  private static Color getRandomColor() {
    return Color.WHITE;
  }

  private static Color getComplementColor(Color c) {
    return c == Color.WHITE ? Color.BLACK : Color.WHITE;
  }

  private static Player getWhitePlayer(Game g) {
    return g.getPlayer1().getColor() == Color.WHITE
      ? g.getPlayer1()
      : g.getPlayer2();
  }

  public static Player getOpponent(Player p, Game g) {
    return g.getPlayer2().equals(p)
      ? g.getPlayer1()
      : g.getPlayer2();
  }

  public static Game addPlayer(Player player, Game game) {
    if (game.getPlayer1() == null) {
      Player colorized = Player.of(player.getId(), getRandomColor());
      return Game.of(game.getId(), colorized);
    }
    else {
      Player colorized = Player.of(
        player.getId(),
        getComplementColor(game.getPlayer1().getColor())
      );

      Game fullGame = Game.of(game.getId(), game.getPlayer1(), colorized);

      return Game.of(
        fullGame.getId(),
        fullGame.getPlayer1(),
        fullGame.getPlayer2(),
        fullGame.getBoard(),
        getWhitePlayer(fullGame)
      );
    }
  }

  @Curry
  public static Function<Game, Game> addPlayer(Player player) {
    return g -> Game.addPlayer(player, g);
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

  public Player getActivePlayer() {
    return this.activePlayer;
  }

  public Board getBoard() {
    return board;
  }

  // public static boolean isComplete(Game g) ?
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
