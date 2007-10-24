package com.googlecode.array4j.packed;

import com.googlecode.array4j.Storage;
import java.nio.Buffer;

/**
 * Matrix packed by columns.
 */
public interface PackedMatrix {
    Buffer data();

    boolean isLowerTriangular();

    boolean isSymmetric();

    boolean isUpperTriangular();

    Storage storage();
}
