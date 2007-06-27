package com.googlecode.array4j;

public interface ObjectArray<T, A extends ObjectArray> extends Array<A> {
    T[] toArray();
}
