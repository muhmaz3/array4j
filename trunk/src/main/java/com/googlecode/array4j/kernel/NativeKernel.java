package com.googlecode.array4j.kernel;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.googlecode.array4j.ArrayFunctions;

public final class NativeKernel implements Kernel {
    static {
        System.loadLibrary("array4j");
    }

    private static final ArrayFunctions INTEGER_FUNCTIONS = null;

    private static final ArrayFunctions DOUBLE_FUNCTIONS = new NativeDoubleFunctions();

    public static void init() {
    }

    public ByteBuffer createBuffer(final int capacity) {
        final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(capacity);
        byteBuffer.order(ByteOrder.nativeOrder());
        return byteBuffer;
    }

    public ArrayFunctions getIntegerFunctions() {
        return INTEGER_FUNCTIONS;
    }

    public ArrayFunctions getDoubleFunctions() {
        return DOUBLE_FUNCTIONS;
    }
}
