package com.googlecode.array4j.blas;

import org.netlib.blas.Isamax;
import org.netlib.blas.Saxpy;
import org.netlib.blas.Scopy;
import org.netlib.blas.Sdot;
import org.netlib.blas.Sscal;

import com.googlecode.array4j.DenseFloatMatrix;
import com.googlecode.array4j.DenseFloatVector;

public final class DenseFloatBLAS implements FloatBLAS<DenseFloatMatrix, DenseFloatVector> {
    public static final FloatBLAS<DenseFloatMatrix, DenseFloatVector> INSTANCE = new DenseFloatBLAS();

    private DenseFloatBLAS() {
    }

    @Override
    public void axpy(final float value, final DenseFloatVector x, final DenseFloatVector y) {
        Saxpy.saxpy(x.size(), value, x.data(), x.offset(), x.stride(), y.data(), y.offset(), y.stride());
    }

    @Override
    public void copy(final DenseFloatVector x, final DenseFloatVector y) {
        if (x.size() != y.size()) {
            throw new IllegalArgumentException();
        }
        Scopy.scopy(x.size(), x.data(), x.offset(), x.stride(), y.data(), y.offset(), y.stride());
    }

    public float dot(final DenseFloatVector x, final DenseFloatVector y) {
        if (x.size() != y.size()) {
            throw new IllegalArgumentException();
        }
        return Sdot.sdot(x.size(), x.data(), x.offset(), x.stride(), y.data(), y.offset(), y.stride());
    }

    @Override
    public int iamax(final DenseFloatVector x) {
        return Isamax.isamax(x.size(), x.data(), x.offset(), x.stride());
    }

    @Override
    public void scal(final float value, final DenseFloatVector x) {
        Sscal.sscal(x.size(), value, x.data(), x.offset(), x.stride());
    }
}
