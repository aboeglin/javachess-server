package com.javachess.logic;

import com.javachess.util.fp.F;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Rook {

  private Rook() {
  }

  public static List<Position> computeTrajectory(Position from, Position to) {
    Integer xDiff = to.getXAsInt() - from.getXAsInt();
    Integer yDiff = to.getYAsInt() - from.getYAsInt();

    Stream<Position> positions = Stream.of();

    if (xDiff < 0 && yDiff == 0) {
      Stream<Integer> range = IntStream.range(1, -xDiff).boxed();

      positions = F.map((Integer x) -> Position.of(
        from.getXAsInt() - x,
        from.getYAsInt()
      )).apply(range);
    }
    else if (xDiff > 0 && yDiff == 0) {
      Stream<Integer> range = IntStream.range(1, xDiff).boxed();

      positions = F.map((Integer x) -> Position.of(
        from.getXAsInt() + x,
        from.getYAsInt()
      )).apply(range);
    }
    else if (xDiff == 0 && yDiff < 0) {
      Stream<Integer> range = IntStream.range(1, -yDiff).boxed();

      positions = F.map((Integer y) -> Position.of(
        from.getXAsInt(),
        from.getYAsInt() - y
      )).apply(range);
    }
    else if (xDiff == 0 && yDiff > 0) {
      Stream<Integer> range = IntStream.range(1, yDiff).boxed();

      positions = F.map((Integer y) -> Position.of(
        from.getXAsInt(),
        from.getYAsInt() + y
      )).apply(range);
    }

    return positions.collect(Collectors.toList());
  }

  private static int delta(int from, int to) {
    return Math.abs(from - to);
  }

  public static boolean canMoveTo(String x, String y, List<Piece> pieces, Piece piece) {
    Position from = Position.of(piece.getX(), piece.getY());
    Position to = Position.of(x, y);
    int deltaX = delta(to.getXAsInt(), from.getXAsInt());
    int deltaY = delta(to.getYAsInt(), from.getYAsInt());

    if (deltaX == 0 || deltaY == 0) {
      List<Position> squaresInBetween = Rook.computeTrajectory(from, to);

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
