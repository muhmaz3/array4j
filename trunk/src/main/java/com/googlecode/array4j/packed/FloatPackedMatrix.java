package com.googlecode.array4j.packed;

import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.dense.FloatDenseVector;
import java.nio.FloatBuffer;

public interface FloatPackedMatrix extends FloatMatrix, PackedMatrix {
    /** {@inheritDoc} */
    FloatBuffer data();

    /** {@inheritDoc} */
    void plusEquals(FloatMatrix other);

    /** {@inheritDoc} */
    FloatDenseVector asVector();
}
