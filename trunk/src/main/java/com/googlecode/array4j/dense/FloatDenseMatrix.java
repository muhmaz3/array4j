package com.googlecode.array4j.dense;

import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.FloatMatrixUtils;
import com.googlecode.array4j.Orientation;
import com.googlecode.array4j.Storage;
import java.nio.FloatBuffer;
import org.apache.commons.lang.builder.EqualsBuilder;

public final class FloatDenseMatrix extends AbstractFloatDense<FloatDenseMatrix> implements
        FloatMatrix<FloatDenseMatrix, FloatDenseVector> {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for internal use.
     */
    FloatDenseMatrix(final AbstractFloatDense<?> base, final int rows, final int columns, final int offset,
            final int stride, final Orientation orientation) {
        super(base, rows, columns, offset, stride, orientation);
    }

    public FloatDenseMatrix(final FloatBuffer data, final int rows, final int columns, final int offset,
            final int stride, final Orientation orientation) {
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
    public String toString() {
        return FloatMatrixUtils.toString(this);
    }

    @Override
    public FloatDenseMatrix transpose() {
        return new FloatDenseMatrix(this, columns, rows, offset, stride, orientation.transpose());
    }
}
