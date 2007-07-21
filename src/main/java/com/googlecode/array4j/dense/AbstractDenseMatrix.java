package com.googlecode.array4j.dense;

import com.googlecode.array4j.AbstractMatrix;
import com.googlecode.array4j.Matrix;
import com.googlecode.array4j.Orientation;
import com.googlecode.array4j.Vector;

public abstract class AbstractDenseMatrix<M extends Matrix<M, V>, V extends Vector<V>, T> extends AbstractMatrix<M, V>
        implements Matrix<M, V> {
    /** Stride between elements in a column. */
    protected final int columnStride;

    protected final int elementSize;

    protected final int offset;

    protected final Orientation orientation;

    /** Stride between elements in a row. */
    protected final int rowStride;

    protected final int stride;

    public AbstractDenseMatrix(final int elementSize, final int rows, final int columns, final int offset,
            final int stride, final Orientation orientation) {
        super(rows, columns);
        this.elementSize = elementSize;
        this.offset = offset;
        this.stride = stride;
        this.orientation = orientation;
        if (orientation.equals(Orientation.ROW)) {
            this.rowStride = elementSize * stride * columns;
            this.columnStride = elementSize * stride;
        } else {
            this.rowStride = elementSize * stride;
            this.columnStride = elementSize * stride * rows;
        }
    }

    /**
     * Calculate the offset of the beginning of the specified column.
     */
    protected final int columnOffset(final int column) {
        return offset + column * columnStride;
    }

    protected abstract T createArray(int length);

    protected abstract T[] createArrayArray(int length);

    protected final int elementOffset(final int index) {
        return offset + index * elementSize * stride;
    }

    protected final int elementOffset(final int row, final int column) {
        checkRowIndex(row);
        checkColumnIndex(column);
        return rowOffset(row) + column * columnStride;
    }

    public final Orientation orientation() {
        return orientation;
    }

    /**
     * Calculate the offset of the beginning of the specified row.
     */
    protected final int rowOffset(final int row) {
        return offset + row * rowStride;
    }

    // TODO give this method a better name
    protected abstract void setFrom(T dest, int destPos, int srcPos);

    // TODO add support for elementSize
    public final T toArray() {
        final T arr = createArray(size);
        if (size == 0) {
            return arr;
        }
        if (stride == 0) {
            // Arrays.fill(arr, data[offset]);
            // return arr;
            throw new UnsupportedOperationException();
        }

        for (int i = offset, j = 0; j < size; i += stride, j++) {
            setFrom(arr, j, i);
        }
        return arr;
    }

    // TODO add support for elementSize
    private T[] toArrays(final int m, final int n, final boolean rows) {
        final T[] arrs = createArrayArray(m);
        for (int i = 0; i < m; i++) {
            arrs[i] = createArray(n);
        }
        for (int i = 0; i < m; i++) {
            final T arr = arrs[i];
            for (int j = 0; j < n; j++) {
                int position = offset;
                if (rows) {
                    if (orientation.equals(Orientation.ROW)) {
                        position += (i * n + j) * stride;
                    } else {
                        position += (j * m + i) * stride;
                    }
                } else {
                    if (orientation.equals(Orientation.COLUMN)) {
                        position += (i * n + j) * stride;
                    } else {
                        position += (j * m + i) * stride;
                    }
                }
                setFrom(arr, j, position);
            }
        }
        return arrs;
    }

    public final T[] toColumnArrays() {
        return toArrays(columns, rows, false);
    }

    public final T[] toRowArrays() {
        return toArrays(rows, columns, true);
    }
}
