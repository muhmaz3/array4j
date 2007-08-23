package com.googlecode.array4j.blas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import com.googlecode.array4j.Orientation;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.dense.DenseMatrix;

public abstract class AbstractDenseBLAS {
    protected enum CblasConstant {
        COL_MAJOR(102),
        CONJ_TRANS(113),
        LEFT(141),
        LOWER(122),
        NO_TRANS(111),
        NONUNIT(131),
        RIGHT(142),
        ROW_MAJOR(101),
        TRANS(112),
        UNIT(132),
        UPPER(121);

        private final int value;

        CblasConstant(final int value) {
            this.value = value;
        }

        public int intValue() {
            return value;
        }
    }

    private static final class DummyLock implements Lock {
        @Override
        public void lock() {
        }

        @Override
        public void lockInterruptibly() throws InterruptedException {
        }

        @Override
        public Condition newCondition() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean tryLock() {
            return true;
        }

        @Override
        public boolean tryLock(final long time, final TimeUnit unit) throws InterruptedException {
            return true;
        }

        @Override
        public void unlock() {
        }
    }

    protected enum Transpose {
        CONJUGATE_TRANS {
            @Override
            public String toString() {
                return "C";
            }
        },
        NO_TRANS {
            @Override
            public String toString() {
                return "N";
            }
        },
        TRANS {
            @Override
            public String toString() {
                return "T";
            }
        };
    }

    protected static final Lock NATIVE_BLAS_LOCK;

    static {
        NATIVE_BLAS_LOCK = new DummyLock();
    }

    protected static void checkGemm(final DenseMatrix<?, ?> a, final DenseMatrix<?, ?> b, final DenseMatrix<?, ?> c) {
        a.storage().checkSame(b.storage(), c.storage());
        checkStorageOrientation(a.storage(), c.orientation());
        if (a.rows() != c.rows()) {
            throw new IllegalArgumentException("rows(a) != rows(c)");
        }
        if (a.columns() != b.rows()) {
            throw new IllegalArgumentException("columns(a) != rows(b)");
        }
        if (b.columns() != c.columns()) {
            throw new IllegalArgumentException("columns(b) != columns(c)");
        }
        if (a.stride() != 1 || b.stride() != 1 || c.stride() != 1) {
            throw new IllegalArgumentException("all matrices must have unit stride");
        }
    }

    private static void checkStorageOrientation(final Storage storage, final Orientation orientation) {
        if (storage.equals(Storage.HEAP)) {
            if (!orientation.equals(Orientation.COLUMN)) {
                throw new IllegalArgumentException("unsupported orientation " + orientation);
            }
        }
    }

    protected static Transpose chooseTrans(final DenseMatrix<?, ?> x, final DenseMatrix<?, ?> y) {
        return x.orientation().equals(y.orientation()) ? Transpose.NO_TRANS : Transpose.TRANS;
    }

    protected static int ld(final DenseMatrix<?, ?> x) {
        if (Orientation.COLUMN.equals(x.orientation())) {
            return Math.max(1, x.rows());
        } else {
            return Math.max(1, x.columns());
        }
    }

    protected static CblasConstant orderForOrientation(final Orientation orientation) {
        if (Orientation.ROW.equals(orientation)) {
            return CblasConstant.ROW_MAJOR;
        } else {
            return CblasConstant.COL_MAJOR;
        }
    }

    protected static CblasConstant transForTranspose(final Transpose trans) {
        switch (trans) {
        case NO_TRANS:
            return CblasConstant.NO_TRANS;
        case TRANS:
            return CblasConstant.TRANS;
        case CONJUGATE_TRANS:
            return CblasConstant.CONJ_TRANS;
        default:
            throw new AssertionError();
        }
    }
}
