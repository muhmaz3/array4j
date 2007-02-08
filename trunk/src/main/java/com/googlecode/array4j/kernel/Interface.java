package com.googlecode.array4j.kernel;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

public final class Interface {
    private static final boolean IS_KERNEL_NATIVE;

    private static final Kernel KERNEL;

    private Interface() {
    }

    static {
        KERNEL = createKernel();
        IS_KERNEL_NATIVE = KERNEL instanceof NativeKernel;
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

    public static DoubleBuffer createDoubleBuffer(final int capacity) {
        if (IS_KERNEL_NATIVE) {
            final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(8 * capacity);
            byteBuffer.order(ByteOrder.nativeOrder());
            return byteBuffer.asDoubleBuffer();
        } else {
            // TODO check if we need to fix this buffer's byte order
            return DoubleBuffer.allocate(capacity);
        }
    }

    public static FloatBuffer createFloatBuffer(final int capacity) {
        if (IS_KERNEL_NATIVE) {
            final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * capacity);
            byteBuffer.order(ByteOrder.nativeOrder());
            return byteBuffer.asFloatBuffer();
        } else {
            // TODO check if we need to fix this buffer's byte order
            return FloatBuffer.allocate(capacity);
        }
    }
}
