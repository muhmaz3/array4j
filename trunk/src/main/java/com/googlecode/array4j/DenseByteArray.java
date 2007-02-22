package com.googlecode.array4j;

import java.nio.Buffer;

import com.googlecode.array4j.kernel.KernelType;

public final class DenseByteArray extends AbstractByteArray<DenseByteArray> {
    private DenseByteArray(final int[] dims, final int[] strides, final int flags, final Object base,
            final KernelType kernelType) {
        this(dims, strides, null, flags, base, kernelType);
    }

    private DenseByteArray(final int[] dims, final int[] strides, final Buffer data, final int flags,
            final Object base, final KernelType kernelType) {
        super(dims, strides, data, flags, base, kernelType);
    }

    public static DenseByteArray zeros(final int... dims) {
        return zeros(KernelType.DEFAULT, Order.C, dims);
    }

    public static DenseByteArray zeros(final KernelType kernelType, final Order order, final int... dims) {
        return new DenseByteArray(dims, null, orderAsFlags(order), null, kernelType);
    }

    @Override
    protected DenseByteArray create(final int[] dims, final int[] strides, final Buffer data, final int flags,
            final KernelType kernelType) {
        return new DenseByteArray(dims, strides, data, flags, this, kernelType);
    }
}
