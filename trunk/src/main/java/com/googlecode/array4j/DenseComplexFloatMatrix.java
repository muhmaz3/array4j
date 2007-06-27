package com.googlecode.array4j;

public final class DenseComplexFloatMatrix extends
        AbstractDenseMatrix<DenseComplexFloatMatrix, DenseComplexFloatVector> implements
        ComplexFloatMatrix<DenseComplexFloatMatrix, DenseComplexFloatVector>,
        DenseMatrix<DenseComplexFloatMatrix, DenseComplexFloatVector> {
    private final float[] data;

    private final transient DenseComplexFloatSupport<DenseComplexFloatMatrix, DenseComplexFloatVector> complexSupport;

    public DenseComplexFloatMatrix(float[] data, int rows, int columns, int offset, int stride, Orientation orientation) {
        super(rows, columns, offset, stride, orientation);
        checkArgument(data != null);
        checkArgument(size == 0 || offset + 1 < data.length);
        // TODO might need to add an offset here
        checkArgument(size <= 1 || data.length >= 2 * stride * size);
        this.data = data;
        this.complexSupport = new DenseComplexFloatSupport<DenseComplexFloatMatrix, DenseComplexFloatVector>(this, data);
    }

    public DenseComplexFloatMatrix(final int rows, final int columns) {
        this(rows, columns, Orientation.ROW);
    }

    public DenseComplexFloatMatrix(final int rows, final int columns, final Orientation orientation) {
        this(new float[2 * rows * columns], rows, columns, 0, 1, orientation);
    }

    public ComplexFloat[][] toColumnArrays() {
        return complexSupport.toColumnArrays();
    }

    public ComplexFloat[][] toRowArrays() {
        return complexSupport.toRowArrays();
    }

    public DenseComplexFloatVector row(final int row) {
        return matrixSupport.row(row);
    }

    public DenseComplexFloatVector column(final int column) {
        return matrixSupport.column(column);
    }

    public DenseComplexFloatVector createSharingVector(final int size, final int offset, final int stride,
            final Orientation orientation) {
        return new DenseComplexFloatVector(data, size, offset, stride, orientation);
    }

    public void setColumn(final int column, final ComplexFloatVector columnVector) {
        complexSupport.setColumn(column, columnVector);
    }

    public void setRow(final int row, final ComplexFloatVector rowVector) {
        complexSupport.setRow(row, rowVector);
    }

    public DenseComplexFloatVector createColumnVector() {
        return new DenseComplexFloatVector(rows, Orientation.COLUMN);
    }

    public DenseComplexFloatVector createRowVector() {
        return new DenseComplexFloatVector(columns, Orientation.ROW);
    }

    public ComplexFloat[] toArray() {
        return complexSupport.toArray();
    }

    public DenseComplexFloatMatrix transpose() {
        // interchange columns and rows and change orientation
        return new DenseComplexFloatMatrix(data, columns, rows, offset, stride, orientation.transpose());
    }
}
