package com.googlecode.array4j;

// TODO implement BigIntegerArray on top of ObjectArray

// TODO implement BigDecimalArray on top of ObjectArray

public class ObjectArray<T> extends AbstractArray {
    public ObjectArray(final int[] shape) {
        super(shape);
    }

    public final T[] toArray() {
        throw new UnsupportedOperationException();
    }
}
