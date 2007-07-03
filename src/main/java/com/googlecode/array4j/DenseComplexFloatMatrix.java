package com.googlecode.array4j;


public final class DenseComplexFloatMatrix
        extends
        AbstractDenseMatrix<DenseComplexFloatMatrix, DenseComplexFloatVector, DenseComplexFloatSupport<DenseComplexFloatMatrix, DenseComplexFloatVector>, ComplexFloat[]>
        implements ComplexFloatMatrix<DenseComplexFloatMatrix, DenseComplexFloatVector>,
        DenseMatrix<DenseComplexFloatMatrix, DenseComplexFloatVector> {
    private final float[] data;

    public DenseComplexFloatMatrix(float[] data, int rows, int columns, int offset, int stride, Orientation orientation) {
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

    public DenseComplexFloatVector createVector(int size, int offset, int stride, Orientation orientation) {
        // TODO Auto-generated method stub
        return null;
    }

    public void setColumn(int column, ComplexFloatVector<?> columnVector) {
        // TODO Auto-generated method stub
    }

    public void setRow(int row, ComplexFloatVector<?> rowVector) {
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
