package com.googlecode.array4j.dense;

import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.Order;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.math.FloatMatrixUtils;
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
        this(other.rows(), other.columns(), Order.DEFAULT, Storage.DEFAULT_FOR_DENSE);
        copy(other, this);
    }

    public FloatDenseMatrixImpl(final int rows, final int columns, final Order order, final Storage storage) {
        super(rows, columns, order, storage);
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
