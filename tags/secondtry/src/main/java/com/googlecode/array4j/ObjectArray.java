package com.googlecode.array4j;

public interface ObjectArray<T, A extends ObjectArray<T, A>> extends Array<A> {
    T[] toArray();
}
