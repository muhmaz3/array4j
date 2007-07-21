package com.googlecode.array4j;

public final class DenseComplexFloatMatrix
        extends
        AbstractDenseMatrix<DenseComplexFloatMatrix, DenseComplexFloatVector, DenseComplexFloatSupport<DenseComplexFloatMatrix, DenseComplexFloatVector>, ComplexFloat[]>
        implements ComplexFloatMatrix<DenseComplexFloatMatrix, DenseComplexFloatVector>,
        DenseMatrix<DenseComplexFloatMatrix, DenseComplexFloatVector> {
    private final float[] data;

    public DenseComplexFloatMatrix(final float[] data, final int rows, final int columns, final int offset,
            final int stride, final Orientation orientation) {
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

    @Override
    public DenseComplexFloatVector asVector() {
        throw new UnsupportedOperationException();
    }

    public DenseComplexFloatVector createColumnVector() {
        throw new UnsupportedOperationException();
    }

    public DenseComplexFloatVector createRowVector() {
        throw new UnsupportedOperationException();
    }

    public DenseComplexFloatVector createVector(final int size, final int offset, final int stride,
            final Orientation orientation) {
        throw new UnsupportedOperationException();
    }

    public void setColumn(final int column, final ComplexFloatVector<?> columnVector) {
        throw new UnsupportedOperationException();
    }

    public void setRow(final int row, final ComplexFloatVector<?> rowVector) {
        throw new UnsupportedOperationException();
    }

    public ComplexFloat[] toArray() {
        throw new UnsupportedOperationException();
    }

    public DenseComplexFloatMatrix transpose() {
        throw new UnsupportedOperationException();
    }
}
