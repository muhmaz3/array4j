package com.googlecode.array4j.ufunc;

import com.googlecode.array4j.DenseByteArray;
import com.googlecode.array4j.DenseDoubleArray;

public final class AddUFuncTest {
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

    public void testReduceAt() {
    }

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
