package com.googlecode.array4j.packed;

import com.googlecode.array4j.FloatMatrix;
import java.nio.FloatBuffer;

public interface FloatPackedMatrix extends FloatMatrix, PackedMatrix {
    FloatBuffer data();

//    static CopyOfFloatPackedMatrix createLowerTriangular(final int rows, final int columns) {
//        return new CopyOfFloatPackedMatrix(rows, columns, PackedType.LOWER_TRIANGULAR, Storage.DEFAULT_FOR_DENSE);
//    }
//
//    static CopyOfFloatPackedMatrix createSymmetric(final int dim) {
//        return new CopyOfFloatPackedMatrix(dim, dim, PackedType.SYMMETRIC, Storage.DEFAULT_FOR_DENSE);
//    }
//
//    static CopyOfFloatPackedMatrix createSymmetric(final int dim, final Storage storage) {
//        return new CopyOfFloatPackedMatrix(dim, dim, PackedType.SYMMETRIC, storage);
//    }
//
//    static CopyOfFloatPackedMatrix createUpperTriangular(final int rows, final int columns) {
//        return new CopyOfFloatPackedMatrix(rows, columns, PackedType.UPPER_TRIANGULAR, Storage.DEFAULT_FOR_DENSE);
//    }
}
