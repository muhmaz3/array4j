package com.googlecode.array4j;

import java.nio.FloatBuffer;
import java.util.Arrays;

import com.googlecode.array4j.internal.ToArraysConverter;

public final class DirectFloatSupport<M extends DenseMatrix<M, V> & FloatMatrix<M, V>, V extends DenseVector<V> & FloatVector<V>>
        extends DenseMatrixSupport<M, V, float[]> {
    private final ToArraysConverter<M, float[]> arraysConverter;

    private final FloatBuffer data;

    public DirectFloatSupport(final M matrix, final FloatBuffer data) {
        super(matrix);
        this.data = data;
        this.arraysConverter = new ToArraysConverter<M, float[]>(matrix) {
            @Override
            protected float[] createArray(final int length) {
                return new float[length];
            }

            @Override
            protected float[][] createArrayArray(final int length) {
                return new float[length][];
            }

            @Override
            protected void set(final int srcPos, final float[] dest, final int destPos) {
                dest[destPos] = getData().get(srcPos);
            }
        };
    }

    private FloatBuffer getData() {
//        return (FloatBuffer) ((FloatBuffer) data.rewind()).position(matrix.offset());
        return (FloatBuffer) data.rewind();
    }

    @Override
    protected void setColumnImpl(final int column, final FloatVector<?> columnVector) {
        if (!(columnVector instanceof DirectFloatVector)) {
            throw new UnsupportedOperationException();
        }
        final DirectFloatVector directColVec = (DirectFloatVector) columnVector;
        directColVec.copyTo(getData(), columnOffset(column), rowStride);
    }

    @Override
    protected void setRowImpl(final int row, final FloatVector<?> rowVector) {
        if (!(rowVector instanceof DirectFloatVector)) {
            throw new UnsupportedOperationException();
        }
        final DirectFloatVector directRowVec = (DirectFloatVector) rowVector;
        directRowVec.copyTo(getData(), rowOffset(row), columnStride);
    }

    public float[] toArray() {
        final int size = matrix.size();
        final int offset = matrix.offset();
        final int stride = matrix.stride();
        final float[] arr = new float[size];
        if (size == 0) {
            return arr;
        }
        if (stride == 0) {
            Arrays.fill(arr, getData().get(offset));
            return arr;
        }
        final FloatBuffer src = getData();
        for (int i = offset, j = 0; j < size; i += stride, j++) {
            arr[j] = src.get(i);
        }
        return arr;
    }

    @Override
    protected float[][] toArrays(final int m, final int n, final boolean rows) {
        return arraysConverter.toArrays(m, n, rows);
    }
}
