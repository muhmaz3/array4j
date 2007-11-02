package com.googlecode.array4j.packed;

import com.googlecode.array4j.Matrix;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.dense.DenseVector;
import java.nio.Buffer;

/**
 * Matrix packed by columns.
 */
public interface PackedMatrix extends Matrix {
    /** Get data buffer. */
    Buffer data();

    boolean isLowerTriangular();

    boolean isSymmetric();

    boolean isUpperTriangular();

    /** Get the storage type (heap or direct). */
    Storage storage();

    DenseVector asVector();
}
