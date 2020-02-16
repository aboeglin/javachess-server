package com.javachess.logic;

import com.javachess.util.fp.Curry;
import com.javachess.util.fp.F;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Game {
  private int id;
  private Player player1;
  private Player player2;
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

  private static Player getBlackPlayer(Game g) {
    return g.getPlayer1().getColor() == Color.BLACK
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
        fullGame.getBoard()
      );
    }
  }

  @Curry
  public static Function<Game, Game> addPlayer(Player player) {
    return g -> Game.addPlayer(player, g);
  }

  // Should be tested
  public static List<Piece> getPieces(Game g) {
    List<Piece> pieces = Board.getInitialPieces();
    for (Move m : g.getBoard().getMoves()) {
      pieces = Game.applyMove(m, pieces);
    }
    return pieces;
  }

  private static List<Piece> applyMove(Move move, List<Piece> pieces) {
    return F.pipe(
      (Move m) -> Game.getPieceAt(m.getFrom().getX(), m.getFrom().getY(), pieces),
      F.ifElse(
        Optional::isPresent,
        F.pipe(
          Optional::get,
          (Piece piece) -> F.pipe(
            (Stream<Piece> p) -> Game.getPieceAt(move.getTo().getX(), move.getTo().getY(), p.collect(Collectors.toList())),
            p -> F.reject(x -> p.isPresent() ? p.get().equals(x) : false, pieces.stream()),
            F.replace(
              x -> x.equals(piece),
              Piece.moveTo(move.getTo().getX(), move.getTo().getY(), piece)
            )
          ).apply(pieces.stream()),
          s -> s.collect(Collectors.toList())
        ),
        __ -> pieces // We return the initial Pieces if no piece was found
      )
    ).apply(move);
  }

  public static Optional<Piece> getPieceAt(String x, String y, List<Piece> pieces) {
    return F.find(p -> Piece.getX(p).equals(x) && Piece.getY(p).equals(y), pieces.stream());
  }

  @Curry
  public static Function<List<Piece>, Optional<Piece>> getPieceAt(final String x, final String y) {
    return p -> getPieceAt(x, y, p);
  }

  public static Optional<Piece> getPieceAt(String x, String y, Game g) {
    return F.find(p -> Piece.getX(p).equals(x) && Piece.getY(p).equals(y), Game.getPieces(g).stream());
  }

  // Move to Game such that :
  // public static Game doMove(Move move, Game g)
  public static Game doMove(Move move, Game g) {
    return F.pipe(
      (Stream<Move> s) -> F.concat(s, Stream.of(move)),
      m -> m.collect(Collectors.toList()),
      Board::of,
      b -> Game.of(g.getId(), g.getPlayer1(), g.getPlayer2(), b)
    ).apply(g.getBoard().getMoves().stream());
  }

//  public static Board doMoveIfPossible(Move move, Board b) {
//    if (POSSIBLE) {
//      return doMove(move, b);
//    }
//    return b;
//  }

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
    return this.getBoard().getMoves().size() % 2 == 0
      ? Game.getWhitePlayer(this)
      : Game.getBlackPlayer(this);
  }

  public Board getBoard() {
    return board;
  }

  public static boolean isComplete(Game g) {
    return g.getPlayer1() != null && g.getPlayer2() != null;
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
