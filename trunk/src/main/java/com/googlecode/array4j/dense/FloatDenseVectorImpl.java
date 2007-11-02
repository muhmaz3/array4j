package com.googlecode.array4j.dense;

import com.googlecode.array4j.Direction;
import com.googlecode.array4j.FloatVector;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.math.FloatMatrixUtils;
import java.nio.FloatBuffer;

final class FloatDenseVectorImpl extends AbstractFloatDense implements FloatDenseVector {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for internal use.
     */
    FloatDenseVectorImpl(final AbstractFloatDense base, final int length, final int offset, final int stride,
            final Direction direction) {
        super(base, length, offset, stride, direction);
    }

    FloatDenseVectorImpl(final Direction direction, final Storage storage, final float... values) {
        this(values.length, direction, storage);
        data.put(values);
    }

    FloatDenseVectorImpl(final FloatBuffer data, final int length, final int offset, final int stride,
            final Direction direction) {
        super(data, length, offset, stride, direction);
    }

    /** Copy constructor. */
    FloatDenseVectorImpl(final FloatVector other) {
        this(other.length(), other.direction(), defaultStorage(other));
        copy(other, this);
    }

    /**
     * Construct with specified direction and storage.
     */
    FloatDenseVectorImpl(final int length, final Direction direction, final Storage storage) {
        super(length, direction, storage);
    }

    @Override
    public String toString() {
        return FloatMatrixUtils.toString(this);
    }

    @Override
    public FloatDenseVector transpose() {
        return new FloatDenseVectorImpl(this, length, offset, stride, direction().transpose());
    }
}
