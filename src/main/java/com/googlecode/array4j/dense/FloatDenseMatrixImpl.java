package com.googlecode.array4j.dense;

import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.FloatMatrixUtils;
import com.googlecode.array4j.Order;
import com.googlecode.array4j.Storage;
import java.nio.FloatBuffer;

final class FloatDenseMatrixImpl extends AbstractFloatDense implements FloatDenseMatrix {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for internal use.
     */
    FloatDenseMatrixImpl(final AbstractFloatDense base, final int rows, final int columns, final int offset,
            final int stride, final Order order) {
        super(base, rows, columns, offset, stride, order);
    }

    FloatDenseMatrixImpl(final FloatBuffer data, final int rows, final int columns, final int offset,
            final int stride, final Order order) {
        super(data, rows, columns, offset, stride, order);
    }

    /** Copy constructor. */
    public FloatDenseMatrixImpl(final FloatMatrix other) {
        this(other.rows(), other.columns());
        // TODO optimize this
        for (int i = 0; i < other.rows(); i++) {
            for (int j = 0; j < other.columns(); j++) {
                set(i, j, other.get(i, j));
            }
        }
    }

    public FloatDenseMatrixImpl(final int rows, final int columns) {
        this(rows, columns, Order.DEFAULT, Storage.DEFAULT_FOR_DENSE);
    }

    public FloatDenseMatrixImpl(final int rows, final int columns, final Order orientation, final Storage storage) {
        super(rows, columns, orientation, storage);
    }

    @Override
    public FloatDenseMatrix transpose() {
        return new FloatDenseMatrixImpl(this, columns, rows, offset, stride, order.transpose());
    }

    @Override
    public String toString() {
        return FloatMatrixUtils.toString(this);
    }
}
