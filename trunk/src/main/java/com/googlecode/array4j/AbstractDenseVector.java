package com.googlecode.array4j;

import java.nio.DoubleBuffer;

public abstract class AbstractDenseVector<E extends AbstractDenseVector>
        extends AbstractDenseMatrix<E> implements Vector<E> {
    protected AbstractDenseVector(final int rows, final int columns) {
        super(rows, columns);
        checkRowsColumns(rows, columns);
    }

    protected AbstractDenseVector(final double[] values, final int rows, final int columns) {
        super(values, rows, columns);
        checkRowsColumns(rows, columns);
    }

    protected AbstractDenseVector(final DoubleBuffer buffer, final int[] shape) {
        super(buffer, shape);
    }

    private static void checkRowsColumns(final int rows, final int columns) {
        if (rows > 1 && columns > 1) {
            throw new IllegalArgumentException("invalid shape for vector");
        }
    }

    public final double get(final int index) {
        return getRows() == 1 ? get(0, index) : get(index, 0);
    }

    public final int getDimension() {
        return getRows() == 1 ? getColumns() : getRows();
    }

    public final int length() {
        return getDimension();
    }
}
