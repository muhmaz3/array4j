package com.googlecode.array4j;

public final class DenseMatrixSupport<M extends DenseMatrix, V extends DenseVector> {
    /** Stride between elements in a column. */
    protected final int columnStride;

    private final DenseMatrix<M, V> matrix;

    /** Stride between elements in a row. */
    protected final int rowStride;

    public DenseMatrixSupport(final DenseMatrix<M, V> matrix) {
        this(matrix, 1);
    }

    public DenseMatrixSupport(final DenseMatrix<M, V> matrix, final int elementSize) {
        this.matrix = matrix;
        if (matrix.orientation().equals(Orientation.ROW)) {
            this.rowStride = elementSize * matrix.stride() * matrix.columns();
            this.columnStride = elementSize * matrix.stride();
        } else {
            this.rowStride = elementSize * matrix.stride();
            this.columnStride = elementSize * matrix.stride() * matrix.rows();
        }
    }

    public void checkColumnIndex(final int column) {
        int columns = matrix.columns();
        if (column < 0 || column >= columns) {
            throw new IndexOutOfBoundsException(
                    String.format("Column index out of bounds [0,%d): %d", columns, column));
        }
    }

    public void checkRowIndex(final int row) {
        int rows = matrix.rows();
        if (row < 0 || row >= rows) {
            throw new IndexOutOfBoundsException(
                    String.format("Row index out of bounds [0,%d): %d", rows, row));
        }
    }

    public V column(final int column) {
        checkColumnIndex(column);
        return matrix.createSharingVector(matrix.rows(), columnOffset(column), rowStride, Orientation.COLUMN);
    }

    public int columnOffset(final int column) {
        return matrix.offset() + column * columnStride;
    }

    public V row(final int row) {
        checkRowIndex(row);
        return matrix.createSharingVector(matrix.columns(), rowOffset(row), columnStride, Orientation.ROW);
    }

    public int rowOffset(final int row) {
        return matrix.offset() + row * rowStride;
    }
}
