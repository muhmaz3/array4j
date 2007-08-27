package com.googlecode.array4j.blas;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Iterator;

import org.junit.Test;

import com.googlecode.array4j.Orientation;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.dense.DenseMatrix;
import com.googlecode.array4j.dense.FloatDenseMatrix;

public final class FloatDenseBLASTest {
    private static void checkMatrix(final FloatDenseMatrix expected, final FloatDenseMatrix actual) {
        assertEquals(expected.rows(), actual.rows());
        assertEquals(expected.columns(), actual.columns());
        for (int i = 0; i < actual.rows(); i++) {
            for (int j = 0; j < actual.columns(); j++) {
                // compare relative to the reference value because the absolute
                // values can become large
                float refij = expected.get(i, j);
                assertEquals(refij, actual.get(i, j), refij * 1.0e-6f);
            }
        }
    }

    private static boolean checkMatrix2(final FloatDenseMatrix expected, final FloatDenseMatrix actual) {
        assertEquals(expected.rows(), actual.rows());
        assertEquals(expected.columns(), actual.columns());
        for (int i = 0; i < actual.rows(); i++) {
            for (int j = 0; j < actual.columns(); j++) {
                float refij = expected.get(i, j);
                if (Math.abs(refij - actual.get(i, j)) / refij * 1.0e-6 > 1.0e-9) {
                    return false;
                }
            }
        }
        return true;
    }

    private static final class Permutations<E> implements Iterable<E[]> {
        private final int length;

        private final E[] values;

        private final int[] idx;

        private boolean done;

        public Permutations(final int length, final E... values) {
            if (length < 1) {
                throw new IllegalArgumentException();
            }
            this.length = length;
            this.values = values;
            this.idx = new int[length];
            this.done = false;
        }

        private void increment(final int i) {
            if (idx[i] < values.length - 1) {
                idx[i]++;
            } else {
                idx[i] = 0;
                if (i < idx.length - 1) {
                    increment(i + 1);
                } else {
                    done = true;
                }
            }
        }

        @Override
        public Iterator<E[]> iterator() {
            return new Iterator<E[]>() {
                @Override
                public boolean hasNext() {
                    return !done;
                }

                @SuppressWarnings("unchecked")
                @Override
                public E[] next() {
                    E[] perm = (E[]) Arrays.copyOf(values, length, values.getClass());
                    for (int i = 0; i < perm.length; i++) {
                        perm[i] = values[idx[i]];
                    }
                    increment(0);
                    return perm;
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }

    private static void populateMatrix(final FloatDenseMatrix x) {
        for (int i = 0, k = 1; i < x.rows(); i++) {
            for (int j = 0; j < x.columns(); j++, k++) {
                x.set(i, j, (float) k);
            }
        }
    }

    protected static int ld(final DenseMatrix<?, ?> x) {
        if (x.orientation().equals(Orientation.COLUMN)) {
            return Math.max(1, x.rows());
        } else {
            return Math.max(1, x.columns());
        }
    }

    protected static String chooseTranspose(final DenseMatrix<?, ?> x, final DenseMatrix<?, ?> y) {
        return x.orientation().equals(y.orientation()) ? "N" : "T";
    }

    protected static String chooseTranspose(final DenseMatrix<?, ?> a, final DenseMatrix<?, ?> b,
            final DenseMatrix<?, ?> c) {
        if (a.orientation().equals(b.orientation())) {
            return a.orientation().equals(c.orientation()) ? "N" : "T";
        } else {
            return a.orientation().equals(c.orientation()) ? "N" : "T";
        }
    }

//    private static boolean doit(FloatDenseMatrix a, FloatDenseMatrix b, FloatDenseMatrix c1, FloatDenseMatrix c2) {
//        final float alpha = 1.0f;
//        final float beta = 1.0f;
//        populateMatrix(c1);
//        gemm(alpha, a, b, beta, c1);
////        System.out.println(c1);
//        boolean success = false;
//        for (String transa : new String[]{"N", "T"}) {
//            for (String transb : new String[]{"N", "T"}) {
//                for (int lda : new int[]{Math.max(1, a.rows()), Math.max(1, a.columns())}) {
//                    for (int ldb : new int[]{Math.max(1, b.rows()), Math.max(1, b.columns())}) {
//                        for (int ldc : new int[]{Math.max(1, c2.rows()), Math.max(1, c2.columns())}) {
//                            try {
//                                populateMatrix(c2);
//                                FloatDenseBLAS.DEFAULT.gemm(transa, transb, lda, ldb, ldc, alpha, a, b, beta, c2);
//                            } catch (RuntimeException e) {
//                                continue;
//                            }
//                            if (checkMatrix2(c1, c2)) {
//                                System.out.println(transa + " " + transb + " " + lda + " " + ldb + " " + ldc);
////                                System.out.println("SUCCESS!");
//                                success = true;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        System.out.println(success);
//        System.out.println(a.orientation() + " " + a.rows() + " x " + a.columns());
//        System.out.println(b.orientation() + " " + b.rows() + " x " + b.columns());
//        System.out.println(c2.orientation() + " " + c2.rows() + " x " + c2.columns());
//        System.out.println("-----------------------------------");
//        return success;
//    }

    @Test
    public void testGemm() {
        final float alpha = 1.0f;
        final float beta = 1.0f;
        for (Orientation[] o : new Permutations<Orientation>(3, Orientation.values())) {
            for (Storage[] s : new Permutations<Storage>(3, Storage.values())) {
                for (int m = 0; m < 5; m++) {
                    for (int n = 0; n < 5; n++) {
                        for (int k = 0; k < 5; k++) {
                            FloatDenseMatrix a = new FloatDenseMatrix(m, n, o[0], s[0]);
                            FloatDenseMatrix b = new FloatDenseMatrix(n, k, o[1], s[1]);
                            FloatDenseMatrix c1 = new FloatDenseMatrix(m, k, o[2], s[2]);
                            FloatDenseMatrix c2 = new FloatDenseMatrix(m, k, o[2], s[2]);
                            populateMatrix(a);
                            populateMatrix(b);
                            populateMatrix(c1);
                            gemm(alpha, a, b, beta, c1);
                            populateMatrix(c2);
                            FloatDenseBLAS.DEFAULT.gemm(alpha, a, b, beta, c2);
                            checkMatrix(c1, c2);
                        }
                    }
                }
            }
        }
    }

    private static void gemm(final float alpha, final FloatDenseMatrix a, final FloatDenseMatrix b, final float beta,
            final FloatDenseMatrix c) {
        assertEquals(a.columns(), b.rows());
        assertEquals(a.rows(), c.rows());
        assertEquals(b.columns(), c.columns());
        for (int i = 0; i < a.rows(); i++) {
            for (int j = 0; j < b.columns(); j++) {
                float value = beta * c.get(i, j);
                for (int k = 0; k < a.columns(); k++) {
                    value += alpha * a.get(i, k) * b.get(k, j);
                }
                c.set(i, j, value);
            }
        }
    }
}
