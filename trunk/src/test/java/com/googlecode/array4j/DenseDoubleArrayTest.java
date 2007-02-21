package com.googlecode.array4j;

import org.junit.Test;

public final class DenseDoubleArrayTest {
    @Test
    public void testZeros() {
        DenseDoubleArray.zeros(0);
        DenseDoubleArray.zeros(0, 0);
        DenseDoubleArray.zeros(1);
        DenseDoubleArray.zeros(1, 1);
        DenseDoubleArray.zeros(3, 3);
        DenseDoubleArray.zeros(0, 1);
        DenseDoubleArray.zeros(1, 0);
        // TODO test DenseDoubleArray.zeros();
    }
}
