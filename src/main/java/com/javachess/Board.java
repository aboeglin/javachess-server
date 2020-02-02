package com.javachess;

import com.javachess.utils.FP;
import java.util.function.Function;
import java.util.stream.Stream;

public class Board {
  private Piece[] pieces;

  private static Piece[] generatePawns() {
    Function<Stream<String>,Stream<Piece>> colsToPieces = cols -> cols.map(
      FP.ifElse(
        __ -> Boolean.TRUE,
        c -> new Piece(c, "1", Color.WHITE, PieceType.PAWN),
        c -> new Piece(c, "8", Color.BLACK, PieceType.PAWN)
      )
    );
    return FP.pipe(colsToPieces, x -> x)
      .apply(Stream.of("a", "b", "c", "d", "e", "f", "g", "h"))
      .toArray(Piece[]::new);
  }

  public Board() {
    this.pieces = generatePawns();
    System.out.println(this.pieces.length);
  }
}
