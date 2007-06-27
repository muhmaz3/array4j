package com.googlecode.array4j;

public abstract class AbstractDenseMatrix<M extends DenseMatrix, V extends DenseVector> extends AbstractMatrix<M, V>
        implements DenseMatrix<M, V> {
    protected abstract class ToArraysConverter<A> {
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

    protected final transient DenseMatrixSupport<M, V> matrixSupport;

    final int offset;

    final Orientation orientation;

    final int stride;

    public AbstractDenseMatrix(final int rows, final int columns, final int offset, final int stride,
            final Orientation orientation) {
        super(rows, columns);
        this.offset = offset;
        this.stride = stride;
        this.orientation = orientation;
        this.matrixSupport = new DenseMatrixSupport<M, V>(this);
    }

    public final DenseMatrixSupport<M, V> getMatrixSupport() {
        return matrixSupport;
    }

    public final int offset() {
        return offset;
    }

    public final Orientation orientation() {
        return orientation;
    }
    
    public final int stride() {
        return stride;
    }
}
