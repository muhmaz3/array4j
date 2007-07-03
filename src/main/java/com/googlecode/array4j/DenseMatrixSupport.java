package com.googlecode.array4j;

public abstract class DenseMatrixSupport<M extends DenseMatrix<M, V>, V extends DenseVector<V>> {
    /** Stride between elements in a column. */
    protected final int columnStride;

    protected final DenseMatrix<M, V> matrix;

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

    public final void checkColumnIndex(final int column) {
        int columns = matrix.columns();
        if (column < 0 || column >= columns) {
            throw new IndexOutOfBoundsException(
                    String.format("Column index out of bounds [0,%d): %d", columns, column));
        }
    }

    public final void checkRowIndex(final int row) {
        int rows = matrix.rows();
        if (row < 0 || row >= rows) {
            throw new IndexOutOfBoundsException(
                    String.format("Row index out of bounds [0,%d): %d", rows, row));
        }
    }

    public final V column(final int column) {
        checkColumnIndex(column);
        return matrix.createVector(matrix.rows(), columnOffset(column), rowStride, Orientation.COLUMN);
    }

    public final int columnOffset(final int column) {
        return matrix.offset() + column * columnStride;
    }

    public final V row(final int row) {
        checkRowIndex(row);
        return matrix.createVector(matrix.columns(), rowOffset(row), columnStride, Orientation.ROW);
    }

    public final int rowOffset(final int row) {
        return matrix.offset() + row * rowStride;
    }

    public void setColumn(int column, FloatVector<?> columnVector) {
        checkColumnIndex(column);
        setColumnImpl(column, columnVector);
    }

    protected abstract void setColumnImpl(int column, FloatVector<?> columnVector);

    public void setRow(int row, FloatVector<?> rowVector) {
        checkRowIndex(row);
        setRowImpl(row, rowVector);
    }

    protected abstract void setRowImpl(int row, FloatVector<?> rowVector);
}
