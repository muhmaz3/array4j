package com.googlecode.array4j.kernel;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

public final class Interface {
    private static final Kernel KERNEL;

    private Interface() {
    }

    static {
        KERNEL = createKernel();
    }

    private static Kernel createKernel() {
        try {
            return new NativeKernel();
        } catch (final UnsatisfiedLinkError e) {
            // ignore link error and load Java kernel instead
        }
        return new JavaKernel();
    }

    public static Kernel getKernel() {
        return KERNEL;
    }

    public static Kernel kernel() {
        return KERNEL;
    }

    public static ByteBuffer createByteBuffer(final int capacity) {
        return KERNEL.createByteBuffer(capacity);
    }

    public static ShortBuffer createShortBuffer(final int capacity) {
        return KERNEL.createShortBuffer(capacity);
    }

    public static IntBuffer createIntBuffer(final int capacity) {
        return KERNEL.createIntBuffer(capacity);
    }

    public static LongBuffer createLongBuffer(final int capacity) {
        return KERNEL.createLongBuffer(capacity);
    }

    public static DoubleBuffer createDoubleBuffer(final int capacity) {
        return KERNEL.createDoubleBuffer(capacity);
    }

    public static FloatBuffer createFloatBuffer(final int capacity) {
        return KERNEL.createFloatBuffer(capacity);
    }

    public static DoubleBuffer createDoubleComplexBuffer(final int capacity) {
        return KERNEL.createDoubleBuffer(2 * capacity);
    }

    public static FloatBuffer createFloatComplexBuffer(final int capacity) {
        return KERNEL.createFloatBuffer(2 * capacity);
    }
}
