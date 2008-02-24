package com.googlecode.array4j.matrix.packed;

import com.googlecode.array4j.Storage;
import com.googlecode.array4j.matrix.dense.DenseFactory;
import com.googlecode.array4j.matrix.dense.FloatDenseVector;
import com.googlecode.array4j.matrix.packed.AbstractPackedMatrix.PackedType;

public final class PackedFactory {
    public static FloatDenseVector createFloatVector(final int length) {
        return DenseFactory.createFloatVector(length);
    }

    private PackedFactory() {
    }

    public static FloatPackedMatrix createSymmetric(final int dim) {
        return createFloatSymmetric(dim, Storage.DEFAULT_FOR_DENSE);
    }

    public static FloatPackedMatrix createFloatSymmetric(final int dim, final Storage storage) {
        return new FloatPackedMatrixImpl(dim, dim, PackedType.SYMMETRIC, storage);
    }

    public static FloatPackedMatrix createFloatLowerTriangular(final int rows, final int columns) {
        return new FloatPackedMatrixImpl(rows, columns, PackedType.LOWER_TRIANGULAR, Storage.DEFAULT_FOR_DENSE);
    }

    public static FloatPackedMatrix createFloatUpperTriangular(final int rows, final int columns) {
        return new FloatPackedMatrixImpl(rows, columns, PackedType.UPPER_TRIANGULAR, Storage.DEFAULT_FOR_DENSE);
    }
}
