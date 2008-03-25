package net.lunglet.array4j.matrix.packed;

import java.nio.FloatBuffer;
import net.lunglet.array4j.matrix.FloatMatrix;

public interface FloatPackedMatrix extends FloatMatrix, PackedMatrix {
    /** {@inheritDoc} */
    FloatBuffer data();
}
