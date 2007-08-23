package com.googlecode.array4j.dense;

import java.nio.FloatBuffer;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.googlecode.array4j.ComplexFloat;
import com.googlecode.array4j.ComplexFloatVector;
import com.googlecode.array4j.Orientation;
import com.googlecode.array4j.Storage;

public final class CFloatDenseVector extends AbstractCFloatDense<CFloatDenseVector> implements
        ComplexFloatVector<CFloatDenseVector>, DenseVector<CFloatDenseVector> {
    private static final long serialVersionUID = 1L;

    public CFloatDenseVector(final ComplexFloat... values) {
        this(Orientation.DEFAULT_FOR_VECTOR, Storage.DEFAULT_FOR_DENSE, values);
    }

    /**
     * Constructor for internal use.
     */
    CFloatDenseVector(final FloatBuffer data, final int size, final int offset, final int stride,
            final Orientation orientation) {
        super(data, size, offset, stride, orientation);
    }

    /**
     * Constructor.
     */
    public CFloatDenseVector(final int size) {
        this(size, Orientation.DEFAULT_FOR_VECTOR, Storage.DEFAULT_FOR_DENSE);
    }

    /**
     * Construct with specified orientation and storage.
     */
    public CFloatDenseVector(final int size, final Orientation orientation, final Storage storage) {
        super(size, orientation, storage);
    }

    public CFloatDenseVector(final Orientation orientation, final Storage storage, final ComplexFloat... values) {
        this(values.length, orientation, storage);
        // TODO support [real..., imag...] packing scheme
        for (int i = 0; i < values.length; i++) {
            data.put(values[i].real());
            data.put(values[i].imag());
        }
    }

    @Override
    public CFloatDenseVector asVector() {
        return this;
    }

    @Override
    public CFloatDenseVector conjTranpose() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof CFloatDenseVector)) {
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
    public CFloatDenseVector transpose() {
        return new CFloatDenseVector(data, length, offset, stride, orientation.transpose());
    }
}
