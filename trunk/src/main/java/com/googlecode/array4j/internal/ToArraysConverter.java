package com.googlecode.array4j.internal;

import com.googlecode.array4j.DenseMatrix;
import com.googlecode.array4j.Orientation;

public abstract class ToArraysConverter<M extends DenseMatrix<M, ?>, A> {
    private final int offset;

    private final Orientation orientation;

    private final int stride;

    protected ToArraysConverter(final M matrix) {
        this.offset = matrix.offset();
        this.stride = matrix.stride();
        this.orientation = matrix.orientation();
    }

    protected abstract A createArray(int length);

    protected abstract A[] createArrayArray(int length);

    protected abstract void set(int srcPos, A dest, int destPos);

    public final A[] toArrays(final int m, final int n, final boolean rows) {
        A[] arrs = createArrayArray(m);
        for (int i = 0; i < m; i++) {
            arrs[i] = createArray(n);
        }
        for (int i = 0; i < m; i++) {
            A arr = arrs[i];
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
                set(position, arr, j);
            }
        }
        return arrs;
    }
}
