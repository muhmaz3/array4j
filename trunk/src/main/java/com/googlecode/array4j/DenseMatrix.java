package com.googlecode.array4j;

public interface DenseMatrix<M extends DenseMatrix<M, V>, V extends DenseVector<V>> extends Matrix<M, V> {
    int offset();

    int stride();

    Orientation orientation();
}
