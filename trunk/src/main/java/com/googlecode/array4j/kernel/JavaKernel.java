package com.googlecode.array4j.kernel;

import java.nio.ByteBuffer;

import com.googlecode.array4j.ArrayFunctions;

public final class JavaKernel implements Kernel {
    private static final ArrayFunctions DOUBLE_ARRAY_FUNCTIONS = new JavaDoubleArrayFunctions();

    public static void init() {
    }

    public ByteBuffer createBuffer(final int capacity) {
        return ByteBuffer.allocate(capacity);
    }

    public ArrayFunctions getDoubleArrayFunctions() {
        return DOUBLE_ARRAY_FUNCTIONS;
    }
}
