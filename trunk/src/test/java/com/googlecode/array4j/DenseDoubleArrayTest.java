package com.googlecode.array4j;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class DenseDoubleArrayTest {
    @Test
    public void testZeros() {
        DoubleArray<? extends DoubleArray> arr = DenseDoubleArray.zeros(0);
        assertEquals(1287, arr.flags());

        DenseDoubleArray.zeros(0, 0);
        DenseDoubleArray.zeros(1);
        DenseDoubleArray.zeros(1, 1);
        DenseDoubleArray.zeros(3, 3);
        DenseDoubleArray.zeros(0, 1);
        DenseDoubleArray.zeros(1, 0);

        arr = DenseDoubleArray.zeros(3, 3);
        assertEquals(1285, arr.flags());

        // TODO test DenseDoubleArray.zeros(); (no arguments)
    }
}
