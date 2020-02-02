package com.javachess.utils;

import java.util.function.Function;

public class FP {

    public static <A,B,C> Function<A,C> pipe(Function<A,B> f1, Function<B,C> f2) {
        return f1.andThen(f2);
    }

    public static <A,B,C,D> Function<A,D> pipe(Function<A,B> f1, Function<B,C> f2, Function<C,D> f3) {
        return pipe(f1, f2).andThen(f3);
    }

    public static <A,B,C,D,E> Function<A,E> pipe(
        Function<A,B> f1,
        Function<B,C> f2,
        Function<C,D> f3,
        Function<D,E> f4
    ) {
        return pipe(f1, f2, f3).andThen(f4);
    }

    public static <T,V> Function<T,V> ifElse(
        Function<T,Boolean> predicate,
        Function<T,V> ifTrue,
        Function<T,V> ifFalse
    ) {
        return value -> predicate.apply(value).booleanValue() ? ifTrue.apply(value) : ifFalse.apply(value);
    }

    // public static <T> T map(Function<? extends Object,? extends Object> fn, T mappable) {
    //   return mappable.map(fn);
    // }

    public static void main(String argv[]) {
        Function<Integer, Integer> addOne = x -> x + 1;
        Function<Integer, Integer> addThree = x -> x + 3;
        Function<Integer, Integer> addFour = FP.pipe(
            addOne,
            addThree
        );

        // System.out.println(ifElse(
        //     x -> Boolean.TRUE,
        //     x -> 3,
        //     x -> 1,
        //     1
        // ));
        System.out.println(addFour.apply(4));
    }

}