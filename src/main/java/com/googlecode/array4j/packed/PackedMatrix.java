package com.googlecode.array4j.packed;

import com.googlecode.array4j.Matrix;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.dense.DenseVector;
import java.nio.Buffer;

/**
 * Matrix packed by columns.
 */
public interface PackedMatrix<M extends PackedMatrix<M, V>, V extends DenseVector<V>> extends Matrix<M, V> {
    Buffer data();

    boolean isLowerTriangular();

    boolean isSymmetric();

    boolean isUpperTriangular();

    Storage storage();
}
