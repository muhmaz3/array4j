package com.googlecode.array4j.dense;

import com.googlecode.array4j.FloatMatrixUtils;
import com.googlecode.array4j.FloatVector;
import com.googlecode.array4j.Orientation;
import com.googlecode.array4j.Storage;
import java.nio.FloatBuffer;
import org.apache.commons.lang.builder.EqualsBuilder;

public final class FloatDenseVector extends AbstractFloatDense<FloatDenseVector> implements
        FloatVector<FloatDenseVector>, DenseVector<FloatDenseVector> {
    private static final long serialVersionUID = 1L;

    public static FloatDenseVector valueOf(final double... values) {
        float[] floatValues = new float[values.length];
        for (int i = 0; i < values.length; i++) {
            floatValues[i] = (float) values[i];
        }
        return new FloatDenseVector(Orientation.DEFAULT_FOR_VECTOR, Storage.DEFAULT_FOR_DENSE, floatValues);
    }

    public static FloatDenseVector valueOf(final float... values) {
        return new FloatDenseVector(Orientation.DEFAULT_FOR_VECTOR, Storage.DEFAULT_FOR_DENSE, values);
    }

    /**
     * Constructor for internal use.
     */
    FloatDenseVector(final AbstractFloatDense<?> base, final int size, final int offset, final int stride,
            final Orientation orientation) {
        super(base, size, offset, stride, orientation);
    }

    public FloatDenseVector(final FloatBuffer data, final int size, final int offset, final int stride,
            final Orientation orientation) {
        super(data, size, offset, stride, orientation);
    }

    /**
     * Constructor.
     */
    public FloatDenseVector(final int size) {
        this(size, Orientation.DEFAULT_FOR_VECTOR, Storage.DEFAULT_FOR_DENSE);
    }

    /**
     * Construct with specified orientation and storage.
     */
    public FloatDenseVector(final int size, final Orientation orientation, final Storage storage) {
        super(size, orientation, storage);
    }

    public FloatDenseVector(final Orientation orientation, final Storage storage, final float... values) {
        this(values.length, orientation, storage);
        data.put(values);
    }

    public FloatDenseVector(final Storage storage, final float... values) {
        this(Orientation.DEFAULT_FOR_VECTOR, storage, values);
    }

    public FloatDenseMatrix asMatrix() {
        Orientation orient = isRowVector() ? Orientation.ROW : Orientation.COLUMN;
        return new FloatDenseMatrix(data, rows, columns, offset, stride, orient);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof FloatDenseVector)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        return new EqualsBuilder().appendSuper(super.equals(obj)).isEquals();
    }

    public boolean isColumnVector() {
        return orientation.equals(Orientation.COLUMN);
    }

    public boolean isRowVector() {
        return orientation.equals(Orientation.ROW);
    }

    @Override
    public FloatDenseVector minus(final FloatVector<?> other) {
        if (length != other.length()) {
            throw new IllegalArgumentException();
        }
        FloatDenseVector newVector = new FloatDenseVector(length, orientation, storage());
        for (int i = 0; i < length; i++) {
            newVector.set(i, get(i) - other.get(i));
        }
        return newVector;
    }

    @Override
    public void plusEquals(final FloatVector<?> other) {
        if (length != other.length()) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < length; i++) {
            set(i, get(i) + other.get(i));
        }
    }

    @Override
    public String toString() {
        return FloatMatrixUtils.toString(this);
    }

    @Override
    public FloatDenseVector transpose() {
        return new FloatDenseVector(this, length, offset, stride, orientation.transpose());
    }
}
