package com.googlecode.array4j;

public interface DenseMatrix<M extends DenseMatrix, V extends DenseVector> extends Matrix<M, V> {
    int offset();

    int stride();

    Orientation orientation();

    V createSharingVector(int size, int offset, int stride, Orientation orientation);

    DenseMatrixSupport<M, V> getMatrixSupport();
}
