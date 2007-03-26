package com.googlecode.array4j;

import java.nio.ByteBuffer;

public interface ArrayFunctions {
    void fill(ByteBuffer data, int length);

    double getitemDouble(ByteBuffer data, DenseArray arr);

    void setitem(Object obj, ByteBuffer data, DenseArray arr);

    void add(ByteBuffer[] bufptr, int[] dimensions, int[] steps, Object funcdata);

    void multiply(ByteBuffer[] bufptr, int[] dimensions, int[] steps, Object funcdata);

    void square(ByteBuffer[] bufptr, int[] dimensions, int[] steps, Object funcdata);

    void sqrt(ByteBuffer[] bufptr, int[] dimensions, int[] steps, Object funcdata);

    void ldexp(ByteBuffer[] bufptr, int[] dimensions, int[] steps, Object funcdata);

    void log(ByteBuffer[] bufptr, int[] dimensions, int[] steps, Object funcdata);

    void exp(ByteBuffer[] bufptr, int[] dimensions, int[] steps, Object funcdata);
}
