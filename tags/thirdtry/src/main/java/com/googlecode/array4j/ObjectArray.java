package com.googlecode.array4j;

public class ObjectArray<T> extends AbstractArray<ObjectArray<T>> {
    // TODO rename size to length
    public ObjectArray(final int size) {
        super(new int[]{size});
    }

    public final T[] toArray() {
        return null;
    }

    // TODO implement BigIntegerArray on top of ObjectArray
}
