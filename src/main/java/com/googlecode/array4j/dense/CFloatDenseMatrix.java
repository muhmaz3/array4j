package com.googlecode.array4j.dense;

import java.nio.FloatBuffer;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.googlecode.array4j.ComplexFloatMatrix;
import com.googlecode.array4j.Orientation;
import com.googlecode.array4j.Storage;

public final class CFloatDenseMatrix extends AbstractCFloatDense<CFloatDenseMatrix> implements
        ComplexFloatMatrix<CFloatDenseMatrix, CFloatDenseVector> {
    /**
     * Copy constructor.
     *
     * @param other
     *                matrix to copy
     */
    public CFloatDenseMatrix(final ComplexFloatMatrix<?, ?> other) {
        this(other.rows(), other.columns());
        // TODO optimize this
        for (int i = 0; i < other.rows(); i++) {
            for (int j = 0; j < other.columns(); j++) {
                set(i, j, other.get(i, j));
            }
        }
    }

    /**
     * Constructor for internal use.
     */
    CFloatDenseMatrix(final FloatBuffer data, final int rows, final int columns, final int offset, final int stride,
            final Orientation orientation) {
        super(data, rows, columns, offset, stride, orientation);
    }

    /**
     * Constructor.
     */
    public CFloatDenseMatrix(final int rows, final int columns) {
        this(rows, columns, Orientation.DEFAULT, Storage.DEFAULT_FOR_DENSE);
    }

    /**
     * Construct with specified orientation and storage.
     */
    public CFloatDenseMatrix(final int rows, final int columns, final Orientation orientation, final Storage storage) {
        super(rows, columns, orientation, storage);
    }

    @Override
    public CFloatDenseVector asVector() {
        return new CFloatDenseVector(data, size, offset, stride, Orientation.DEFAULT_FOR_VECTOR);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof CFloatDenseMatrix)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        return new EqualsBuilder().appendSuper(super.equals(obj)).isEquals();
    }

    @Override
    public CFloatDenseMatrix subMatrixColumns(final int column0, final int column1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CFloatDenseMatrix transpose() {
        // TODO implement conjugate transpose too
        return new CFloatDenseMatrix(data, columns, rows, offset, stride, orientation.transpose());
    }
}
