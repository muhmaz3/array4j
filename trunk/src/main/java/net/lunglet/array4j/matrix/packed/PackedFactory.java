package net.lunglet.array4j.matrix.packed;

import net.lunglet.array4j.Storage;
import net.lunglet.array4j.matrix.dense.FloatDenseMatrix;
import net.lunglet.array4j.matrix.packed.AbstractPackedMatrix.PackedType;

public final class PackedFactory {
    public static FloatPackedMatrix createFloatLowerTriangular(final int rows, final int columns) {
        return new FloatPackedMatrixImpl(rows, columns, PackedType.LOWER_TRIANGULAR, Storage.DEFAULT);
    }

    public static FloatPackedMatrix createFloatUpperTriangular(final int rows, final int columns) {
        return new FloatPackedMatrixImpl(rows, columns, PackedType.UPPER_TRIANGULAR, Storage.DEFAULT);
    }

    public static FloatPackedMatrix floatSymmetric(final int dim) {
        return floatSymmetric(dim, Storage.DEFAULT);
    }

    public static FloatPackedMatrix floatSymmetric(final int dim, final Storage storage) {
        return new FloatPackedMatrixImpl(dim, dim, PackedType.SYMMETRIC, storage);
    }

    public static FloatPackedMatrix floatSymmetricDirect(final int dim) {
        return floatSymmetric(dim, Storage.DIRECT);
    }

    public static FloatPackedMatrix floatSymmetricHeap(final int dim) {
        return floatSymmetric(dim, Storage.HEAP);
    }

    public static FloatPackedMatrix symmetric(final FloatDenseMatrix a) {
        if (!a.isSquare()) {
            throw new IllegalArgumentException();
        }
        FloatPackedMatrix b = floatSymmetric(a.rows(), a.storage());
        for (int i = 0; i < a.rows(); i++) {
            for (int j = i; j < a.columns(); j++) {
                b.set(i, j, a.get(i, j));
            }
        }
        return b;
    }

    private PackedFactory() {
    }
}
