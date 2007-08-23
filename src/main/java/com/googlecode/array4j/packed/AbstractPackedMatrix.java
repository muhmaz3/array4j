package com.googlecode.array4j.packed;

import com.googlecode.array4j.AbstractMatrix;
import com.googlecode.array4j.dense.DenseVector;

public abstract class AbstractPackedMatrix<M extends PackedMatrix<M, V>, V extends DenseVector<V>> extends
        AbstractMatrix<M, V> implements PackedMatrix<M, V> {
    private final UpLo uplo;

    public AbstractPackedMatrix(final int rows, final int columns) {
        super(rows, columns);
        this.uplo = UpLo.UP;
    }

    public final UpLo getUpLo() {
        return uplo;
    }

    protected final int elementOffset(final int rows, final int columns) {
        final int i, j;
        if (rows <= columns) {
            i = rows;
            j = columns;
        } else {
            j = rows;
            i = columns;
        }
        if (UpLo.UP.equals(uplo)) {
            return i + (j + 1) * j / 2;
        } else {
            return i + (2 * columns - (j + 1)) * j / 2;
        }
    }

    public final int offset() {
        return 0;
    }
}
