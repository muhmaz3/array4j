package com.googlecode.array4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;

public final class DenseVectorTest {
    @Test
    public void testConstructors() {
        new DenseVector(0);
        final DenseVector vec = new DenseVector(10);
        assertEquals(10, vec.rows());
        assertEquals(1, vec.columns());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructor1() {
        new DenseVector(-1);
    }

    @Ignore
    public void testReshape() {
        DenseVector vec = new DenseVector(10);
        vec = vec.reshape(10);
        assertNotNull(vec);
    }

    @Ignore
    public void testFill() {
        final DenseVector vec = new DenseVector(10);
        for (int i = 0; i < vec.length(); i++) {
            assertEquals(0.0, vec.get(i));
        }
        final double[] values = {Double.MIN_VALUE, Double.MAX_VALUE, Double.NaN, Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY, -1.0, 0.0, 1.0};
        for (double value : values) {
            vec.fill(value);
            for (int i = 0; i < vec.length(); i++) {
                assertEquals(value, vec.get(i));
            }
        }
    }

    @Test
    public void testGet() {
        final DenseVector arr = new DenseVector(1);
        assertEquals(0.0, arr.get(0));
    }
}
