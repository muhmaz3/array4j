package com.googlecode.array4j.dense;

import java.nio.FloatBuffer;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.Orientation;
import com.googlecode.array4j.Storage;

public final class FloatDenseMatrix extends AbstractFloatDense<FloatDenseMatrix> implements
        FloatMatrix<FloatDenseMatrix, FloatDenseVector> {
    /**
     * Constructor for internal use.
     */
    FloatDenseMatrix(final FloatBuffer data, final int rows, final int columns, final int offset, final int stride,
            final Orientation orientation) {
        super(data, rows, columns, offset, stride, orientation);
    }

    /**
     * Copy constructor.
     *
     * @param other
     *                matrix to copy
     */
    public FloatDenseMatrix(final FloatMatrix<?, ?> other) {
        this(other.rows(), other.columns());
        // TODO optimize this
        for (int i = 0; i < other.rows(); i++) {
            for (int j = 0; j < other.columns(); j++) {
                set(i, j, other.get(i, j));
            }
        }
    }

    /**
     * Constructor.
     */
    public FloatDenseMatrix(final int rows, final int columns) {
        this(rows, columns, Orientation.DEFAULT, Storage.DEFAULT_FOR_DENSE);
    }

    /**
     * Construct with specified orientation and storage.
     */
    public FloatDenseMatrix(final int rows, final int columns, final Orientation orientation, final Storage storage) {
        super(rows, columns, orientation, storage);
    }

    @Override
    public FloatDenseVector asVector() {
        return new FloatDenseVector(data, size, offset, stride, Orientation.DEFAULT_FOR_VECTOR);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof FloatDenseMatrix)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        return new EqualsBuilder().appendSuper(super.equals(obj)).isEquals();
    }

    @Override
    public FloatDenseMatrix subMatrixColumns(final int column0, final int column1) {
        checkArgument(column0 >= 0 && column0 <= columns,
                String.format("column0=%d not in range [0, %d]", column0, columns));
        checkArgument(column1 >= column0 && column1 <= columns,
                String.format("column1=%d not in range [%d, %d]", column1, column0, columns));
        // TODO only copy if necessary
        FloatDenseMatrix newMatrix = new FloatDenseMatrix(rows, column1 - column0, orientation, storage());
        for (int i = column0, j = 0; i < column1; i++, j++) {
            newMatrix.setColumn(j, column(i));
        }
        return newMatrix;
    }

    @Override
    public FloatDenseMatrix transpose() {
        return new FloatDenseMatrix(data, columns, rows, offset, stride, orientation.transpose());
    }
}
