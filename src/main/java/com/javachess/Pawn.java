package com.javachess;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.Optional;

import com.javachess.utils.F;

public class Pawn extends Piece {
  private Pawn(String x, String y, Color c) {
    this.x = x;
    this.y = y;
    this.color = c;
  }

  public static Pawn of(String x, String y, Color c) {
    return new Pawn(x, y, c);
  }

  public static int computeYOffset(String y1, String y2) {
    return F.pipe(
      (Entry<String, String> m) -> new SimpleEntry<>(Position.yAsInt(m.getKey()), Position.yAsInt(m.getValue())),
      (Entry<Integer, Integer> m) -> m.getKey() - m.getValue()
    ).apply(new SimpleEntry<>(y1, y2));
  }

  // TODO: Make generic and pass Position::xAsInt or Position::yAsInt accordingly
  public static int computeXOffset(String y1, String y2) {
    return F.pipe(
      (Entry<String, String> m) -> new SimpleEntry<>(Position.xAsInt(m.getKey()), Position.xAsInt(m.getValue())),
      (Entry<Integer, Integer> m) -> m.getKey() - m.getValue()
    ).apply(new SimpleEntry<>(y1, y2));
  }

  public Pawn moveTo(String x, String y) {
    return Pawn.of(x, y, this.getColor());
  }

  private boolean isValidDiagonalMove(Optional<Piece> target) {
    return F.pipe(
      Optional<Piece>::get,
      F.ifElse(
        p -> p.getColor() == this.getColor(),
        __ -> false,
        F.pipe(
          p -> new SimpleEntry<Integer, Integer>(
            computeXOffset(Piece.getX(p), Piece.getX(this)),
            computeYOffset(Piece.getY(p), Piece.getY(this))
          ),
          o -> Math.abs(o.getKey()) == 1 && o.getValue() == 1
        )
      )
    ).apply(target);
  }

  private boolean isValidStraightMove(String toX, String toY) {
    return F.pipe(
      (Entry<String, String> to) -> new SimpleEntry<Integer, Integer>(
        computeXOffset(to.getKey(), Piece.getX(this)),
        computeYOffset(to.getValue(), Piece.getY(this))
      ),
      F.ifElse(
        __ -> this.getColor() == Color.WHITE,
        offsets -> offsets.getKey() == 0 && offsets.getValue() == 1
          || offsets.getKey() == 0 && offsets.getValue() == 2 && Piece.getY(this).equals("2"),
        offsets -> offsets.getKey() == 0 && offsets.getValue() == -1
          || offsets.getKey() == 0 && offsets.getValue() == -2 && Piece.getY(this).equals("7")
      )
    ).apply(new SimpleEntry<String, String>(toX, toY));
  }

  @Override
  public boolean canMoveTo(String x, String y, Board board) {
    return F.pipe(
      Board.getPieceAt(x, y),
      F.ifElse(
        Optional<Piece>::isPresent,
        this::isValidDiagonalMove, // Check if position is in diagonal and is enemy ( Feind )
        __ -> this.isValidStraightMove(x, y)
      )
    ).apply(board);
  }

  @Override
  public boolean equals(Object o) {
    return super.equals(o) && o instanceof Pawn;
  }
}
