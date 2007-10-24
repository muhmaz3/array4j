package com.googlecode.array4j.packed;

import com.googlecode.array4j.AbstractMatrix;
import com.googlecode.array4j.dense.DenseVector;

public abstract class AbstractPackedMatrix<V extends DenseVector> extends AbstractMatrix<V> implements PackedMatrix {
    protected enum PackedType {
        LOWER_TRIANGULAR {
            @Override
            public PackedType transpose() {
                return UPPER_TRIANGULAR;
            }
        },
        SYMMETRIC {
            @Override
            public PackedType transpose() {
                return SYMMETRIC;
            }
        },
        UPPER_TRIANGULAR {
            @Override
            public PackedType transpose() {
                return LOWER_TRIANGULAR;
            }
        };

        public abstract PackedType transpose();
    }

    protected final PackedType packedType;

    public AbstractPackedMatrix(final int rows, final int columns, final PackedType packedType) {
        super(null, rows, columns);
        // TODO can possibly relax this restriction in some cases?
        if (rows != columns) {
            throw new IllegalArgumentException();
        }
        this.packedType = packedType;
    }

    protected final void checkCanSet(final int row, final int column) {
        if (!nonZeroElement(row, column)) {
            throw new IllegalArgumentException();
        }
    }

    protected final int elementOffset(final int m, final int n) {
        checkRowIndex(m);
        checkColumnIndex(n);
        final int i, j;
        if (m <= n) {
            i = m;
            j = n;
        } else {
            j = m;
            i = n;
        }
        if (packedType.equals(PackedType.LOWER_TRIANGULAR)) {
            return i + (2 * columns - (j + 1)) * j / 2;
        } else {
            return i + (j + 1) * j / 2;
        }
    }

    protected final int getBufferSize() {
        return rows * (rows + 1) / 2;
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

    protected final boolean nonZeroElement(final int row, final int column) {
        if (packedType.equals(PackedType.UPPER_TRIANGULAR)) {
            return row <= column;
        } else if (packedType.equals(PackedType.LOWER_TRIANGULAR)) {
            return row >= column;
        } else {
            return true;
        }
    }
}
