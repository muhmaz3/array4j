package com.googlecode.array4j;

import java.util.Arrays;

public final class DenseFloatSupport<M extends DenseMatrix<M, V> & FloatMatrix<M, V>, V extends DenseVector<V> & FloatVector<V>> {
    private final int columns;

    private final float[] data;

    private final M matrix;

    private final DenseMatrixSupport<M, V> matrixSupport;

    private final int offset;

    private final Orientation orientation;

    private final int rows;

    private final int size;

    private final int stride;

    public DenseFloatSupport(final M matrix, final float[] data) {
        this.matrix = matrix;
        // TODO might be able to get rid of this...
        this.matrixSupport = matrix.getMatrixSupport();
        this.data = data;
        this.size = matrix.size();
        this.rows = matrix.rows();
        this.columns = matrix.columns();
        this.offset = matrix.offset();
        this.stride = matrix.stride();
        this.orientation = matrix.orientation();
    }

    public void setColumn(final int column, final FloatVector columnVector) {
        matrixSupport.checkColumnIndex(column);
        if (!columnVector.isColumnVector() || rows != columnVector.size()) {
            throw new IllegalArgumentException();
        }

        if (!(columnVector instanceof DenseFloatVector)) {
            // TODO support any FloatVector instance here, maybe with a special
            // case for the DirectFloatVector
            throw new UnsupportedOperationException();
        }

        DenseFloatVector denseColVec = (DenseFloatVector) columnVector;
        denseColVec.copyTo(data, matrixSupport.columnOffset(column), matrixSupport.rowStride);
    }

    public void setRow(final int row, final FloatVector rowVector) {
        matrixSupport.checkRowIndex(row);
        if (!rowVector.isRowVector() || columns != rowVector.size()) {
            throw new IllegalArgumentException();
        }

        if (!(rowVector instanceof DenseFloatVector)) {
            // TODO support any FloatVector instance here, maybe with a special
            // case for the DirectFloatVector
            throw new UnsupportedOperationException();
        }

        DenseFloatVector denseRowVec = (DenseFloatVector) rowVector;
        denseRowVec.copyTo(data, matrixSupport.rowOffset(row), matrixSupport.columnStride);
    }

    public float[] toArray() {
        float[] arr = new float[size];
        if (size == 0) {
            return arr;
        }
        if (stride == 0) {
            Arrays.fill(arr, data[offset]);
            return arr;
        }
        for (int i = offset, j = 0; j < size; i += stride, j++) {
            arr[j] = data[i];
        }
        return arr;
    }
}
