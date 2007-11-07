package com.googlecode.array4j.packed;

import com.googlecode.array4j.Matrix;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.dense.DenseVector;
import java.nio.Buffer;

/**
 * Matrix packed by columns.
 */
public interface PackedMatrix extends Matrix {
    DenseVector asVector();

    /** Get data buffer. */
    Buffer data();

    /** Returns <CODE>true</CODE> if the matrix is lower triangular. */
    boolean isLowerTriangular();

    /** Returns <CODE>true</CODE> if the matrix is symmetric. */
    boolean isSymmetric();

    /** Returns <CODE>true</CODE> if the matrix is upper triangular. */
    boolean isUpperTriangular();

    /** Get the storage type (heap or direct). */
    Storage storage();
}
