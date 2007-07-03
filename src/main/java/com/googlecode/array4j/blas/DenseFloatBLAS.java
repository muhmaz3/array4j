package com.googlecode.array4j.blas;

import org.netlib.blas.Sdot;

import com.googlecode.array4j.DenseFloatVector;

public final class DenseFloatBLAS {
    public static float dot(final DenseFloatVector x, final DenseFloatVector y) {
        if (x.size() != y.size()) {
            throw new IllegalArgumentException();
        }
        return Sdot.sdot(x.size(), x.getData(), x.offset(), x.stride(), y.getData(), y.offset(), y.stride());
    }

    private DenseFloatBLAS() {
    }
}
