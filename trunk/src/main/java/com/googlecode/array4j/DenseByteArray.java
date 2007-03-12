package com.googlecode.array4j;

import java.nio.ByteBuffer;

import com.googlecode.array4j.kernel.KernelType;
import com.googlecode.array4j.types.ArrayDescr;
import com.googlecode.array4j.types.Types;

public final class DenseByteArray extends AbstractArray<DenseByteArray> implements ByteArray<DenseByteArray> {
    private static final ArrayDescr DESCR = ArrayDescr.valueOf(Types.BYTE);

    private ByteBuffer fData;

    private DenseByteArray(final int[] dims, final int[] strides, final int flags, final Object base,
            final KernelType kernelType) {
        this(dims, strides, null, flags, base, kernelType);
    }

    public DenseByteArray(final int[] dims, final int[] strides, final ByteBuffer data, final int flags,
            final Object base, final KernelType kernelType) {
        super(DESCR, dims, strides, data, flags, base, kernelType);
    }

    public byte get(final int... indexes) {
        return getData().get(getOffsetFromIndexes(indexes));
    }

    @Override
    protected void setBuffer(final ByteBuffer data) {
        fData = (ByteBuffer) data.duplicate().rewind();
    }

    public static DenseByteArray zeros(final int... dims) {
        return zeros(KernelType.DEFAULT, Order.C, dims);
    }

    public static DenseByteArray zeros(final KernelType kernelType, final Order order, final int... dims) {
        return new DenseByteArray(dims, null, orderAsFlags(order), null, kernelType);
    }

    @Override
    protected DenseByteArray create(final int[] dims, final int[] strides, final ByteBuffer data, final int flags,
            final KernelType kernelType) {
        return new DenseByteArray(dims, strides, data, flags, this, kernelType);
    }
}
