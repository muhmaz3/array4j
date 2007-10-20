package com.googlecode.array4j;

public interface DoubleArray<A extends DoubleArray<A>> extends Array<A> {
    double[] toArray();
}
