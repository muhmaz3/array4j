package com.googlecode.array4j.kernel;

import java.nio.ByteBuffer;

import com.googlecode.array4j.ArrayFunctions;

public final class JavaKernel implements Kernel {
    private static final ArrayFunctions INTEGER_FUNCTIONS = new JavaIntegerFunctions();

    private static final ArrayFunctions DOUBLE_FUNCTIONS = new JavaDoubleFunctions();

    public static void init() {
    }

    public ByteBuffer createBuffer(final int capacity) {
        return ByteBuffer.allocate(capacity);
    }

    public ArrayFunctions getIntegerFunctions() {
        return INTEGER_FUNCTIONS;
    }

    public ArrayFunctions getDoubleFunctions() {
        return DOUBLE_FUNCTIONS;
    }
}
