package com.googlecode.array4j;

import java.nio.Buffer;

import com.googlecode.array4j.kernel.KernelType;

public final class DenseDoubleArray extends AbstractDoubleArray<DenseDoubleArray> {
    private DenseDoubleArray(final int[] dims, final int[] strides, final int flags, final Object base,
            final KernelType kernelType) {
        this(dims, strides, null, flags, base, kernelType);
    }

    private DenseDoubleArray(final int[] dims, final int[] strides, final Buffer data, final int flags,
            final Object base, final KernelType kernelType) {
        super(dims, strides, data, flags, base, kernelType);
    }

    public static DenseDoubleArray zeros(final int... dims) {
        return zeros(KernelType.DEFAULT, Order.C, dims);
    }

    public static DenseDoubleArray zeros(final KernelType kernelType, final Order order, final int... dims) {
        return new DenseDoubleArray(dims, null, orderAsFlags(order), null, kernelType);
    }

    @Override
    protected DenseDoubleArray create(final int[] dims, final int[] strides, final Buffer data, final int flags,
            final KernelType kernelType) {
        return new DenseDoubleArray(dims, strides, data, flags, this, kernelType);
    }
}
