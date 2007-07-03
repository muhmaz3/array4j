package com.googlecode.array4j;

import java.nio.FloatBuffer;
import java.util.Arrays;

public final class DirectFloatSupport<M extends DenseMatrix<M, V> & FloatMatrix<M, V>, V extends DenseVector<V> & FloatVector<V>>
        extends DenseMatrixSupport<M, V> {
    private final FloatBuffer data;

    public DirectFloatSupport(final M matrix, final FloatBuffer data) {
        super(matrix);
        this.data = data;
    }

    private FloatBuffer getData() {
        return (FloatBuffer) ((FloatBuffer) data.rewind()).position(matrix.offset());
    }

    @Override
    protected void setColumnImpl(int column, FloatVector<?> columnVector) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void setRowImpl(int row, FloatVector<?> rowVector) {
        // TODO Auto-generated method stub
    }

    public float[] toArray() {
        final int size = matrix.size();
        final int offset = matrix.offset();
        final int stride = matrix.stride();
        float[] arr = new float[size];
        if (size == 0) {
            return arr;
        }
        if (stride == 0) {
            Arrays.fill(arr, getData().get(0));
            return arr;
        }
        FloatBuffer src = getData();
        for (int i = offset, j = 0; j < size; i += stride, j++) {
            arr[j] = src.get(i);
        }
        return arr;
    }
}
