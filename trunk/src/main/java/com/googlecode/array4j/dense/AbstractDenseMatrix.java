package com.googlecode.array4j.dense;

import com.googlecode.array4j.AbstractMatrix;
import com.googlecode.array4j.Orientation;
import com.googlecode.array4j.util.BufferUtils;
import java.nio.Buffer;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * Abstract base class for dense (full) matrices.
 */
public abstract class AbstractDenseMatrix<M extends DenseMatrix<M, V>, V extends DenseVector<V>, T> extends
        AbstractMatrix<M, V> implements DenseMatrix<M, V> {
    protected final void checkData(final Buffer data) {
        int start = offset * elementSizeBytes;
        int end = start + (Math.max(0, length - 1) * stride + (length > 0 ? 1 : 0)) * elementSizeBytes;
        int capacity = BufferUtils.getBytesCapacity(data);
        if (start > capacity || end > capacity) {
            throw new IllegalArgumentException();
        }
    }

    /** Stride between elements in a column. */
    protected final int columnStride;

    /** Size of each element in buffer positions. */
    protected final int elementSize;

    /** Size of each element in bytes. */
    protected final int elementSizeBytes;

    /** Offset in storage where matrix data begins. */
    protected final int offset;

    protected final Orientation orientation;

    /** Stride between elements in a row. */
    protected final int rowStride;

    /** Stride between elements. */
    protected final int stride;

    public AbstractDenseMatrix(final AbstractDenseMatrix<?, ?, ?> base, final int elementSize,
            final int elementSizeBytes, final int rows, final int columns, final int offset, final int stride,
            final Orientation orientation) {
        super(base, rows, columns);
        this.elementSize = elementSize;
        this.elementSizeBytes = elementSizeBytes;
        this.offset = offset;
        this.stride = stride;
        this.orientation = orientation;
        if (orientation.equals(Orientation.ROW)) {
            this.rowStride = stride * columns;
            this.columnStride = stride;
        } else {
            this.rowStride = stride;
            this.columnStride = stride * rows;
        }
    }

    /**
     * Calculate the offset of the beginning of the specified column.
     */
    protected final int columnOffset(final int column) {
        checkColumnIndex(column);
        if (length > 0) {
            return offset + column * elementSize * columnStride;
        } else {
            return offset;
        }
    }

    protected abstract T createArray(int length);

    protected abstract T[] createArrayArray(int length);

    protected final int elementOffset(final int index) {
        checkIndex(index);
        if (length > 0) {
            return offset + index * elementSize * stride;
        } else {
            return offset;
        }
    }

    protected final int elementOffset(final int row, final int column) {
        checkRowIndex(row);
        checkColumnIndex(column);
        if (length > 0) {
            return rowOffset(row) + column * elementSize * columnStride;
        } else {
            return offset;
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof AbstractDenseMatrix)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        AbstractDenseMatrix<?, ?, ?> other = (AbstractDenseMatrix<?, ?, ?>) obj;
        return new EqualsBuilder().appendSuper(super.equals(obj)).append(elementSize, other.elementSize).append(
                orientation, other.orientation).isEquals();
    }

    // TODO give this method a better name
    protected abstract void fillFrom(T dest, int srcPos);

    public final int offset() {
        return offset;
    }

    public final Orientation orientation() {
        return orientation;
    }

    /**
     * Calculate the offset of the beginning of the specified row.
     */
    protected final int rowOffset(final int row) {
        checkRowIndex(row);
        if (length > 0) {
            return offset + row * elementSize * rowStride;
        } else {
            return offset;
        }
    }

    // TODO give this method a better name
    protected abstract void setFrom(T dest, int destPos, int srcPos);

    public final int stride() {
        return stride;
    }

    public final T toArray() {
        final T arr = createArray(length);
        if (length == 0) {
            return arr;
        }
        if (stride == 0) {
            fillFrom(arr, offset);
            return arr;
        }
        for (int i = offset, j = 0; j < length; i += elementSize * stride, j++) {
            setFrom(arr, j, i);
        }
        return arr;
    }

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
                        position += (i * n + j) * elementSize * stride;
                    } else {
                        position += (j * m + i) * elementSize * stride;
                    }
                } else {
                    if (orientation.equals(Orientation.COLUMN)) {
                        position += (i * n + j) * elementSize * stride;
                    } else {
                        position += (j * m + i) * elementSize * stride;
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
