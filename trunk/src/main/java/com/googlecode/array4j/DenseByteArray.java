package com.googlecode.array4j;

public final class DenseByteArray extends AbstractByteArray<DenseByteArray> {
    private DenseByteArray() {
        super();
    }

    private DenseByteArray(final DenseByteArray other) {
        super(other);
    }

//    @Override
//    protected DenseByteArray create(final DenseByteArray other) {
//        return new DenseByteArray(other);
//    }

    public static DenseByteArray arange(final int stop) {
        return arange(0, stop, 1);
    }

    public static DenseByteArray arange(final int start, final int stop) {
        return arange(start, stop, 1);
    }

    public static DenseByteArray arange(final int start, final int stop, final int step) {
        return arange(start, stop, step, new DenseByteArray());
    }
}
