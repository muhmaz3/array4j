package com.googlecode.array4j;

import static com.googlecode.array4j.Indexing.sliceStart;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public final class DenseDoubleArrayTest {
    @Test
    public void testZeros() {
        DoubleArray<? extends DoubleArray> arr;
        arr = DenseDoubleArray.zeros(0);
        assertEquals(1287, arr.flags());
        // TODO numpy allocates something, but doesn't report it
//        assertEquals(0, arr.nbytes());

        DenseDoubleArray.zeros(0, 0);
        DenseDoubleArray.zeros(1);
        DenseDoubleArray.zeros(1, 1);
        DenseDoubleArray.zeros(3, 3);
        DenseDoubleArray.zeros(0, 1);
        DenseDoubleArray.zeros(1, 0);

        arr = DenseDoubleArray.zeros(3, 3);
        assertEquals(1285, arr.flags());
        assertEquals(72, arr.nbytes());

        // TODO test DenseDoubleArray.zeros(); (no arguments)
    }

    @Test
    public void testReshape() {
        DoubleArray<? extends DoubleArray> arr;

        arr = DenseDoubleArray.zeros(4, 3);
        arr = arr.reshape(3, 4);
        assertNotNull(arr);
        assertTrue(Arrays.equals(new int[]{3, 4}, arr.shape()));
    }

    @Test
    public void testIndexing() {
        DoubleArray<? extends DoubleArray> arr;
        arr = DenseDoubleArray.zeros(3, 3);
        arr = arr.get(sliceStart(1), sliceStart(1));
        assertNotNull(arr);
        assertTrue(Arrays.equals(new int[]{2, 2}, arr.shape()));
        // TODO check that we actually get the right data from the view
    }
}
