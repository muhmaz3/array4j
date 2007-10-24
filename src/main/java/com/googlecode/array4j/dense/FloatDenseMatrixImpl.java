package com.googlecode.array4j.dense;

import com.googlecode.array4j.FloatMatrixUtils;

final class FloatDenseMatrixImpl extends AbstractFloatDense implements FloatDenseMatrix {
    private static final long serialVersionUID = 1L;

//    /**
//     * Constructor for internal use.
//     */
//    CopyOfFloatDenseMatrix(final AbstractFloatDense<?> base, final int rows, final int columns, final int offset,
//            final int stride, final Order orientation) {
//        super(base, rows, columns, offset, stride, orientation);
//    }
//
//    public CopyOfFloatDenseMatrix(final FloatBuffer data, final int rows, final int columns, final int offset,
//            final int stride, final Order orientation) {
//        super(data, rows, columns, offset, stride, orientation);
//    }
//
//    /**
//     * Copy constructor.
//     *
//     * @param other
//     *                matrix to copy
//     */
//    public FloatDenseMatrix(final FloatMatrix<?, ?> other) {
//        this(other.rows(), other.columns());
//        // TODO optimize this
//        for (int i = 0; i < other.rows(); i++) {
//            for (int j = 0; j < other.columns(); j++) {
//                set(i, j, other.get(i, j));
//            }
//        }
//    }
//
//    /**
//     * Constructor.
//     */
//    public CopyOfFloatDenseMatrix(final int rows, final int columns) {
//        this(rows, columns, Order.DEFAULT, Storage.DEFAULT_FOR_DENSE);
//    }
//
//    /**
//     * Construct with specified orientation and storage.
//     */
//    public CopyOfFloatDenseMatrix(final int rows, final int columns, final Order orientation, final Storage storage) {
//        super(rows, columns, orientation, storage);
//    }

    @Override
    public FloatDenseMatrix transpose() {
//        return new FloatDenseMatrixImpl(this, columns, rows, offset, stride, order.transpose());
        return null;
    }

    @Override
    public String toString() {
        return FloatMatrixUtils.toString(this);
    }
}
