package com.googlecode.array4j;

public final class DenseComplexFloatSupport<M extends DenseMatrix<M, V> & ComplexFloatMatrix<M, V>, V extends DenseVector<V> & ComplexFloatVector<V>>
        extends DenseMatrixSupport<M, V, ComplexFloat[]> {
    public DenseComplexFloatSupport(final M matrix, final float[] data) {
        super(matrix);
    }

    @Override
    protected void setColumnImpl(final int column, final FloatVector<?> columnVector) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void setRowImpl(final int row, final FloatVector<?> rowVector) {
        // TODO Auto-generated method stub

    }

    @Override
    protected ComplexFloat[][] toArrays(final int m, final int n, final boolean rows) {
        // TODO Auto-generated method stub
        return null;
    }
}
