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

public class Board {
  private final List<Move> moves;

  private static String[] COLUMNS = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};
  private static String[] ROWS = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};

  private static Position[] ALL_POSITIONS = Board.buildAllPositions(COLUMNS, ROWS);

  private Board() {
    this.moves = new ArrayList<>();
  }

  private Board(List<Move> moves) {
    this.moves = moves;
  }

  public static Board create() {
    return new Board();
  }

  public static Board of() {
    return new Board();
  }

  public static Board of(List<Move> moves) {
    return new Board(moves);
  }

  public List<Move> getMoves() {
    return this.moves;
  }

  private static Position[] buildAllPositions(String[] cols, String[] rows) {
    return F.pipe(
      F.map(
        (String col) -> F.map(row -> Position.of(col, row), Arrays.stream(rows))
      ),
      x -> x.flatMap(a -> a),
      s -> s.toArray(Position[]::new)
    ).apply(Arrays.stream(cols));
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


  /*********************************************************************************************************************
   *
   * START: Board generation part
   *
   ********************************************************************************************************************/

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
   *
   * END: Board generation part
   *
   ********************************************************************************************************************/
}
