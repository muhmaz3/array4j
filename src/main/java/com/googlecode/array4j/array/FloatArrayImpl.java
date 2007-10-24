package com.googlecode.array4j.array;

import com.googlecode.array4j.AbstractArray;
import com.googlecode.array4j.FloatArray;
import java.nio.FloatBuffer;

final class FloatArrayImpl extends AbstractArray implements FloatArray {
    private static final long serialVersionUID = 1L;

    private transient FloatBuffer data;

    public FloatArrayImpl(final int... shape) {
        super(shape);
    }

    @Override
    public float[] toArray() {
        // TODO Auto-generated method stub
        return null;
    }
}
