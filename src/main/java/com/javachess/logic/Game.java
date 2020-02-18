package com.javachess.logic;

import com.javachess.util.fp.Curry;
import com.javachess.util.fp.F;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Game {

  private int id;
  private Player player1;
  private Player player2;
  private List<Move> moves;

  private static String[] COLUMNS = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};
  private static String[] ROWS = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};
  private static Position[] ALL_POSITIONS = Game.buildAllPositions(COLUMNS, ROWS);

  /*********************************************************************************************************************
   * START: Constructors
   ********************************************************************************************************************/

  private Game(int id) {
    this.id = id;
    this.moves = new ArrayList<>();
  }

  private Game(int id, Player player1) {
    this.id = id;
    this.player1 = player1;
    this.moves = new ArrayList<>();
  }

  private Game(int id, Player player1, Player player2) {
    this.id = id;
    this.player1 = player1;
    this.player2 = player2;
    this.moves = new ArrayList<>();
  }

  private Game(int id, Player player1, Player player2, List<Move> moves) {
    this.id = id;
    this.player1 = player1;
    this.player2 = player2;
    this.moves = moves;
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

  public static Game of(int id, Player player1, Player player2, List<Move> moves) {
    return new Game(id, player1, player2, moves);
  }

  /*********************************************************************************************************************
   * END: Constructors
   ********************************************************************************************************************/

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

  public static Game addPlayer(Player player, Game game) {
    if (game.getPlayer1() == null) {
      Player colorized = Player.of(player.getId(), Color.getRandomColor());
      return Game.of(game.getId(), colorized);
    }
    else {
      Player colorized = Player.of(
        player.getId(),
        Color.getComplementColor(game.getPlayer1().getColor())
      );

      return Game.of(game.getId(), game.getPlayer1(), colorized);
    }
  }

  @Curry
  public static Function<Game, Game> addPlayer(Player player) {
    return g -> Game.addPlayer(player, g);
  }

  // Should be tested
  public static List<Piece> getPieces(Game g) {
    return F.reduce(Game::applyMove, Game.getInitialPieces(), g.getMoves().stream());
  }

  private static List<Piece> applyMove(List<Piece> pieces, Move move) {
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

  public static Position[] getPossibleMoves(String x, String y, List<Piece> pieces) {
    return F.pipe(
      Game.getPieceAt(x, y),
      F.ifElse(
        Optional::isPresent,
        F.pipe(
          Optional::get,
          piece -> F.filter((Position p) ->
            Piece.canMoveTo(p.getX(), p.getY(), pieces, piece)
          ).apply(Arrays.stream(ALL_POSITIONS))
        ),
        __ -> Stream.empty()
      ),
      s -> s.toArray(Position[]::new)
    ).apply(pieces);
  }

  public static Optional<Piece> getPieceAt(String x, String y, List<Piece> pieces) {
    return F.find(p -> p.getX().equals(x) && p.getY().equals(y), pieces.stream());
  }

  @Curry
  public static Function<List<Piece>, Optional<Piece>> getPieceAt(final String x, final String y) {
    return p -> getPieceAt(x, y, p);
  }

  public static Optional<Piece> getPieceAt(String x, String y, Game g) {
    return F.find(p -> p.getX().equals(x) && p.getY().equals(y), Game.getPieces(g).stream());
  }

  public static Game doMove(Move move, Game g) {
    return F.pipe(
      (Stream<Move> s) -> F.concat(s, Stream.of(move)),
      m -> m.collect(Collectors.toList()),
      m -> Game.of(g.getId(), g.getPlayer1(), g.getPlayer2(), m)
    ).apply(g.getMoves().stream());
  }

  public static Game doMoveIfPossible(Move move, Game g) {
    Player activePlayer = Game.getActivePlayer(g);
    Optional<Piece> pieceToMove = Game.getPieceAt(move.getFrom().getX(), move.getFrom().getY(), g);

    return pieceToMove.isPresent()
      && pieceToMove.get().getColor() == activePlayer.getColor()
      && Piece.canMoveTo(move.getTo().getX(), move.getTo().getY(), Game.getPieces(g), pieceToMove.get())
        ? doMove(move, g)
        : g;
  }

  public static Player getActivePlayer(Game g) {
    return F.ifElse(
      Game::isWhitePlayerActive,
      Game::getWhitePlayer,
      Game::getBlackPlayer
    ).apply(g);
  }

  public static boolean isWhitePlayerActive(Game g) {
    return g.getMoves().size() % 2 == 0;
  }

  public static boolean isComplete(Game g) {
    return g.getPlayer1() != null && g.getPlayer2() != null;
  }

  /*********************************************************************************************************************
   * START: Getters
   ********************************************************************************************************************/

  public int getId() {
    return this.id;
  }

  public Player getPlayer1() {
    return this.player1;
  }

  public Player getPlayer2() {
    return this.player2;
  }

  public List<Move> getMoves() {
    return this.moves;
  }

  /*********************************************************************************************************************
   * END: Getters
   ********************************************************************************************************************/

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

  /*********************************************************************************************************************
   * START: Board generation part
   ********************************************************************************************************************/

  private static Position[] buildAllPositions(String[] cols, String[] rows) {
    return F.pipe(
      F.map(
        (String col) -> F.map(row -> Position.of(col, row), Arrays.stream(rows))
      ),
      x -> x.flatMap(a -> a),
      s -> s.toArray(Position[]::new)
    ).apply(Arrays.stream(cols));
  }

  public static List<Piece> getInitialPieces() {
    return F.pipe(
      F.concat(generatePawns()),
      F.concat(generateRooks()),
      F.concat(generateBishops()),
      F.concat(generateKnights()),
      F.concat(generateQueens()),
      F.concat(generateKings()),
      s -> s.collect(Collectors.toList())
    ).apply(Stream.of());
  }

  private static Function<Stream<String>, Stream<Piece>> colsToPawns(final Color c) {
    return F.map(
      x -> c == Color.WHITE
        ? Piece.of(x, "2", Color.WHITE, PieceType.PAWN)
        : Piece.of(x, "7", Color.BLACK, PieceType.PAWN)
    );
  }

  private static Stream<Piece> generatePawns() {
    return F.concat(
      colsToPawns(Color.WHITE).apply(Arrays.stream(COLUMNS)),
      colsToPawns(Color.BLACK).apply(Arrays.stream(COLUMNS))
    );
  }

  private static Stream<Piece> generateRooks() {
    return Stream.of(
      Piece.of("a", "1", Color.WHITE, PieceType.ROOK),
      Piece.of("h", "1", Color.WHITE, PieceType.ROOK),
      Piece.of("a", "8", Color.BLACK, PieceType.ROOK),
      Piece.of("h", "8", Color.BLACK, PieceType.ROOK)
    );
  }

  private static Stream<Piece> generateBishops() {
    return Stream.of(
      Piece.of("c", "1", Color.WHITE, PieceType.BISHOP),
      Piece.of("f", "1", Color.WHITE, PieceType.BISHOP),
      Piece.of("c", "8", Color.BLACK, PieceType.BISHOP),
      Piece.of("f", "8", Color.BLACK, PieceType.BISHOP)
    );
  }

  private static Stream<Piece> generateKnights() {
    return Stream.of(
      Piece.of("b", "1", Color.WHITE, PieceType.KNIGHT),
      Piece.of("g", "1", Color.WHITE, PieceType.KNIGHT),
      Piece.of("b", "8", Color.BLACK, PieceType.KNIGHT),
      Piece.of("g", "8", Color.BLACK, PieceType.KNIGHT)
    );
  }

  private static Stream<Piece> generateQueens() {
    return Stream.of(
      Piece.of("d", "1", Color.WHITE, PieceType.QUEEN),
      Piece.of("d", "8", Color.BLACK, PieceType.QUEEN)
    );
  }

  private static Stream<Piece> generateKings() {
    return Stream.of(
      Piece.of("e", "1", Color.WHITE, PieceType.KING),
      Piece.of("e", "8", Color.BLACK, PieceType.KING)
    );
  }

  /*********************************************************************************************************************
   * END: Board generation part
   ********************************************************************************************************************/
}
