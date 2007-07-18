package com.googlecode.array4j.blas;

import org.netlib.blas.Sdot;

import com.googlecode.array4j.DenseFloatMatrix;
import com.googlecode.array4j.DenseFloatVector;

public final class DenseFloatBLAS implements FloatBLAS<DenseFloatMatrix, DenseFloatVector> {
    public static final DenseFloatBLAS INSTANCE = new DenseFloatBLAS();

    private DenseFloatBLAS() {
    }

    public float dot(final DenseFloatVector x, final DenseFloatVector y) {
        if (x.size() != y.size()) {
            throw new IllegalArgumentException();
        }
        return Sdot.sdot(x.size(), x.getData(), x.offset(), x.stride(), y.getData(), y.offset(), y.stride());
    }

    @Override
    public float sum(final DenseFloatVector x) {
        return Sdot.sdot(x.size(), x.getData(), x.offset(), x.stride(), new float[]{1.0f}, 0, 0);
    }
}
