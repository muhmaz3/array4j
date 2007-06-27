package com.googlecode.array4j;

public abstract class AbstractDenseVector<V extends DenseVector, ValueArray> extends
        AbstractDenseMatrix<V, V, ValueArray> implements DenseVector<V>, Vector<V> {
    final Orientation orientation;

    public AbstractDenseVector(final int size, final int offset, final int stride, final Orientation orientation) {
        super(rows(size, orientation), columns(size, orientation), offset, stride, orientation);
        this.orientation = orientation;
    }

    private static int rows(final int size, final Orientation orientation) {
        checkArgument(size >= 0);
        if (orientation.equals(Orientation.COLUMN)) {
            return size;
        } else {
            if (size == 0) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    private static int columns(final int size, final Orientation orientation) {
        checkArgument(size >= 0);
        if (orientation.equals(Orientation.ROW)) {
            return size;
        } else {
            if (size == 0) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    public final boolean isColumnVector() {
        return orientation.equals(Orientation.COLUMN);
    }

    public final boolean isRowVector() {
        return orientation.equals(Orientation.ROW);
    }
}
