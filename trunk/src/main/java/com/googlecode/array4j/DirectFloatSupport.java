package com.googlecode.array4j;

public final class DirectFloatSupport<M extends DenseMatrix<M, V> & FloatMatrix<M, V>, V extends DenseVector<V> & FloatVector<V>>
        extends DenseMatrixSupport<M, V> {
    public DirectFloatSupport(final M matrix) {
        super(matrix);
    }

    public float[] toArray() {
//        float[] arr = new float[size];
//        if (size == 0) {
//            return arr;
//        }
//        if (stride == 0) {
//            Arrays.fill(arr, matrix.getData().get(0));
//            return arr;
//        }
//        FloatBuffer src = matrix.getData();
//        for (int i = offset, j = 0; j < size; i += stride, j++) {
//            arr[j] = src.get(i);
//        }
//        return arr;
        return null;
    }

    @Override
    protected V createSharingVector(int size, int offset, int stride, Orientation orientation) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void setColumnImpl(int column, FloatVector<?> columnVector) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void setRowImpl(int row, FloatVector<?> rowVector) {
        // TODO Auto-generated method stub
    }
}
