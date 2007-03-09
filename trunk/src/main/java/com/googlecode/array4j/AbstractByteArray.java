package com.googlecode.array4j;

import java.nio.ByteBuffer;

import com.googlecode.array4j.kernel.KernelType;
import com.googlecode.array4j.types.ArrayDescr;
import com.googlecode.array4j.types.Types;

public abstract class AbstractByteArray<E extends AbstractByteArray> extends AbstractArray<E> implements ByteArray<E> {
    private static final ArrayDescr DESCR = ArrayDescr.valueOf(Types.BYTE);

    private ByteBuffer fData;

    public AbstractByteArray(final int[] dims, final int[] strides, final ByteBuffer data, final int flags,
            final Object base, final KernelType kernelType) {
        super(DESCR, dims, strides, data, flags, base, kernelType);
    }

    public final byte get(final int... indexes) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected final void setBuffer(final ByteBuffer data) {
        fData = (ByteBuffer) data.duplicate().rewind();
    }
}
