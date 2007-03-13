package com.googlecode.array4j.ufunc;

import org.junit.Test;

public final class AddUFuncTest {
    @Test
    public void testCall() {
        final UFunc add = new AddUFunc();
        final DenseDoubleArray in = null;
        DenseDoubleArray out = null;
        final DenseByteArray in2 = null;
        DenseByteArray out2 = null;

        out = add.call(DenseDoubleArray.class, in, in2);
        out2 = add.call(DenseByteArray.class, in, in2);
        add.call(in, in, out);
        add.call(in, in, out2);
    }

    @Test
    public void testAccumulate() {
        final UFunc add = new AddUFunc();

        final DenseDoubleArray in = null;
        DenseDoubleArray out;
        DenseByteArray out2 = null;

        out = add.accumulate(in);
        out = add.accumulate(in, 0);
        out = add.accumulate(in, DenseDoubleArray.class);
        out = add.accumulate(in, 0, DenseDoubleArray.class);
        out2 = add.accumulate(in, DenseByteArray.class);
        out2 = add.accumulate(in, 0, DenseByteArray.class);
    }

    @Test
    public void testReduce() {
        final UFunc add = new AddUFunc();

        final DenseDoubleArray in = null;
        DenseDoubleArray out;
        DenseByteArray out2 = null;

        out = add.reduce(in);
        out = add.reduce(in, DenseDoubleArray.class);
        out = add.reduce(in, 0);
        out = add.reduce(in, 0, DenseDoubleArray.class);
        out2 = add.reduce(in, DenseByteArray.class);
        out2 = add.reduce(in, 0, DenseByteArray.class);
    }

    @Test
    public void testReduceAt() {
        final UFunc add = new AddUFunc();

        final DenseDoubleArray in = null;
        DenseDoubleArray out;

        out = add.reduceat(in);
        out = add.reduceat(in, DenseDoubleArray.class);
        out = add.reduceat(in, 0);
        out = add.reduceat(in, 0, DenseDoubleArray.class);
        out = add.reduceat(in, 0, new int[]{});
        out = add.reduceat(in, new int[]{}, DenseDoubleArray.class);
        out = add.reduceat(in, 0, new int[]{}, DenseDoubleArray.class);

    }

    @Test
    public void testOuter() {
        final UFunc add = new AddUFunc();
        final DenseDoubleArray in1 = null;
        DenseDoubleArray out1;
        final DenseByteArray in2 = null;
        DenseByteArray out2;

        out1 = add.outer(in1, in2, DenseDoubleArray.class);
        out2 = add.outer(in1, in1, DenseByteArray.class);
    }
}
