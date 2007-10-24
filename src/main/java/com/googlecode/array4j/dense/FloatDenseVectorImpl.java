package com.googlecode.array4j.dense;

import com.googlecode.array4j.FloatMatrixUtils;

final class FloatDenseVectorImpl extends AbstractFloatDense implements FloatDenseVector {
    private static final long serialVersionUID = 1L;

//    /**
//     * Constructor for internal use.
//     */
//    CopyOfFloatDenseVector(final AbstractFloatDense<?> base, final int size, final int offset, final int stride,
//            final Order orientation) {
//        super(base, size, offset, stride, orientation);
//    }
//
//    public CopyOfFloatDenseVector(final FloatBuffer data, final int size, final int offset, final int stride,
//            final Order orientation) {
//        super(data, size, offset, stride, orientation);
//    }
//
//    /**
//     * Constructor.
//     */
//    public CopyOfFloatDenseVector(final int size) {
//        this(size, Order.DEFAULT_FOR_VECTOR, Storage.DEFAULT_FOR_DENSE);
//    }
//
//    /**
//     * Construct with specified orientation and storage.
//     */
//    public CopyOfFloatDenseVector(final int size, final Order orientation, final Storage storage) {
//        super(size, orientation, storage);
//    }
//
//    public CopyOfFloatDenseVector(final Order orientation, final Storage storage, final float... values) {
//        this(values.length, orientation, storage);
//        data.put(values);
//    }
//
//    public CopyOfFloatDenseVector(final Storage storage, final float... values) {
//        this(Order.DEFAULT_FOR_VECTOR, storage, values);
//    }

//    @Override
//    public CopyOfFloatDenseVector minus(final FloatVector<?> other) {
//        // TODO use axpy here
//        if (length != other.length()) {
//            throw new IllegalArgumentException();
//        }
//        CopyOfFloatDenseVector newVector = new CopyOfFloatDenseVector(length, orientation, storage());
//        for (int i = 0; i < length; i++) {
//            newVector.set(i, get(i) - other.get(i));
//        }
//        return newVector;
//    }

//    @Override
//    public void plusEquals(final FloatVector<?> other) {
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
//    }

    @Override
    public String toString() {
        return FloatMatrixUtils.toString(this);
    }

    @Override
    public FloatDenseVector transpose() {
//        return new CopyOfFloatDenseVector(this, length, offset, stride, orientation.transpose());
        return null;
    }
}
