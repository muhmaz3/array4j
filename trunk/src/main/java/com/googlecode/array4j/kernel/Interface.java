package com.googlecode.array4j.kernel;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

public final class Interface {
    private static final Kernel DEFAULT_KERNEL;

    private static final Kernel JAVA_KERNEL;

    private static final Kernel NATIVE_KERNEL;

    private Interface() {
    }

    static {
        JAVA_KERNEL = new JavaKernel();
        Kernel nativeKernel;
        boolean nativeOk = false;
        try {
            nativeKernel = new NativeKernel();
            nativeOk = true;
        } catch (final UnsatisfiedLinkError e) {
            nativeKernel = null;
        }
        NATIVE_KERNEL = nativeKernel;
        if (nativeOk) {
            DEFAULT_KERNEL = NATIVE_KERNEL;
        } else {
            DEFAULT_KERNEL = JAVA_KERNEL;
        }
    }

    public static Kernel kernel() {
        return DEFAULT_KERNEL;
    }

    public static Kernel kernel(final KernelType kernelType) {
        switch (kernelType) {
        case DEFAULT:
            return DEFAULT_KERNEL;
        case JAVA:
            return JAVA_KERNEL;
        case NATIVE:
            if (NATIVE_KERNEL == null) {
                throw new UnsupportedOperationException();
            }
            return NATIVE_KERNEL;
        default:
            throw new AssertionError();
        }
    }

    public static ByteBuffer createByteBuffer(final int capacity) {
        return DEFAULT_KERNEL.createByteBuffer(capacity);
    }

    public static ShortBuffer createShortBuffer(final int capacity) {
        return DEFAULT_KERNEL.createShortBuffer(capacity);
    }

    public static IntBuffer createIntBuffer(final int capacity) {
        return DEFAULT_KERNEL.createIntBuffer(capacity);
    }

    public static LongBuffer createLongBuffer(final int capacity) {
        return DEFAULT_KERNEL.createLongBuffer(capacity);
    }

    public static DoubleBuffer createDoubleBuffer(final int capacity) {
        return DEFAULT_KERNEL.createDoubleBuffer(capacity);
    }

    public static FloatBuffer createFloatBuffer(final int capacity) {
        return DEFAULT_KERNEL.createFloatBuffer(capacity);
    }

    public static DoubleBuffer createDoubleComplexBuffer(final int capacity) {
        return DEFAULT_KERNEL.createDoubleBuffer(2 * capacity);
    }

    public static FloatBuffer createFloatComplexBuffer(final int capacity) {
        return DEFAULT_KERNEL.createFloatBuffer(2 * capacity);
    }
}
