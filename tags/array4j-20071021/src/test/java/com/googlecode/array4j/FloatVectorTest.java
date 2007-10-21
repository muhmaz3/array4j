package com.googlecode.array4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.googlecode.array4j.dense.FloatDenseMatrixFactory;
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
        return Arrays.asList(new Object[][]{{new FloatDenseMatrixFactory(Storage.HEAP)},
                {new FloatDenseMatrixFactory(Storage.DIRECT)}});
    }

    private final FloatMatrixFactory<M, V> factory;

    public FloatVectorTest(final FloatMatrixFactory<M, V> factory) {
        this.factory = factory;
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructorDataTooSmall() {
        // XXX this test might start failing when we start allocating aligned
        // buffers... do something about it then
        factory.createVector(new float[1], 2, 0, 1, Orientation.ROW);
    }

    @Test(expected=IllegalArgumentException.class)
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
    public void testFill() {
        for (int i = 0; i < 5; i++) {
            V x = factory.createVector(i, Orientation.DEFAULT_FOR_VECTOR);
            for (int j = 0; j < i; j++) {
                assertEquals(0.0f, x.get(j), 0.0);
            }
            FloatMatrixUtils.fill(x, i);
            for (int j = 0; j < i; j++) {
                assertEquals(i, x.get(j), 0.0);
            }
        }
    }

    @Test
    public void testMinus() {
        for (int i = 0; i < 5; i++) {
            V x = factory.createVector(i, Orientation.DEFAULT_FOR_VECTOR);
            V y = factory.createVector(i, Orientation.DEFAULT_FOR_VECTOR);
            for (int j = 0; j < i; j++) {
                x.set(j, j);
                y.set(j, i - j);
            }
            V z = x.minus(y);
            for (int j = 0; j < i; j++) {
                assertEquals(j, x.get(j), 0.0);
                assertEquals(i - j, y.get(j), 0.0);
                assertEquals(2 * j - i, z.get(j), 0.0);
            }
        }
    }

    @Test
    public void testPlusEquals() {
        for (int i = 0; i < 5; i++) {
            V x = factory.createVector(i, Orientation.DEFAULT_FOR_VECTOR);
            V y = factory.createVector(i, Orientation.DEFAULT_FOR_VECTOR);
            for (int j = 0; j < i; j++) {
                x.set(j, j);
                y.set(j, i - j);
            }
            x.plusEquals(y);
            for (int j = 0; j < i; j++) {
                assertEquals(i, x.get(j), 0.0);
                assertEquals(i - j, y.get(j), 0.0);
            }
        }
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
