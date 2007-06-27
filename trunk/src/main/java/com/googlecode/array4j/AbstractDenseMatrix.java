package com.googlecode.array4j;

import com.googlecode.array4j.internal.ToArraysConverter;

public abstract class AbstractDenseMatrix<M extends DenseMatrix, V extends DenseVector, ValueArray> extends
        AbstractMatrix<M, V> implements DenseMatrix<M, V> {
    private final transient ToArraysConverter<M, ValueArray> arraysConverter;

    protected final transient DenseMatrixSupport<M, V> matrixSupport;

    final int offset;

    final Orientation orientation;

    final int stride;

    public AbstractDenseMatrix(final int rows, final int columns, final int offset, final int stride,
            final Orientation orientation) {
        super(rows, columns);
        this.offset = offset;
        this.stride = stride;
        this.orientation = orientation;
        this.matrixSupport = new DenseMatrixSupport<M, V>(this);
        // only create the arrays converter after the other fields have been set
        this.arraysConverter = createArraysConverter();
    }

    protected abstract ToArraysConverter<M, ValueArray> createArraysConverter();

    public final V column(final int column) {
        return matrixSupport.column(column);
    }

    public final DenseMatrixSupport<M, V> getMatrixSupport() {
        return matrixSupport;
    }

    public final int offset() {
        return offset;
    }

    public final Orientation orientation() {
        return orientation;
    }

    public final V row(final int row) {
        return matrixSupport.row(row);
    }

    public final int stride() {
        return stride;
    }

    public final ValueArray[] toColumnArrays() {
        return arraysConverter.toArrays(columns, rows, false);
    }

    public final ValueArray[] toRowArrays() {
        return arraysConverter.toArrays(rows, columns, true);
    }
}
