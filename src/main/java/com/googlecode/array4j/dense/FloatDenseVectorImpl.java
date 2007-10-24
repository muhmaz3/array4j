package com.googlecode.array4j.dense;

import com.googlecode.array4j.Direction;
import com.googlecode.array4j.FloatMatrixUtils;
import com.googlecode.array4j.FloatVector;
import com.googlecode.array4j.Storage;
import java.nio.FloatBuffer;

final class FloatDenseVectorImpl extends AbstractFloatDense implements FloatDenseVector {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for internal use.
     */
    FloatDenseVectorImpl(final AbstractFloatDense base, final int size, final int offset, final int stride,
            final Direction direction) {
        super(base, size, offset, stride, direction);
    }

    FloatDenseVectorImpl(final FloatBuffer data, final int size, final int offset, final int stride,
            final Direction direction) {
        super(data, size, offset, stride, direction);
    }

    /**
     * Constructor.
     */
    FloatDenseVectorImpl(final int size) {
        this(size, Direction.DEFAULT, Storage.DEFAULT_FOR_DENSE);
    }

    /**
     * Construct with specified direction and storage.
     */
    FloatDenseVectorImpl(final int size, final Direction direction, final Storage storage) {
        super(size, direction, storage);
    }

    FloatDenseVectorImpl(final Direction direction, final Storage storage, final float... values) {
        this(values.length, direction, storage);
        data.put(values);
    }

    FloatDenseVectorImpl(final Storage storage, final float... values) {
        this(Direction.DEFAULT, storage, values);
    }

    @Override
    public FloatDenseVectorImpl minus(final FloatVector other) {
//        // TODO use axpy here
//        if (length != other.length()) {
//            throw new IllegalArgumentException();
//        }
//        CopyOfFloatDenseVector newVector = new CopyOfFloatDenseVector(length, orientation, storage());
//        for (int i = 0; i < length; i++) {
//            newVector.set(i, get(i) - other.get(i));
//        }
//        return newVector;
        return null;
    }

    @Override
    public void plusEquals(final FloatVector other) {
//        if (other instanceof CopyOfFloatDenseVector) {
//            FloatDenseBLAS.DEFAULT.axpy(1.0f, (CopyOfFloatDenseVector) other, this);
//        } else {
//            if (length != other.length()) {
//                throw new IllegalArgumentException();
//            }
//            for (int i = 0; i < length; i++) {
//                set(i, get(i) + other.get(i));
//            }
//        }
    }

    @Override
    public String toString() {
        return FloatMatrixUtils.toString(this);
    }

    @Override
    public FloatDenseVector transpose() {
        Direction direction = isRowVector() ? Direction.ROW : Direction.COLUMN;
        return new FloatDenseVectorImpl(this, length, offset, stride, direction);
    }
}
