package com.googlecode.array4j.packed;

import com.googlecode.array4j.FloatMatrix;
import java.nio.FloatBuffer;

public interface FloatPackedMatrix extends FloatMatrix, PackedMatrix {
    FloatBuffer data();

    /**
     * Element-wise addition without broadcasting.
     * <p>
     * Implementations of this method may accept only a
     * <CODE>FloatPackedMatrix</CODE> argument, throwing
     * <CODE>IllegalArgumentException</CODE> otherwise.
     */
    void plusEquals(FloatMatrix other);
}
