package com.javachess;

import java.util.HashMap;
import java.util.Map;

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

  public static int xAsInt(String x) {
    return Position.getXPositionsMap().get(x);
  }

  public static int yAsInt(String y) {
    return Integer.parseInt(y);
  }
}
