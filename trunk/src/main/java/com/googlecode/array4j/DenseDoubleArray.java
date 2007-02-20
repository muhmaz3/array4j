package com.googlecode.array4j;

public final class DenseDoubleArray extends AbstractDoubleArray<DenseDoubleArray> {
    public static DenseDoubleArray zeros(final int... dims) {
        return zeros(Order.C, dims);
    }

    public static DenseDoubleArray zeros(final Order order, final int... dims) {
        final Flags fortran;
        if (order.equals(Order.FORTRAN)) {
            fortran = Flags.FORTRAN;
        } else {
            fortran = Flags.EMPTY;
        }
        
        
        return null;
    }
}
