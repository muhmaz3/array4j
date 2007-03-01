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
        DoubleArray<?> arr;
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

        arr = DenseDoubleArray.zeros();
        assertEquals(1285, arr.flags());
        assertEquals(8, arr.nbytes());
    }

    @Test
    public void testArange() {
        DoubleArray<?> arr;
        arr = DenseDoubleArray.arange(12.0);
        assertEquals(9.0, arr.get(9));
        assertEquals(8.0, arr.reshape(4, 3).get(2, 2));
        assertEquals(10.0, arr.reshape(3, 4).get(2, 2));
    }

    @Test
    public void testReshape() {
        DoubleArray<?> arr;

        arr = DenseDoubleArray.zeros(4, 3);
        arr = arr.reshape(3, 4);
        assertNotNull(arr);
        assertTrue(Arrays.equals(new int[]{3, 4}, arr.shape()));
    }

    @Test
    public void testIndexing() {
        DoubleArray<?> arr;
        arr = DenseDoubleArray.zeros(3, 3);
        arr = arr.get(sliceStart(1), sliceStart(1));
        assertNotNull(arr);
        assertTrue(Arrays.equals(new int[]{2, 2}, arr.shape()));
        // TODO check that we actually get the right data from the view
    }

    @Test
    public void testAddEquals() {
        final DoubleArray<?> arr1 = DenseDoubleArray.arange(4).reshape(2, 2);
        final DoubleArray<?> arr2 = DenseDoubleArray.arange(2);
        arr1.addEquals(arr2);
        assertEquals(0.0, arr1.get(0, 0));
        assertEquals(2.0, arr1.get(0, 1));
        assertEquals(2.0, arr1.get(1, 0));
        assertEquals(4.0, arr1.get(1, 1));
    }
}
