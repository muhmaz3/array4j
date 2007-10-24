package com.googlecode.array4j.array;

import com.googlecode.array4j.AbstractArray;
import com.googlecode.array4j.DoubleArray;
import java.nio.DoubleBuffer;

final class DoubleArrayImpl extends AbstractArray implements DoubleArray {
    private static final long serialVersionUID = 1L;

    private transient DoubleBuffer data;

    public DoubleArrayImpl(final int... shape) {
        super(shape);
    }

    @Override
    public double[] toArray() {
        // TODO Auto-generated method stub
        return null;
    }
}
