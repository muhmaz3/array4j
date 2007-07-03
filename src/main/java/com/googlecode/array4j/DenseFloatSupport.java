package com.googlecode.array4j;

import java.util.Arrays;

public final class DenseFloatSupport<M extends DenseMatrix<M, V> & FloatMatrix<M, V>, V extends DenseVector<V> & FloatVector<V>>
        extends DenseMatrixSupport<M, V> {
    private final float[] data;

    public DenseFloatSupport(final M matrix, final float[] data) {
        super(matrix);
        this.data = data;
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
    protected V createSharingVector(int size, int offset, int stride, Orientation orientation) {
//        return new DenseFloatVector(data, size, offset, stride, orientation);
        return null;
    }
}
