package com.googlecode.array4j.packed;

import com.googlecode.array4j.AbstractMatrix;
import com.googlecode.array4j.dense.DenseVector;

public abstract class AbstractPackedMatrix<M extends PackedMatrix<M, V>, V extends DenseVector<V>> extends
        AbstractMatrix<M, V> implements PackedMatrix<M, V> {
    protected enum PackedType {
        SYMMETRIC,
        UPPER_TRIANGULAR,
        LOWER_TRIANGULAR
    }

    private enum UpLo {
        UP, LO;
    }

    private final PackedType packedType;

    private final UpLo uplo;

    public AbstractPackedMatrix(final int rows, final int columns, final PackedType packedType) {
        super(rows, columns);
        if (packedType.equals(PackedType.SYMMETRIC)) {
            if (rows != columns) {
                throw new IllegalArgumentException();
            }
        }
        this.packedType = packedType;
        if (packedType.equals(PackedType.LOWER_TRIANGULAR)) {
            this.uplo = UpLo.LO;
        } else {
            this.uplo = UpLo.UP;
        }
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
        if (uplo.equals(UpLo.UP)) {
            return i + (j + 1) * j / 2;
        } else {
            return i + (2 * columns - (j + 1)) * j / 2;
        }
    }

    public final int offset() {
        return 0;
    }

    @Override
    public final boolean isLowerTriangular() {
        return packedType.equals(PackedType.LOWER_TRIANGULAR);
    }

    @Override
    public final boolean isSymmetric() {
        return packedType.equals(PackedType.SYMMETRIC);
    }

    @Override
    public final boolean isUpperTriangular() {
        return packedType.equals(PackedType.UPPER_TRIANGULAR);
    }
}
