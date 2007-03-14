package com.googlecode.array4j;

import java.nio.ByteBuffer;

public interface ArrayFunctions {
    void setitem(Object obj, ByteBuffer data, DenseArray arr);

    void fill(ByteBuffer data, int length);
}
