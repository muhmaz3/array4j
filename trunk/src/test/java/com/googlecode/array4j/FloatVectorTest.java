package com.googlecode.array4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public final class FloatVectorTest<M extends FloatMatrix<M, V>, V extends FloatVector<V>> {
    @Parameters
    public static Collection<?> data() {
        return Arrays.asList(new Object[][]{{new DenseFloatMatrixFactory()}, {new DenseFloatMatrixFactory()}});
    }

    private final FloatMatrixFactory<M, V> factory;

    public FloatVectorTest(final FloatMatrixFactory<M, V> factory) {
        this.factory = factory;
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructorDataTooSmall() {
        factory.createVector(new float[1], 2, 0, 1, Orientation.ROW);
    }

    // TODO should probably throw IllegalArgumentException instead?
    @Test(expected=NegativeArraySizeException.class)
    public void testConstructorNegativeSize() {
        factory.createVector(-1, Orientation.DEFAULT_FOR_VECTOR);
    }

    @Test
    public void testConstructors() {
        factory.createVector(0, Orientation.DEFAULT_FOR_VECTOR);
        factory.createVector(1, Orientation.DEFAULT_FOR_VECTOR);
        factory.createVector(10, Orientation.DEFAULT_FOR_VECTOR);
        factory.createVector(new float[0], 0, 0, 1, Orientation.ROW);
        factory.createVector(new float[1], 1, 0, 1, Orientation.ROW);
        // a really long constant vector
        factory.createVector(new float[1], Integer.MAX_VALUE, 0, 0, Orientation.ROW);
        // TODO test for overflows in checkArgument checks
    }

    @Test
    public void testTranspose() {
        final V vector = factory.createVector(10, Orientation.DEFAULT_FOR_VECTOR);
        assertTrue("Vector must be a column vector by default", vector.isColumnVector());
        final V transposedVector = vector.transpose();
        assertTrue(transposedVector.isRowVector());
        assertFalse(vector.equals(transposedVector));
        final V originalVector = transposedVector.transpose();
        assertTrue(originalVector.isColumnVector());
        assertEquals(vector, originalVector);
    }
}
