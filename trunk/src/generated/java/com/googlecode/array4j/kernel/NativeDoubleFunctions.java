package com.googlecode.array4j.kernel;

import java.nio.ByteBuffer;

import com.googlecode.array4j.ArrayFunctions;
import com.googlecode.array4j.DenseArray;

public final class NativeDoubleFunctions implements ArrayFunctions {
    public native void fill(ByteBuffer data, int length);

    public double getitemDouble(final ByteBuffer data, final DenseArray arr) {
        return getitemDouble(data, data.position());
    }

    private native double getitemDouble(ByteBuffer data, int offset);

    public void setitem(final Object obj, final ByteBuffer data, final DenseArray arr) {
        if (obj instanceof Number) {
            // can't use DoubleBuffer.put here, because data ends up being
            // inserted in big endian format because we're using a ByteBuffer
            setitemDouble(((Number) obj).doubleValue(), data, data.position());
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private native void setitemDouble(final double value, final ByteBuffer data, final int offset);

    public void add(final ByteBuffer[] bufptr, final int[] dimensions, final int[] steps, final Object funcdata) {
        final int[] offsets = new int[bufptr.length];
        for (int i = 0; i < offsets.length; i++) {
            offsets[i] = bufptr[i].position();
        }
        add(bufptr, offsets, dimensions, steps);
    }

    private native void add(ByteBuffer bufptr[], int[] offsets, int[] dimensions, int[] steps);

    public native void multiply(ByteBuffer[] bufptr, int[] dimensions, int[] steps, Object funcdata);

    public native void square(ByteBuffer[] bufptr, int[] dimensions, int[] steps, final Object funcdata);

    public native void ldexp(ByteBuffer[] bufptr, int[] dimensions, int[] steps, Object funcdata);

    public native void sqrt(ByteBuffer[] bufptr, int[] dimensions, int[] steps, Object funcdata);

    public native void log(ByteBuffer[] bufptr, int[] dimensions, int[] steps, Object funcdata);

    public native void exp(ByteBuffer[] bufptr, int[] dimensions, int[] steps, Object funcdata);
}
