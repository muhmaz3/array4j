package com.googlecode.array4j;

import java.nio.Buffer;
import java.nio.ByteBuffer;

import com.googlecode.array4j.kernel.Interface;
import com.googlecode.array4j.kernel.KernelType;

public abstract class AbstractByteArray<E extends AbstractByteArray> extends AbstractArray<E> implements ByteArray<E> {
    private static final int ELEMENT_SIZE = 1;

    private ByteBuffer fData;

    public AbstractByteArray(final int[] dims, final int[] strides, final Buffer data, final int flags,
            final Object base, final KernelType kernelType) {
        super(dims, strides, data, flags, base, kernelType);
        fData = (ByteBuffer) data;
    }

    public final int elementSize() {
        return ELEMENT_SIZE;
    }

    public final byte get(final int... indexes) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected final Buffer allocate(final KernelType kernelType, final int capacity) {
        fData = Interface.kernel(kernelType).createByteBuffer(capacity);
        return fData;
    }
}
