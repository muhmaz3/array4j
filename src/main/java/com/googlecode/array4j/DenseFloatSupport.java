package com.googlecode.array4j;

import java.util.Arrays;

import com.googlecode.array4j.internal.ToArraysConverter;

public final class DenseFloatSupport<M extends DenseMatrix<M, V> & FloatMatrix<M, V>, V extends DenseVector<V> & FloatVector<V>>
        extends DenseMatrixSupport<M, V, float[]> {
    private final ToArraysConverter<M, float[]> arraysConverter;

    private final float[] data;

    public DenseFloatSupport(final M matrix, final float[] data) {
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
                dest[destPos] = DenseFloatSupport.this.data[srcPos];
            }
        };
    }

    public void setColumnImpl(final int column, final FloatVector<?> columnVector) {
        if (!(columnVector instanceof DenseFloatVector)) {
            // TODO support any FloatVector instance here, maybe with a special
            // case for the DirectFloatVector
            throw new UnsupportedOperationException();
        }
        DenseFloatVector denseColVec = (DenseFloatVector) columnVector;
        denseColVec.copyTo(data, columnOffset(column), rowStride);
    }

    public void setRowImpl(final int row, final FloatVector<?> rowVector) {
        if (!(rowVector instanceof DenseFloatVector)) {
            // TODO support any FloatVector instance here, maybe with a special
            // case for the DirectFloatVector
            throw new UnsupportedOperationException();
        }
        DenseFloatVector denseRowVec = (DenseFloatVector) rowVector;
        denseRowVec.copyTo(data, rowOffset(row), columnStride);
    }

    public float[] toArray() {
        final int size = matrix.size();
        float[] arr = new float[size];
        if (size == 0) {
            return arr;
        }
        if (matrix.stride() == 0) {
            Arrays.fill(arr, data[matrix.offset()]);
            return arr;
        }
        for (int i = matrix.offset(), j = 0; j < size; i += matrix.stride(), j++) {
            arr[j] = data[i];
        }
        return arr;
    }

    @Override
    protected float[][] toArrays(int m, int n, boolean rows) {
        return arraysConverter.toArrays(m, n, rows);
    }
}
