package com.googlecode.array4j.matrix.packed;

import com.googlecode.array4j.matrix.FloatMatrix;
import com.googlecode.array4j.matrix.dense.FloatDenseVector;
import java.nio.FloatBuffer;

public interface FloatPackedMatrix extends FloatMatrix, PackedMatrix {
    /** {@inheritDoc} */
    FloatBuffer data();

    /** {@inheritDoc} */
    void plusEquals(FloatMatrix other);

    /** {@inheritDoc} */
    FloatDenseVector asVector();
}
