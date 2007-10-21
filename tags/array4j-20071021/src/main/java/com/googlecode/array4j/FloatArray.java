package com.googlecode.array4j;

public interface FloatArray<A extends FloatArray<A>> extends Array<A> {
    float[] toArray();
}
