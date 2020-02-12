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
  private final Piece[] pieces;

  private static Function<Stream<String>, Stream<Piece>> colsToPawns(final Color c) {
    return F.map(
      x -> c == Color.WHITE
        ? Piece.of(x, "2", Color.WHITE, PieceType.PAWN)
        : Piece.of(x, "7", Color.BLACK, PieceType.PAWN)
    );
  }

  private static String[] COLUMNS = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};
  private static String[] ROWS = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};

  private static Position[] ALL_POSITIONS = Board.buildAllPositions(COLUMNS, ROWS);

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

  private Board() {
    this.pieces = F.pipe(
      F.concat(generatePawns()),
      F.concat(generateRooks()),
      F.concat(generateBishops()),
      F.concat(generateKnights()),
      F.concat(generateQueens()),
      F.concat(generateKings()),
      s -> s.toArray(Piece[]::new)
    ).apply(Stream.of());
  }

  private Board(Piece[] pieces) {
    this.pieces = pieces;
  }

  public Stream<Piece> getPieces() {
    return Arrays.stream(this.pieces);
  }

  public static Board executeMove(String fromX, String fromY, String toX, String toY, Board b) {
    return F.pipe(
      Board.getPieceAt(fromX, fromY),
      F.ifElse(
        Optional<Piece>::isPresent,
        F.pipe(
          Optional<Piece>::get,
          piece -> F.replace(x -> x.equals(piece), Piece.moveTo(toX, toY, piece)).apply(b.getPieces()),
          pieces -> new Board(pieces.toArray(Piece[]::new))
        ),
        __ -> b // We return the initial board if no piece was found
      )
    ).apply(b);
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

  public static Position[] getPossibleMoves(String x, String y, Board b) {
    return F.pipe(
      Board.getPieceAt(x, y),
      F.ifElse(
        Optional::isPresent,
        F.pipe(
          Optional::get,
          piece -> F.filter((Position p) ->
            Piece.canMoveTo(p.getX(), p.getY(), b, piece)
          ).apply(Arrays.stream(ALL_POSITIONS))
        ),
        __ -> Stream.empty()
      ),
      s -> s.toArray(Position[]::new)
    ).apply(b);
  }

  public static Optional<Piece> getPieceAt(final String x, final String y, final Board b) {
    return F.find(p -> Piece.getX(p).equals(x) && Piece.getY(p).equals(y), b.getPieces());
  }

  @Curry
  public static Function<Board, Optional<Piece>> getPieceAt(final String x, final String y) {
    return b -> getPieceAt(x, y, b);
  }

  public static Board create() {
    return new Board();
  }
}
