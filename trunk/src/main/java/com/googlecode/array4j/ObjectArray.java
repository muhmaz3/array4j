package com.googlecode.array4j;

// TODO implement BigIntegerArray on top of ObjectArray

// TODO implement BigDecimalArray on top of ObjectArray

// TODO possibly derive them from AbstractNumberArray<E extends Number>

public class ObjectArray<T> extends AbstractArray {
    private static final long serialVersionUID = 1L;

    public ObjectArray(final int[] shape) {
        super(shape);
    }

    public final T[] toArray() {
        throw new UnsupportedOperationException();
    }
}
