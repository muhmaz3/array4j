package com.googlecode.array4j.kernel;

import java.nio.ByteBuffer;

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

    public static ByteBuffer createBuffer(final int capacity) {
        return DEFAULT_KERNEL.createBuffer(capacity);
    }
}
