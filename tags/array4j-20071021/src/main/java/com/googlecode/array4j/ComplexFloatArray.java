package com.googlecode.array4j;

public interface ComplexFloatArray<A extends ComplexFloatArray<A>> extends Array<A> {
    ComplexFloat[] toArray();
}
