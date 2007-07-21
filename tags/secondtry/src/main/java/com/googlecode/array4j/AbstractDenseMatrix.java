package com.googlecode.array4j;

public abstract class AbstractDenseMatrix<M extends DenseMatrix<M, V>, V extends DenseVector<V>, S extends DenseMatrixSupport<M, V, ValueArray>, ValueArray>
        extends AbstractMatrix<M, V> implements DenseMatrix<M, V> {
    final int offset;

    final Orientation orientation;

    final int stride;

    protected transient S support;

    public AbstractDenseMatrix(final int rows, final int columns, final int offset, final int stride,
            final Orientation orientation) {
        super(rows, columns);
        this.offset = offset;
        this.stride = stride;
        this.orientation = orientation;
    }

    public final V column(final int column) {
        return support.column(column);
    }

    public final int offset() {
        return offset;
    }

    public final Orientation orientation() {
        return orientation;
    }

    public final V row(final int row) {
        return support.row(row);
    }

    public final int stride() {
        return stride;
    }

    public final ValueArray[] toColumnArrays() {
        return support.toArrays(columns, rows, false);
    }

    public final ValueArray[] toRowArrays() {
        return support.toArrays(rows, columns, true);
    }
}
