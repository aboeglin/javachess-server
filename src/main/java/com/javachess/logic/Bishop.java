package com.javachess.logic;

import com.javachess.util.fp.F;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Bishop {

  private Bishop() {
  }

  public static List<Position> computeTrajectory(Position from, Position to) {
    Integer distance = Math.abs(to.getYAsInt() - from.getYAsInt());
    Stream<Integer> range = IntStream.range(1, distance).boxed();

    Direction xDirection = to.getXAsInt() - from.getXAsInt() > 0
      ? Direction.RIGHT
      : Direction.LEFT;

    Direction yDirection = to.getYAsInt() - from.getYAsInt() > 0
      ? Direction.TOP
      : Direction.BOTTOM;

    return F.pipe(
      F.map((Integer x) -> Position.of(
        xDirection == Direction.LEFT ? from.getXAsInt() - x : from.getXAsInt() + x,
        yDirection == Direction.BOTTOM ? from.getYAsInt() - x : from.getYAsInt() + x
      )),
      s -> s.collect(Collectors.toList())
    ).apply(range);
  }

  private static int delta(int from, int to) {
    return Math.abs(from - to);
  }

  public static boolean canMoveTo(String x, String y, List<Piece> pieces, Piece piece) {
    Position from = Position.of(piece.getX(), piece.getY());
    Position to = Position.of(x, y);
    int deltaX = delta(to.getXAsInt(), from.getXAsInt());
    int deltaY = delta(to.getYAsInt(), from.getYAsInt());

    if (deltaX == deltaY) {
      List<Position> squaresInBetween = Bishop.computeTrajectory(from, to);

      return !F.reduce(
        (Boolean inBetween, Position pos) ->
          Game.getPieceAt(pos.getX(), pos.getY(), pieces).isPresent() || inBetween,
        false,
        squaresInBetween.stream()
      );
    }
    return false;
  }
}
