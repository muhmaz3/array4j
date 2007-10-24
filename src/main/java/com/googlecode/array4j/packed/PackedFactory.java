package com.googlecode.array4j.packed;

import com.googlecode.array4j.dense.DenseFactory;
import com.googlecode.array4j.dense.FloatDenseMatrix;
import com.googlecode.array4j.dense.FloatDenseVector;

public final class PackedFactory {
    public static FloatDenseMatrix createFloatMatrix(final int rows, final int columns) {
        return null;
    }

    public static FloatDenseVector createFloatVector(final int length) {
        return DenseFactory.createFloatVector(length);
    }

    private PackedFactory() {
    }
}
