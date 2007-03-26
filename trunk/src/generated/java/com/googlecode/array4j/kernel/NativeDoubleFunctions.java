package com.googlecode.array4j.kernel;

import java.nio.ByteBuffer;

import com.googlecode.array4j.ArrayFunctions;
import com.googlecode.array4j.DenseArray;

public final class NativeDoubleFunctions implements ArrayFunctions {
    public native void fill(final ByteBuffer data, final int length);

    public void setitem(final Object obj, final ByteBuffer data, final DenseArray arr) {
        if (obj instanceof Number) {
            // can't use DoubleBuffer.put here, because data ends up being
            // inserted in big endian format because we're using a ByteBuffer
            setitem(((Number) obj).doubleValue(), data, data.position());
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private native void setitem(final double value, final ByteBuffer data, final int offset);

    public native void add(final ByteBuffer[] bufptr, final int[] dimensions, final int[] steps, final Object funcdata);

    public native void multiply(final ByteBuffer[] bufptr, final int[] dimensions, final int[] steps,
            final Object funcdata);

    public native void square(final ByteBuffer[] bufptr, final int[] dimensions, final int[] steps,
            final Object funcdata);

    public native void ldexp(final ByteBuffer[] bufptr, final int[] dimensions, final int[] steps, final Object funcdata);

    public native void sqrt(final ByteBuffer[] bufptr, final int[] dimensions, final int[] steps, final Object funcdata);

    public native void log(final ByteBuffer[] bufptr, final int[] dimensions, final int[] steps, final Object funcdata);

    public native void exp(final ByteBuffer[] bufptr, final int[] dimensions, final int[] steps, final Object funcdata);
}
