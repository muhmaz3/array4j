package com.googlecode.array4j;

public abstract class AbstractDoubleArray<E extends AbstractDoubleArray> extends AbstractArray<E> implements
        DoubleArray<E> {
    private static final int ELEMENT_SIZE = 8;

    public final int elementSize() {
        return ELEMENT_SIZE;
    }
}
