package com.javachess.utils;

import java.util.stream.Stream;

public class Arrays {
	//  Flatten a stream of multiple arrays of same type in Java
	public static <T> Stream<T> flatten(T[] ... arrays) {
		return Stream.of(arrays).flatMap(java.util.Arrays::stream);
	}
}
