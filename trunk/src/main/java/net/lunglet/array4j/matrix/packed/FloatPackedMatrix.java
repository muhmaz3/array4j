package net.lunglet.array4j.matrix.packed;

import java.nio.FloatBuffer;
import net.lunglet.array4j.matrix.FloatMatrix;
import net.lunglet.array4j.matrix.dense.FloatDenseVector;

public interface FloatPackedMatrix extends FloatMatrix, PackedMatrix {
    /** {@inheritDoc} */
    FloatBuffer data();

    /** {@inheritDoc} */
    void plusEquals(FloatMatrix other);

    /** {@inheritDoc} */
    FloatDenseVector asVector();
}
