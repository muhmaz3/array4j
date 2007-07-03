package com.googlecode.array4j;

public final class DenseComplexFloatSupport<M extends DenseMatrix<M, V> & ComplexFloatMatrix<M, V>, V extends DenseVector<V> & ComplexFloatVector<V>>
        extends DenseMatrixSupport<M, V, ComplexFloat[]> {
    public DenseComplexFloatSupport(final M matrix, final float[] data) {
        super(matrix);
    }

    @Override
    protected void setColumnImpl(int column, FloatVector<?> columnVector) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void setRowImpl(int row, FloatVector<?> rowVector) {
        // TODO Auto-generated method stub

    }

    @Override
    protected ComplexFloat[][] toArrays(int m, int n, boolean rows) {
        // TODO Auto-generated method stub
        return null;
    }
}
