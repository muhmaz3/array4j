package com.googlecode.array4j.blas;

import com.googlecode.array4j.Order;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.matrix.dense.DenseMatrix;
import com.googlecode.array4j.matrix.dense.DenseVector;

interface BLASPolicy {
    /**
     * Policy to always use native BLAS, copying if necessary.
     */
    public static final class AlwaysNative implements BLASPolicy {
        @Override
        public Method chooseL1Method(final DenseVector x, final DenseVector y) {
            return Method.NATIVE;
        }

        @Override
        public Method chooseL3Method(final DenseMatrix a, final DenseMatrix b, final DenseMatrix c) {
            return Method.NATIVE;
        }

        @Override
        public Method chooseL2Method(final DenseMatrix a, final DenseVector x, final DenseVector y) {
            return Method.NATIVE;
        }
    }

    public static final class BestEffort implements BLASPolicy {
        @Override
        public Method chooseL1Method(final DenseVector x, final DenseVector y) {
            if (x.storage().equals(Storage.DIRECT) || (y != null && y.storage().equals(Storage.DIRECT))) {
                return Method.NATIVE;
            }
            return Method.F2J;
        }

        @Override
        public Method chooseL2Method(final DenseMatrix a, final DenseVector x, final DenseVector y) {
            for (DenseMatrix arg : new DenseMatrix[]{a, x, y}) {
                if (arg != null && arg.storage().equals(Storage.DIRECT)) {
                    return Method.NATIVE;
                }
            }
            if (a.order().equals(Order.ROW)) {
                return Method.NATIVE;
            }
            // If all the buffers are heap buffers, use F2J'ed BLAS.
            return Method.F2J;
        }

        @Override
        public Method chooseL3Method(final DenseMatrix a, final DenseMatrix b, final DenseMatrix c) {
            for (DenseMatrix arg : new DenseMatrix[]{a, b, c}) {
                // If any matrix is stored in a direct buffer, use native BLAS.
                // This might cause copying of heap buffers when they are
                // pinned.
                if (arg != null && arg.storage().equals(Storage.DIRECT)) {
                    return Method.NATIVE;
                }
            }
            // F2J can't work with C in row order, so use native BLAS
            if (c.order().equals(Order.ROW)) {
                return Method.NATIVE;
            }
            // If all the buffers are heap buffers, use F2J'ed BLAS.
            return Method.F2J;
        }
    }

    public enum Method {
        F2J, NATIVE
    }

    public static final class OnlyF2J implements BLASPolicy {
        private static void checkHasArray(final DenseMatrix... args) {
            for (DenseMatrix arg : args) {
                if (arg != null && !arg.data().hasArray()) {
                    throw new IllegalArgumentException();
                }
            }
        }

        @Override
        public Method chooseL1Method(final DenseVector x, final DenseVector y) {
            checkHasArray(x, y);
            return Method.F2J;
        }

        @Override
        public Method chooseL2Method(final DenseMatrix a, final DenseVector x, final DenseVector y) {
            checkHasArray(a, x, y);
            return Method.F2J;
        }

        @Override
        public Method chooseL3Method(final DenseMatrix a, final DenseMatrix b, final DenseMatrix c) {
            checkHasArray(a, b, c);
            if (c.order().equals(Order.ROW)) {
                throw new IllegalArgumentException();
            }
            return Method.F2J;
        }
    }

    public static final class OnlyNative implements BLASPolicy {
        private static void checkDirect(final DenseMatrix... args) {
            for (DenseMatrix arg : args) {
                if (arg != null && !arg.data().isDirect()) {
                    throw new IllegalArgumentException();
                }
            }
        }

        @Override
        public Method chooseL1Method(final DenseVector x, final DenseVector y) {
            checkDirect(x, y);
            return Method.NATIVE;
        }

        @Override
        public Method chooseL2Method(final DenseMatrix a, final DenseVector x, final DenseVector y) {
            checkDirect(a, x, y);
            return Method.NATIVE;
        }

        @Override
        public Method chooseL3Method(final DenseMatrix a, final DenseMatrix b, final DenseMatrix c) {
            checkDirect(a, b, c);
            return Method.NATIVE;
        }
    }

    Method chooseL1Method(DenseVector x, DenseVector y);

    Method chooseL2Method(DenseMatrix a, DenseVector x, DenseVector y);

    Method chooseL3Method(DenseMatrix a, DenseMatrix b, DenseMatrix c);
}
