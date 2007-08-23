package com.googlecode.array4j;

public interface ComplexMatrix<M extends ComplexMatrix<M, V>, V extends ComplexVector<V>> extends Matrix<M, V> {
    void conj();

    M conjTranpose();
}
