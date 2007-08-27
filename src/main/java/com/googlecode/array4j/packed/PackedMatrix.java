package com.googlecode.array4j.packed;

import com.googlecode.array4j.Matrix;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.dense.DenseVector;

/**
 * Matrix packed by columns.
 */
public interface PackedMatrix<M extends PackedMatrix<M, V>, V extends DenseVector<V>> extends Matrix<M, V> {
    Storage storage();

    boolean isSymmetric();

    boolean isUpperTriangular();

    boolean isLowerTriangular();
}
