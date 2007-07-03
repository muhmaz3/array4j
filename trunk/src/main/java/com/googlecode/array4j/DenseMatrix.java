package com.googlecode.array4j;

public interface DenseMatrix<M extends DenseMatrix<M, V>, V extends DenseVector<V>> extends Matrix<M, V> {
    V createVector(int size, int offset, int stride, Orientation orientation);

    int offset();

    Orientation orientation();
    
    int stride();
}
