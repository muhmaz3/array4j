package com.googlecode.array4j.ufunc;

import org.junit.Test;

import com.googlecode.array4j.ArrayDescr;
import com.googlecode.array4j.DenseArray;
import com.googlecode.array4j.DoubleArray;

public final class AddUFuncTest {
    private static final ArrayDescr DOUBLE_DTYPE = DoubleArray.DTYPE;

    @Test
    public void testCall() {
        final UFunc add = new AddUFunc();
        final DenseArray in = null;
        DenseArray out = null;
        final DenseByteArray in2 = null;
        DenseByteArray out2 = null;

        out = add.call(DOUBLE_DTYPE, in, in2);
        out2 = add.call(DenseByteArray.class, in, in2);
        add.call(in, in, out);
        add.call(in, in, out2);
    }

    @Test
    public void testAccumulate() {
        final UFunc add = new AddUFunc();

        final DenseArray in = null;
        DenseArray out;
        DenseByteArray out2 = null;

        out = add.accumulate(in);
        out = add.accumulate(in, 0);
        out = add.accumulate(in, DOUBLE_DTYPE);
        out = add.accumulate(in, 0, DOUBLE_DTYPE);
        out2 = add.accumulate(in, DenseByteArray.class);
        out2 = add.accumulate(in, 0, DenseByteArray.class);
    }

    @Test
    public void testReduce() {
        final UFunc add = new AddUFunc();

        final DenseArray in = null;
        DenseArray out;
        DenseByteArray out2 = null;

        out = add.reduce(in);
        out = add.reduce(in, DOUBLE_DTYPE);
        out = add.reduce(in, 0);
        out = add.reduce(in, 0, DenseArray.class);
        out2 = add.reduce(in, DenseByteArray.class);
        out2 = add.reduce(in, 0, DenseByteArray.class);
    }

    @Test
    public void testReduceAt() {
        final UFunc add = new AddUFunc();

        final DenseArray in = null;
        DenseArray out;

        out = add.reduceat(in);
        out = add.reduceat(in, DOUBLE_DTYPE);
        out = add.reduceat(in, 0);
        out = add.reduceat(in, 0, DOUBLE_DTYPE);
        out = add.reduceat(in, 0, new int[]{});
        out = add.reduceat(in, new int[]{}, DOUBLE_DTYPE);
        out = add.reduceat(in, 0, new int[]{}, DOUBLE_DTYPE);

    }

    @Test
    public void testOuter() {
        final UFunc add = new AddUFunc();
        final DenseArray in1 = null;
        DenseArray out1;
        final DenseByteArray in2 = null;
        DenseByteArray out2;

        out1 = add.outer(in1, in2, DOUBLE_DTYPE);
        out2 = add.outer(in1, in1, DenseByteArray.class);
    }
}
