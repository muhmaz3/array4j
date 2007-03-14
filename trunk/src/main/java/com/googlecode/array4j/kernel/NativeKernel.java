package com.googlecode.array4j.kernel;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.googlecode.array4j.ArrayFunctions;

public final class NativeKernel implements Kernel {
    static {
        System.loadLibrary("array4j");
    }

    public static void init() {
    }

    public ByteBuffer createBuffer(final int capacity) {
        final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(capacity);
        byteBuffer.order(ByteOrder.nativeOrder());
        return byteBuffer;
    }

    public ArrayFunctions getDoubleArrayFunctions() {
        throw new UnsupportedOperationException();
    }
}
