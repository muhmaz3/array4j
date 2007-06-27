package com.googlecode.array4j;

import org.netlib.blas.Sdot;

public final class DenseFloatBLAS {
    private DenseFloatBLAS() {
    }

    public static float dot(final DenseFloatVector x, final DenseFloatVector y) {
        if (x.size != y.size) {
            throw new IllegalArgumentException();
        }
        return Sdot.sdot(x.size, x.data, x.offset, x.stride, y.data, y.offset, y.stride);
    }
}
