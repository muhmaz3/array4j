package com.googlecode.array4j.kernel;

import java.nio.ByteBuffer;

public final class Interface {
    private static final KernelType DEFAULT_KERNEL_TYPE;

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
            System.out.println(e.getMessage());
            nativeKernel = null;
        }
        NATIVE_KERNEL = nativeKernel;
        if (nativeOk) {
            DEFAULT_KERNEL_TYPE = KernelType.NATIVE;
            DEFAULT_KERNEL = NATIVE_KERNEL;
        } else {
            DEFAULT_KERNEL_TYPE = KernelType.JAVA;
            DEFAULT_KERNEL = JAVA_KERNEL;
        }
    }

    /** Returns the default kernel. */
    public static Kernel defaultKernel() {
        return DEFAULT_KERNEL;
    }

    /** Returns the default kernel type. */
    public static KernelType defaultKernelType() {
        return DEFAULT_KERNEL_TYPE;
    }

    public static Kernel kernel(final KernelType kernelType) {
        switch (kernelType) {
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
