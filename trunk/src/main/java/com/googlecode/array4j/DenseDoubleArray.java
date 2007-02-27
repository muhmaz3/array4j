package com.googlecode.array4j;

import java.nio.ByteBuffer;

import com.googlecode.array4j.kernel.KernelType;

public final class DenseDoubleArray extends AbstractDoubleArray<DenseDoubleArray> {
    private DenseDoubleArray(final int[] dims, final KernelType kernelType) {
        this(dims, null, Flags.EMPTY.getValue(), null, kernelType);
    }

    private DenseDoubleArray(final int[] dims, final int[] strides, final int flags, final Object base,
            final KernelType kernelType) {
        this(dims, strides, null, flags, base, kernelType);
    }

    private DenseDoubleArray(final int[] dims, final int[] strides, final ByteBuffer data, final int flags,
            final Object base, final KernelType kernelType) {
        super(dims, strides, data, flags, base, kernelType);
    }

    public static DenseDoubleArray zeros(final int... dims) {
        return zeros(KernelType.DEFAULT, Order.C, dims);
    }

    public static DenseDoubleArray zeros(final KernelType kernelType, final Order order, final int... dims) {
        return new DenseDoubleArray(dims, null, orderAsFlags(order), null, kernelType);
    }

    public static DenseDoubleArray arange(final double stop) {
        return arange(0.0, stop, 1.0);
    }

    public static DenseDoubleArray arange(final double start, final double stop, final double step) {
        final Range range = calculateRange(start, stop, step);
        final DenseDoubleArray arr = new DenseDoubleArray(new int[]{range.length},  KernelType.DEFAULT);
        fill(arr, range);
        // TODO byte swapping
        return arr;
    }

    @Override
    protected DenseDoubleArray create(final int[] dims, final int[] strides, final ByteBuffer data, final int flags,
            final KernelType kernelType) {
        return new DenseDoubleArray(dims, strides, data, flags, this, kernelType);
    }
}
