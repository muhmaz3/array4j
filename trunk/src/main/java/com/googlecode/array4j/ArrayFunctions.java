package com.googlecode.array4j;

import java.nio.ByteBuffer;

import com.googlecode.array4j.ufunc.CopySwapNFunction;
import com.googlecode.array4j.ufunc.VectorUnaryFunction;

public interface ArrayFunctions extends CopySwapNFunction {
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

    VectorUnaryFunction getCastFunc(ArrayType type);

    CopySwapNFunction getCopySwapN();
}
