
package com.javachess.utils;

import java.util.function.Predicate;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FTests {

  @Test
  @DisplayName("map should work")
  public void map() {
    Stream<String> expected = Stream.of("a-mapped", "b-mapped");
    Stream<String> input = Stream.of("a", "b");
    Stream<String> output = F.map(s -> s + "-mapped", input);
    assertArrayEquals(expected.toArray(String[]::new), output.toArray(String[]::new));
  }

  @Test
  @DisplayName("pipe should work")
  public void pipe() {
    Integer expected = 8;
    Integer output = F.pipe((Integer x) -> x + 1, (Integer x) -> x * 2).apply(3);
    assertEquals(expected, output);
  }

  @Test
  @DisplayName("pipe should work with only one arg")
  public void pipeOne() {
    Integer expected = 4;
    Integer output = F.pipe((Integer x) -> x + 1).apply(3);
    assertEquals(expected, output);
  }

  @Test
  @DisplayName("concat should concatenate two streams")
  public void concat() {
    Stream<Integer> s1 = Stream.of(1, 2);
    Stream<Integer> s2 = Stream.of(3, 4);
    Stream<Integer> expected = Stream.of(1, 2, 3, 4);
    Stream<Integer> output = F.concat(s1, s2);
    assertArrayEquals(expected.toArray(Integer[]::new), output.toArray(Integer[]::new));
  }

  @Test
  @DisplayName("concat should be curried")
  public void concatCurried() {
    Stream<Integer> s1 = Stream.of(1, 2);
    Stream<Integer> s2 = Stream.of(3, 4);
    Stream<Integer> expected = Stream.of(1, 2, 3, 4);
    Stream<Integer> output = F.concat(s1).apply(s2);
    assertArrayEquals(expected.toArray(Integer[]::new), output.toArray(Integer[]::new));
  }

  @Test
  @DisplayName("find should return the first found item from a stream given a predicate")
  public void find() {
    Stream<Integer> s1 = Stream.of(1, 2);
    Integer output = F.find(x -> x == 1, s1).get();
    Integer expected = 1;
    assertEquals(expected, output);
  }

  @Test
  @DisplayName("filter should filter")
  public void filter() {
    Stream<Integer> s1 = Stream.of(1, 2, 3, 4);
    Stream<Integer> output = F.filter(x -> x > 2, s1);
    Stream<Integer> expected = Stream.of(3, 4);
    assertArrayEquals(expected.toArray(Integer[]::new), output.toArray(Integer[]::new));
  }

  @Test
  @DisplayName("complement should return the complement of a Predicate")
  public void complement() {
    Predicate<Integer> p = x -> x > 2;
    boolean output = F.complement(p).test(3);
    boolean expected = false;
    assertEquals(expected, output);
  }

  @Test
  @DisplayName("reject should reject")
  public void reject() {
    Stream<Integer> s1 = Stream.of(1, 2, 3, 4);
    Stream<Integer> output = F.reject(x -> x > 2, s1);
    Stream<Integer> expected = Stream.of(1, 2);
    assertArrayEquals(expected.toArray(Integer[]::new), output.toArray(Integer[]::new));
  }

  @Test
  @DisplayName("replace should replace the first element that matches the predicate with the given value")
  public void replaceOne() {
    Stream<Integer> s = Stream.of(1, 2, 3, 4);
    Stream<Integer> output = F.replace(x -> x > 2, 19, s);
    Stream<Integer> expected = Stream.of(1, 2, 19, 4);
    assertArrayEquals(expected.toArray(Integer[]::new), output.toArray(Integer[]::new));
  }

}
