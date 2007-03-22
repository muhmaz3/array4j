package com.googlecode.array4j;

import java.nio.ByteBuffer;

public interface ArrayFunctions {
    void setitem(Object obj, ByteBuffer data, DenseArray arr);

    void fill(ByteBuffer data, int length);

    void add(ByteBuffer[] bufptr, int[] dimensions, int[] steps, Object funcdata);

    void multiply(ByteBuffer[] bufptr, int[] dimensions, int[] steps, Object funcdata);

    void square(ByteBuffer[] bufptr, int[] dimensions, int[] steps, Object funcdata);

    void sqrt(ByteBuffer[] bufptr, int[] dimensions, int[] steps, Object funcdata);

    void ldexp(ByteBuffer[] bufptr, int[] dimensions, int[] steps, Object funcdata);
}
