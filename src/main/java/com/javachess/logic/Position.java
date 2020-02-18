package com.javachess.logic;

import com.javachess.util.fp.F;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Position {

  private String x;
  private String y;

  private Position(String x, String y) {
    this.x = x;
    this.y = y;
  }

  private Position(Integer x, Integer y) {
    String[] xs = (String[]) Position.getXPositionsMap().keySet().toArray(new String[]{});
    this.x = xs[x - 1];
    this.y = y.toString();
  }

  public static Position of(String x, String y) {
    return new Position(x, y);
  }

  public static Position of(Integer x, Integer y) {
    return new Position(x, y);
  }

  public boolean equals(Object o) {
    if (o instanceof  Position) {
      Position p = (Position) o;
      return p.getX().equals(this.getX()) && p.getY().equals(this.getY());
    }
    return false;
  }

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

  private static Function<Entry<String, String>, Entry<Integer, Integer>> mapTupleToIntSpace(Function<String, Integer> map) {
    return tuple -> new SimpleEntry<>(map.apply(tuple.getKey()), map.apply(tuple.getValue()));
  }

  private static int computeTupleDiff(Entry<Integer, Integer> tuple) {
    return tuple.getKey() - tuple.getValue();
  }

  // Make it take ints now that we have Position::getXAsInt and Position::getYAsInt ?
  private static int computeOffset(String a1, String a2, Function<String, Integer> asIntFn) {
    return F.pipe(
      mapTupleToIntSpace(asIntFn),
      Position::computeTupleDiff
    ).apply(new SimpleEntry<>(a1, a2));
  }

  public static int xAsInt(String x) {
    return Position.getXPositionsMap().get(x);
  }

  public static int yAsInt(String y) {
    return Integer.parseInt(y);
  }

  public static String yFromInt(Integer y) {
    return y.toString();
  }

  public int getYAsInt() {
    return Integer.parseInt(this.getY());
  }

  public int getXAsInt() {
    return Position.getXPositionsMap().get(this.getX());
  }

  public String getX() {
    return x;
  }

  public String getY() {
    return y;
  }
}
