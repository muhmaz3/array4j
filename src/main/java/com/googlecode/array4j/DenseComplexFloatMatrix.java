package com.googlecode.array4j;


public final class DenseComplexFloatMatrix
        extends
        AbstractDenseMatrix<DenseComplexFloatMatrix, DenseComplexFloatVector, DenseComplexFloatSupport<DenseComplexFloatMatrix, DenseComplexFloatVector>, ComplexFloat[]>
        implements ComplexFloatMatrix<DenseComplexFloatMatrix, DenseComplexFloatVector>,
        DenseMatrix<DenseComplexFloatMatrix, DenseComplexFloatVector> {
    private final float[] data;

    public DenseComplexFloatMatrix(final float[] data, final int rows, final int columns, final int offset, final int stride, final Orientation orientation) {
        super(rows, columns, offset, stride, orientation);
        checkArgument(data != null);
        checkArgument(size == 0 || offset + 1 < data.length);
        // TODO might need to add an offset here
        checkArgument(size <= 1 || data.length >= 2 * stride * size);
        this.data = data;
        this.support = new DenseComplexFloatSupport<DenseComplexFloatMatrix, DenseComplexFloatVector>(this, data);
    }

    public DenseComplexFloatMatrix(final int rows, final int columns) {
        this(rows, columns, Orientation.DEFAULT);
    }

    public DenseComplexFloatMatrix(final int rows, final int columns, final Orientation orientation) {
        this(new float[2 * rows * columns], rows, columns, 0, 1, orientation);
    }

    public DenseComplexFloatVector createColumnVector() {
        // TODO Auto-generated method stub
        return null;
    }

    public DenseComplexFloatVector createRowVector() {
        // TODO Auto-generated method stub
        return null;
    }

    public DenseComplexFloatVector createVector(final int size, final int offset, final int stride, final Orientation orientation) {
        // TODO Auto-generated method stub
        return null;
    }

    public void setColumn(final int column, final ComplexFloatVector<?> columnVector) {
        // TODO Auto-generated method stub
    }

    public void setRow(final int row, final ComplexFloatVector<?> rowVector) {
        // TODO Auto-generated method stub

    }

    public ComplexFloat[] toArray() {
        // TODO Auto-generated method stub
        return null;
    }

    public DenseComplexFloatMatrix transpose() {
        // TODO Auto-generated method stub
        return null;
    }
}
