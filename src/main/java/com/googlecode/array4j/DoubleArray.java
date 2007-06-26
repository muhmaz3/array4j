package com.googlecode.array4j;

public final class DoubleArray {
    public static final ArrayDescr DTYPE = ArrayDescr.fromType(ArrayType.DOUBLE);

    private DoubleArray() {
    }

    public static DenseArray zeros(final int... dims) {
        return new DenseArray(DTYPE, dims);
    }

    public static DenseArray arange(final double stop) {
        // when only stop is specified, pass it on as start
        return Arange.arange(stop, null, null, DTYPE);
    }
}
