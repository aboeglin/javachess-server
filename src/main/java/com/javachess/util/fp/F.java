package com.javachess.util.fp;

import java.util.List;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class F {

  private F() {
  }

  public static <A, B> Function<A, B> pipe(Function<A, B> f1) {
    return f1;
  }

  public static <A, B, C> Function<A, C> pipe(Function<A, B> f1, Function<B, C> f2) {
    return f1.andThen(f2);
  }

  public static <A, B, C, D> Function<A, D> pipe(
    Function<A, B> f1,
    Function<B, C> f2,
    Function<C, D> f3
  ) {
    return pipe(f1, f2).andThen(f3);
  }

  public static <A, B, C, D, E> Function<A, E> pipe(
    Function<A, B> f1,
    Function<B, C> f2,
    Function<C, D> f3,
    Function<D, E> f4
  ) {
    return pipe(f1, f2, f3).andThen(f4);
  }

  public static <A, B, C, D, E, F> Function<A, F> pipe(
    Function<A, B> f1,
    Function<B, C> f2,
    Function<C, D> f3,
    Function<D, E> f4,
    Function<E, F> f5
  ) {
    return pipe(f1, f2, f3, f4).andThen(f5);
  }

  public static <A, B, C, D, E, F, G> Function<A, G> pipe(
    Function<A, B> f1,
    Function<B, C> f2,
    Function<C, D> f3,
    Function<D, E> f4,
    Function<E, F> f5,
    Function<F, G> f6
  ) {
    return pipe(f1, f2, f3, f4, f5).andThen(f6);
  }

  public static <A, B, C, D, E, F, G, H> Function<A, H> pipe(
    Function<A, B> f1,
    Function<B, C> f2,
    Function<C, D> f3,
    Function<D, E> f4,
    Function<E, F> f5,
    Function<F, G> f6,
    Function<G, H> f7
  ) {
    return pipe(f1, f2, f3, f4, f5, f6).andThen(f7);
  }

  public static <T, V> V ifElse(
    Function<T, Boolean> predicate,
    Function<T, V> ifTrue,
    Function<T, V> ifFalse,
    T t
  ) {
    return predicate.apply(t)
      ? ifTrue.apply(t)
      : ifFalse.apply(t);
  }

  @Curry
  public static <T, V> Function<T, V> ifElse(
    Function<T, Boolean> predicate,
    Function<T, V> ifTrue,
    Function<T, V> ifFalse
  ) {
    return v -> ifElse(predicate, ifTrue, ifFalse, v);
  }

  public static <T> Stream<T> concat(Stream<T> s1, Stream<T> s2) {
    return Stream.concat(s1, s2);
  }

  @Curry
  public static <T> Function<Stream<T>, Stream<T>> concat(Stream<T> s1) {
    return (Stream<T> s2) -> concat(s1, s2);
  }

  public static <T> Optional<T> find(Predicate<T> p, Stream<T> s) {
    return s.filter(p).findFirst();
  }

  @Curry
  public static <T> Function<Stream<T>, Optional<T>> find(Predicate<T> p) {
    return s -> s.filter(p).findFirst();
  }

  public static <T, V> Stream<V> map(Function<T, V> fn, Stream<T> s) {
    return s.map(fn);
  }

  @Curry
  public static <T, V> Function<Stream<T>, Stream<V>> map(Function<T, V> fn) {
    return arr -> map(fn, arr);
  }

  public static <T> Stream<T> filter(Predicate<T> p, Stream<T> s) {
    return s.filter(p);
  }

  @Curry
  public static <T> Function<Stream<T>, Stream<T>> filter(Predicate<T> p) {
    return s -> filter(p, s);
  }

  public static <T> Predicate<T> complement(Predicate<T> p) {
    return x -> !p.test(x);
  }

  public static <T> Stream<T> reject(Predicate<T> p, Stream<T> s) {
    return filter(complement(p), s);
  }

  @Curry
  public static <T> Function<Stream<T>, Stream<T>> reject(Predicate<T> p) {
    return s -> reject(p, s);
  }

  public static <T> Stream<T> replace(Predicate<T> p, T value, Stream<T> s) {
    final Closured<Boolean> done = Closured.of(false);

    return map(x -> {
      if (p.test(x) && !done.value()) {
        done.mutate(true);
        return value;
      }
      return x;
    }, s);
  }

  @Curry
  public static <T> Function<Stream<T>, Stream<T>> replace(Predicate<T> p, T value) {
    return s -> replace(p, value, s);
  }

  public static <T> T tap(Consumer<? super T> action, T s) {
    action.accept(s);
    return s;
  }

  @Curry
  public static <T> Function<T, T> tap(Consumer<? super T> action) {
    return s -> tap(action, s);
  }

  public static <T> T last(Stream<T> s1) {
    List<T> l = s1.collect(Collectors.toList());
    return l.get(l.size() - 1);
  }

  public static <T, V> V reduce(BiFunction<V, T, V> reducer, V initialValue, Stream<T> s) {
    List<T> values = s.collect(Collectors.toList());
    V computed = initialValue;
    for (T v : values) {
      computed = reducer.apply(computed, v);
    }
    return computed;
  }
}

class Closured<T> {
  private T value;

  private Closured(T value) {
    this.value = value;
  }

  public static <T> Closured<T> of(T value) {
    return new Closured(value);
  }

  public void mutate(T value) {
    this.value = value;
  }

  public T value() {
    return this.value;
  }
}
