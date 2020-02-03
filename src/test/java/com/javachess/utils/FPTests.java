
package com.javachess.utils;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FPTests {

	@Test
	@DisplayName("map should work")
	public void map() {
		Stream<String> expected = Stream.of("a-mapped", "b-mapped");
		Stream<String> input = Stream.of("a", "b");
		Stream<String> output = FP.map(s -> s + "-mapped", input);
		assertArrayEquals(expected.toArray(String[]::new), output.toArray(String[]::new));
	}

	@Test
	@DisplayName("pipe should work")
	public void pipe() {
		Integer expected = 8;
		Integer output = FP.pipe((Integer x) -> x + 1, (Integer x) -> x * 2).apply(3);
		assertEquals(expected, output);
	}

	@Test
	@DisplayName("pipe should work with only one arg")
	public void pipeOne() {
		Integer expected = 4;
		Integer output = FP.pipe((Integer x) -> x + 1).apply(3);
		assertEquals(expected, output);
	}

	@Test
	@DisplayName("concat should concatenate two streams")
	public void concat() {
		Stream<Integer> s1 = Stream.of(1, 2);
		Stream<Integer> s2 = Stream.of(3, 4);
		Stream<Integer> expected = Stream.of(1, 2, 3, 4);
		Stream<Integer> output = FP.concat(s1, s2);
		assertArrayEquals(expected.toArray(Integer[]::new), output.toArray(Integer[]::new));
	}

	@Test
	@DisplayName("concat should be curried")
	public void concatCurried() {
		Stream<Integer> s1 = Stream.of(1, 2);
		Stream<Integer> s2 = Stream.of(3, 4);
		Stream<Integer> expected = Stream.of(1, 2, 3, 4);
		Stream<Integer> output = FP.concat(s1).apply(s2);
		assertArrayEquals(expected.toArray(Integer[]::new), output.toArray(Integer[]::new));
	}

	@Test
	@DisplayName("find should return the first found item from a stream given a predicate")
	public void find() {
		Stream<Integer> s1 = Stream.of(1, 2);
		Integer output = FP.find(x -> x == 1, s1).get();
		Integer expected = 1;
		assertEquals(expected, output);
	}

}
