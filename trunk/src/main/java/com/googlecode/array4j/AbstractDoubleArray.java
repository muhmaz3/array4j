package com.googlecode.array4j;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;

import com.googlecode.array4j.kernel.KernelType;
import com.googlecode.array4j.types.ArrayDescr;
import com.googlecode.array4j.types.Types;

public abstract class AbstractDoubleArray<E extends AbstractDoubleArray> extends AbstractArray<E> implements
        DoubleArray<E> {
    private static final ArrayDescr DESCR = ArrayDescr.valueOf(Types.DOUBLE);

    private DoubleBuffer fData;

    public AbstractDoubleArray(final int[] dims, final int[] strides, final ByteBuffer data, final int flags,
            final Object base, final KernelType kernelType) {
        super(DESCR, dims, strides, data, flags, base, kernelType);
    }

    public final double get(final int... indexes) {
        return getData().getDouble(getOffsetFromIndexes(indexes));
    }

    protected static void fill(final AbstractDoubleArray<?> arr, final Range range) {
        if (range.length == 0) {
            return;
        }
        final ByteBuffer data = arr.getData();
        data.putDouble(0, range.start);
        if (range.length == 1) {
            return;
        }
        data.putDouble(arr.elementSize(), range.next);

        // This code corresponds to the DOUBLE_fill function in NumPy.
        final double start = arr.fData.get(0);
        double delta = arr.fData.get(1);
        delta -= start;
        for (int i = 2; i < range.length; i++) {
            arr.fData.put(i, start + i * delta);
        }
    }

    @Override
    protected final void setBuffer(final ByteBuffer data) {
        fData = ((ByteBuffer) data.duplicate().rewind()).asDoubleBuffer();
    }
}
