package com.googlecode.array4j;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;

import com.googlecode.array4j.kernel.KernelType;
import com.googlecode.array4j.types.ArrayDescr;
import com.googlecode.array4j.types.Types;

public final class DenseDoubleArray extends AbstractArray<DenseDoubleArray> implements DoubleArray<DenseDoubleArray> {
    private static final ArrayDescr DESCR = ArrayDescr.valueOf(Types.DOUBLE);

    private DoubleBuffer fData;

    private DenseDoubleArray(final int[] dims, final KernelType kernelType) {
        this(dims, null, Flags.EMPTY.getValue(), null, kernelType);
    }

    private DenseDoubleArray(final int[] dims, final int[] strides, final int flags, final Object base,
            final KernelType kernelType) {
        this(dims, strides, null, flags, base, kernelType);
    }

    private DenseDoubleArray(final int[] dims, final int[] strides, final ByteBuffer data, final int flags,
            final Object base, final KernelType kernelType) {
        super(DESCR, dims, strides, data, flags, base, kernelType);
    }

    public double get(final int... indexes) {
        return getData().getDouble(getOffsetFromIndexes(indexes));
    }

    private static void fill(final DenseDoubleArray arr, final Range range) {
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
    protected void setBuffer(final ByteBuffer data) {
        fData = ((ByteBuffer) data.duplicate().rewind()).asDoubleBuffer();
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
