package com.javachess.logic;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import com.javachess.util.fp.F;

public class Pawn {

  private Pawn() {
  }

  private static boolean isValidDiagonalMove(Optional<Piece> target, Piece piece) {
    return F.pipe(
      Optional<Piece>::get,
      F.ifElse(
        p -> p.getColor() == piece.getColor(),
        __ -> false,
        F.pipe(
          p -> new SimpleEntry<>(
            Position.computeXOffset(p.getX(), piece.getX()),
            Position.computeYOffset(p.getY(), piece.getY())
          ),
          F.ifElse(
            __ -> piece.getColor() == Color.WHITE,
            o -> Math.abs(o.getKey()) == 1 && o.getValue() == 1,
            o -> Math.abs(o.getKey()) == 1 && o.getValue() == -1
          )
        )
      )
    ).apply(target);
  }

  private static boolean resolveStraightWhiteMove(Entry<Integer, Integer> offsets, boolean inBetween, Piece piece) {
    return !inBetween && (offsets.getKey() == 0 && offsets.getValue() == 1
      || offsets.getKey() == 0 && offsets.getValue() == 2 && piece.getY().equals("2"));
  }

  private static boolean isValidStraightMove(String toX, String toY, List<Piece> pieces, Piece piece) {
    return F.pipe(
      (Entry<String, String> to) -> new SimpleEntry<Integer, Integer>(
        Position.computeXOffset(to.getKey(), piece.getX()),
        Position.computeYOffset(to.getValue(), piece.getY())
      ),
      F.ifElse(
        __ -> piece.getColor() == Color.WHITE,
        offsets -> F.pipe(
          Game.getPieceAt(piece.getX(), Position.yFromInt(Position.yAsInt(piece.getY()) + 1)),
          Optional::isPresent,
          inBetween -> resolveStraightWhiteMove(offsets, inBetween, piece)
        ).apply(pieces),
        offsets -> offsets.getKey() == 0 && offsets.getValue() == -1
          || offsets.getKey() == 0 && offsets.getValue() == -2 && piece.getY().equals("7")
      )
    ).apply(new SimpleEntry<String, String>(toX, toY));
  }

  public static boolean canMoveTo(String x, String y, List<Piece> pieces, Piece p) {
    return F.pipe(
      Game.getPieceAt(x, y),
      F.ifElse(
        Optional::isPresent,
        target -> Pawn.isValidDiagonalMove(target, p), // Check if position is in diagonal and is enemy ( Feind )
        __ -> Pawn.isValidStraightMove(x, y, pieces, p)
      )
    ).apply(pieces);
  }
}
