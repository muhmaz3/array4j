package com.googlecode.array4j;

import com.googlecode.array4j.kernel.KernelType;

public final class DenseDoubleArray extends AbstractDoubleArray<DenseDoubleArray> {
    private DenseDoubleArray(final int[] dims, final int[] strides, final int flags, final Object base,
            final KernelType kernelType) {
        super(dims, strides, flags, base, kernelType);
    }

    public static DenseDoubleArray zeros(final int... dims) {
        return zeros(KernelType.DEFAULT, Order.C, dims);
    }

    public static DenseDoubleArray zeros(final KernelType kernelType, final Order order, final int... dims) {
        final Flags fortran;
        if (order.equals(Order.FORTRAN)) {
            fortran = Flags.FORTRAN;
        } else {
            fortran = Flags.EMPTY;
        }
        return new DenseDoubleArray(dims, null, fortran.getValue(), null, kernelType);
    }

    public double get(final int... indexes) {
        throw new UnsupportedOperationException();
    }

    public DenseDoubleArray get(final Object... indexes) {
        throw new UnsupportedOperationException();
    }
}
