package com.googlecode.array4j;

import java.nio.Buffer;
import java.nio.DoubleBuffer;

import com.googlecode.array4j.kernel.Interface;
import com.googlecode.array4j.kernel.KernelType;

public abstract class AbstractDoubleArray<E extends AbstractDoubleArray> extends AbstractArray<E> implements
        DoubleArray<E> {
    private static final int ELEMENT_SIZE = 8;

    private DoubleBuffer fData;

    public AbstractDoubleArray(final int[] dims, final int[] strides, final Buffer data, final int flags,
            final Object base, final KernelType kernelType) {
        super(dims, strides, data, flags, base, kernelType);
        fData = (DoubleBuffer) data;
    }

    public final int elementSize() {
        return ELEMENT_SIZE;
    }

    public final double get(final int... indexes) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected final Buffer allocate(final KernelType kernelType, final int capacity) {
        fData = Interface.kernel(kernelType).createDoubleBuffer(capacity);
        return fData;
    }
}
