package com.googlecode.array4j.blas;

import com.googlecode.array4j.Orientation;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.dense.DenseMatrix;

public interface BLASPolicy {
    public enum Method {
        F2J, NATIVE
    }

    public static final class BestEffort implements BLASPolicy {
        @Override
        public Method chooseL3Method(final DenseMatrix<?, ?> a, final DenseMatrix<?, ?> b, final DenseMatrix<?, ?> c) {
            for (DenseMatrix<?, ?> arg : new DenseMatrix<?, ?>[]{a, b, c}) {
                // If any matrix is stored in a direct buffer, use native BLAS,
                // which might cause copies of heap buffers when they are
                // pinned.
                if (arg.storage().equals(Storage.DIRECT)) {
                    return Method.NATIVE;
                }
            }
            // F2J can't work with C in row order, so use native BLAS
            if (c.orientation().equals(Orientation.ROW)) {
                return Method.NATIVE;
            }
            // If all the buffers are heap buffers, use F2J'ed BLAS.
            return Method.F2J;
        }
    }

    public static final class AlwaysNative implements BLASPolicy {
        @Override
        public Method chooseL3Method(final DenseMatrix<?, ?> a, final DenseMatrix<?, ?> b, final DenseMatrix<?, ?> c) {
            return Method.NATIVE;
        }
    }

    public static final class OnlyNative implements BLASPolicy {
        @Override
        public Method chooseL3Method(final DenseMatrix<?, ?> a, final DenseMatrix<?, ?> b, final DenseMatrix<?, ?> c) {
            for (DenseMatrix<?, ?> arg : new DenseMatrix<?, ?>[]{a, b, c}) {
                if (!arg.data().isDirect()) {
                    throw new IllegalArgumentException();
                }
            }
            return Method.NATIVE;
        }
    }

    public static final class OnlyF2J implements BLASPolicy {
        @Override
        public Method chooseL3Method(final DenseMatrix<?, ?> a, final DenseMatrix<?, ?> b, final DenseMatrix<?, ?> c) {
            for (DenseMatrix<?, ?> arg : new DenseMatrix<?, ?>[]{a, b, c}) {
                if (!arg.data().hasArray()) {
                    throw new IllegalArgumentException();
                }
            }
            if (c.orientation().equals(Orientation.ROW)) {
                throw new IllegalArgumentException();
            }
            return Method.F2J;
        }
    }

    Method chooseL3Method(DenseMatrix<?, ?> a, DenseMatrix<?, ?> b, DenseMatrix<?, ?> c);
}
