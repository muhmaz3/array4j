package com.googlecode.array4j;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.googlecode.array4j.DenseArray;

public final class DenseArrayTest {
    @Test
    public void testReshapeAndGet() {
        // example from the NumPy book (page 27)
        DenseArray arr = DenseArray.arange(4 * 5 * 6);
        assertEquals(120, arr.size());
        int[] shape = {4, 5, 6};
        arr = arr.reshape(shape);
        assertEquals(50.0, arr.get(1, 3, 2));

        arr = DenseArray.arange(24.0);
        assertEquals(24, arr.size());
        shape = new int[]{2, 4, 3};
        arr = arr.reshape(shape);
        double x = 0.0;
        for (int i = 0; i < shape[0]; i++) {
            for (int j = 0; j < shape[1]; j++) {
                for (int k = 0; k < shape[2]; k++) {
                    assertEquals(x, arr.get(i, j, k));
                    x += 1.0;
                }
            }
        }
    }

    @Test
    public void testSetGet() {
        final DenseArray arr = new DenseArray(2, 4, 3);
        double value = 0.0;
        for (int i = 0; i < arr.shape(0); i++) {
            for (int j = 0; j < arr.shape(1); j++) {
                for (int k = 0; k < arr.shape(2); k++) {
                    arr.set(value, i, j, k);
                    assertEquals(value, arr.get(i, j, k));
                    value += 1.0;
                }
            }
        }
    }

    @Test
    public void testArange() {
        DenseArray arr = DenseArray.arange(1.0, 0.0);
        assertEquals(0, arr.size());
        arr = DenseArray.arange(1.0, 1.0);
        assertEquals(0, arr.size());
    }

    @Test
    public void testLog() {
        final double[] values = {1.0, Math.exp(1.0), Math.exp(-1.0)};
        final DenseArray origarr = new DenseArray(values);
        final DenseArray logarr = origarr.log();
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], origarr.get(i));
            assertEquals(Math.log(values[i]), logarr.get(i));
        }
    }

    @Test
    public void testLogEquals() {
        final double[] values = {1.0, Math.exp(1.0), Math.exp(-1.0)};
        final DenseArray arr = new DenseArray(values);
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], arr.get(i));
        }
        arr.logEquals();
        for (int i = 0; i < values.length; i++) {
            assertEquals(Math.log(values[i]), arr.get(i));
        }
    }

    @Test
    public void testSum() {
        final DenseArray arr = DenseArray.valueOf(1.0, 2.0, 3.0);
        assertEquals(6.0, arr.sum());
    }
}
