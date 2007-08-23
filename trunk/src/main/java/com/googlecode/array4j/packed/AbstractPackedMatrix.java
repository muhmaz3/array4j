package com.googlecode.array4j.packed;

import com.googlecode.array4j.AbstractMatrix;
import com.googlecode.array4j.Matrix;
import com.googlecode.array4j.Vector;

public abstract class AbstractPackedMatrix<M extends Matrix<M, V>, V extends Vector<V>> extends AbstractMatrix<M, V> {
    private enum UpLo {
        LO,
        UP
    }

    private final UpLo uplo;

    public AbstractPackedMatrix(final int rows, final int columns) {
        super(rows, columns);
        this.uplo = UpLo.UP;
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
}
