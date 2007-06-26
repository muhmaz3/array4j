package com.googlecode.array4j;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public final class DoubleArrayTest {
    @Test
    public void testArange() {
        final DenseArray arr = DoubleArray.arange(10.0);
        assertNotNull(arr);
    }
}
