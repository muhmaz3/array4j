package com.googlecode.array4j.kernel;

import java.nio.ByteBuffer;

import com.googlecode.array4j.ArrayFunctions;

public interface Kernel {
    ByteBuffer createBuffer(int capacity);

    ArrayFunctions getDoubleArrayFunctions();
}
