package com.javachess;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.Optional;

import com.javachess.utils.F;

public class Pawn {

  private Pawn() {
  }

  private static int computeYOffset(String y1, String y2) {
    return F.pipe(
      (Entry<String, String> m) -> new SimpleEntry<>(Position.yAsInt(m.getKey()), Position.yAsInt(m.getValue())),
      (Entry<Integer, Integer> m) -> m.getKey() - m.getValue()
    ).apply(new SimpleEntry<>(y1, y2));
  }

  // TODO: Make generic and pass Position::xAsInt or Position::yAsInt accordingly
  private static int computeXOffset(String y1, String y2) {
    return F.pipe(
      (Entry<String, String> m) -> new SimpleEntry<>(Position.xAsInt(m.getKey()), Position.xAsInt(m.getValue())),
      (Entry<Integer, Integer> m) -> m.getKey() - m.getValue()
    ).apply(new SimpleEntry<>(y1, y2));
  }

  private static boolean isValidDiagonalMove(Optional<Piece> target, Piece piece) {
    return F.pipe(
      Optional<Piece>::get,
      F.ifElse(
        p -> p.getColor() == piece.getColor(),
        __ -> false,
        F.pipe(
          p -> new SimpleEntry<Integer, Integer>(
            computeXOffset(Piece.getX(p), Piece.getX(piece)),
            computeYOffset(Piece.getY(p), Piece.getY(piece))
          ),
          o -> Math.abs(o.getKey()) == 1 && o.getValue() == 1
        )
      )
    ).apply(target);
  }

  private static boolean isValidStraightMove(String toX, String toY, Piece piece) {
    return F.pipe(
      (Entry<String, String> to) -> new SimpleEntry<Integer, Integer>(
        computeXOffset(to.getKey(), Piece.getX(piece)),
        computeYOffset(to.getValue(), Piece.getY(piece))
      ),
      F.ifElse(
        __ -> piece.getColor() == Color.WHITE,
        offsets -> offsets.getKey() == 0 && offsets.getValue() == 1
          || offsets.getKey() == 0 && offsets.getValue() == 2 && Piece.getY(piece).equals("2"),
        offsets -> offsets.getKey() == 0 && offsets.getValue() == -1
          || offsets.getKey() == 0 && offsets.getValue() == -2 && Piece.getY(piece).equals("7")
      )
    ).apply(new SimpleEntry<String, String>(toX, toY));
  }

  public static boolean canMoveTo(String x, String y, Board board, Piece p) {
    return F.pipe(
      Board.getPieceAt(x, y),
      F.ifElse(
        Optional<Piece>::isPresent,
        target -> Pawn.isValidDiagonalMove(target, p), // Check if position is in diagonal and is enemy ( Feind )
        __ -> Pawn.isValidStraightMove(x, y, p)
      )
    ).apply(board);
  }
}
