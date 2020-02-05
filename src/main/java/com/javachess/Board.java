package com.javachess;

import com.javachess.utils.Curry;
import com.javachess.utils.F;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class Board {
  private final Piece[] pieces;

  private static Function<Stream<String>, Stream<Piece>> colsToPieces(final Color c) {
    return F.map(
      x -> c == Color.WHITE
        ? Pawn.of(x, "2", Color.WHITE)
        : Pawn.of(x, "7", Color.BLACK)
    );
  }

  private static Stream<String> getCols() {
    return Stream.of("a", "b", "c", "d", "e", "f", "g", "h");
  }

  private static Stream<Piece> generatePawns() {
    return F.concat(
      colsToPieces(Color.WHITE).apply(getCols()),
      colsToPieces(Color.BLACK).apply(getCols())
    );
  }

  private static Stream<Piece> generateRooks() {
    return Stream.of(
      Rook.of("a", "1", Color.WHITE),
      Rook.of("h", "1", Color.WHITE),
      Rook.of("a", "8", Color.BLACK),
      Rook.of("h", "8", Color.BLACK)
    );
  }

  private static Stream<Piece> generateBishops() {
    return Stream.of(
      Bishop.of("b", "1", Color.WHITE),
      Bishop.of("g", "1", Color.WHITE),
      Bishop.of("g", "8", Color.BLACK),
      Bishop.of("b", "8", Color.BLACK)
    );
  }

  private static Stream<Piece> generateKnights() {
    return Stream.of(
      Knight.of("c", "1", Color.WHITE),
      Knight.of("f", "1", Color.WHITE),
      Knight.of("c", "8", Color.BLACK),
      Knight.of("f", "8", Color.BLACK)
    );
  }

  private static Stream<Piece> generateQueens() {
    return Stream.of(Queen.of("d", "1", Color.WHITE), Queen.of("d", "8", Color.BLACK));
  }

  private static Stream<Piece> generateKings() {
    return Stream.of(King.of("e", "1", Color.WHITE), King.of("e", "8", Color.BLACK));
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

  public static Piece movePieceAtPosition(String x, String y, Piece p) {
    if (p instanceof Pawn) {
      return Pawn.of(x, y, p.getColor());
    }
    return null;
  }

  public static Board executeMove(String fromX, String fromY, String toX, String toY, Board b) {
    return F.pipe(
      Board.getPieceAt(fromX, fromY),
      F.ifElse(
        Optional<Piece>::isPresent,
        F.pipe(
          Optional<Piece>::get,
          piece -> F.pipe(
            p -> F.reject(x -> x.equals(p), b.getPieces()),
            F.concat(Stream.of(movePieceAtPosition(toX, toY, piece)))
          ).apply(piece),
          pieces -> new Board(pieces.toArray(Piece[]::new))
        ),
        __ -> b // We return the initial board if no piece was found
      )
    ).apply(b);
  }

  public static Optional<Piece> getPieceAt(final String x, final String y, final Board b) {
    return F.find(p -> Piece.getX(p) == x && Piece.getY(p) == y, b.getPieces());
  }

  @Curry
  public static Function<Board, Optional<Piece>> getPieceAt(final String x, final String y) {
    return b -> getPieceAt(x, y, b);
  }

  public static Board create() {
    return new Board();
  }
}
