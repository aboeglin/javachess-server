
package com.javachess.util.fp;

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
  @DisplayName("pipe should work with 3 functions")
  public void pipe3Functions() {
    Integer expected = 16;
    Integer output = F.pipe(
      (Integer x) -> x + 1,
      (Integer x) -> x * 2,
      (Integer x) -> x * 2
    ).apply(3);
    assertEquals(expected, output);
  }

  @Test
  @DisplayName("pipe should work with 4 functions")
  public void pipe4Functions() {
    Integer expected = 32;
    Integer output = F.pipe(
      (Integer x) -> x + 1,
      (Integer x) -> x * 2,
      (Integer x) -> x * 2,
      (Integer x) -> x * 2
    ).apply(3);
    assertEquals(expected, output);
  }

  @Test
  @DisplayName("pipe should work with 5 functions")
  public void pipe5Functions() {
    Integer expected = 64;
    Integer output = F.pipe(
      (Integer x) -> x + 1,
      (Integer x) -> x * 2,
      (Integer x) -> x * 2,
      (Integer x) -> x * 2,
      (Integer x) -> x * 2
    ).apply(3);
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
  @DisplayName("reject should be curried")
  public void rejectCurried() {
    Stream<Integer> s1 = Stream.of(1, 2, 3, 4);
    Stream<Integer> output = F.reject((Integer x) -> x > 2).apply(s1);
    Stream<Integer> expected = Stream.of(1, 2);
    assertArrayEquals(expected.toArray(Integer[]::new), output.toArray(Integer[]::new));
  }

  @Test
  @DisplayName("last should return the last element of a stream")
  public void last() {
    Stream<Integer> s1 = Stream.of(1, 2, 3, 4);
    Integer output = F.last(s1);
    Integer expected = 4;
    assertEquals(expected, output);
  }

  @Test
  @DisplayName("replace should replace the first element that matches the predicate with the given value")
  public void replaceOne() {
    Stream<Integer> s = Stream.of(1, 2, 3, 4);
    Stream<Integer> output = F.replace(x -> x > 2, 19, s);
    Stream<Integer> expected = Stream.of(1, 2, 19, 4);
    assertArrayEquals(expected.toArray(Integer[]::new), output.toArray(Integer[]::new));
  }

  @Test
  @DisplayName("tap should return the given value")
  public void tapReturn() {
    Integer expected = 3;
    Integer actual = F.tap(x -> {}, expected);
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("tap should execute the given function and have access to the given value")
  public void tapExec() {
    Integer expected = 3;
    Integer n = F.tap(actual -> {
      assertEquals(expected, actual);
    }, expected);
  }

  @Test
  public void reduce() {
    Stream<Integer> s = Stream.of(1, 2, 3, 4);
    Integer actual = F.reduce((acc, v) -> acc + v, 0, s);
    Integer expected = 10;
    assertEquals(expected, actual);
  }

}
