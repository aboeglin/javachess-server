package com.javachess;

import com.javachess.util.fp.F;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Position {
  private final static Map<String, Integer> getXPositionsMap() {
    final Map<String, Integer> m = new HashMap<String, Integer>(8);
    m.put("a", 1);
    m.put("b", 2);
    m.put("c", 3);
    m.put("d", 4);
    m.put("e", 5);
    m.put("f", 6);
    m.put("g", 7);
    m.put("h", 8);
    return m;
  }

  public static int computeYOffset(String y1, String y2) {
    return computeOffset(y1, y2, Position::yAsInt);
  }

  public static int computeXOffset(String x1, String x2) {
    return computeOffset(x1, x2, Position::xAsInt);
  }

  private static Function<Map.Entry<String, String>, Map.Entry<Integer, Integer>> mapTupleToIntSpace(Function<String, Integer> map) {
    return tuple -> new HashMap.SimpleEntry<>(map.apply(tuple.getKey()), map.apply(tuple.getValue()));
  }

  private static int computeTupleDiff(Map.Entry<Integer, Integer> tuple) {
    return tuple.getKey() - tuple.getValue();
  }

  private static int computeOffset(String a1, String a2, Function<String, Integer> asIntFn) {
    return F.pipe(
      mapTupleToIntSpace(asIntFn),
      Position::computeTupleDiff
    ).apply(new HashMap.SimpleEntry<>(a1, a2));
  }

  public static int xAsInt(String x) {
    return Position.getXPositionsMap().get(x);
  }

  public static int yAsInt(String y) {
    return Integer.parseInt(y);
  }
}
