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
public final class FloatVectorTest {
    @Parameters
    public static Collection<?> data() {
        return Arrays.asList(new Object[][]{{new FloatDenseMatrixFactory(Storage.HEAP)},
                {new FloatDenseMatrixFactory(Storage.DIRECT)}});
    }

    private final FloatMatrixFactory factory;

    public FloatVectorTest(final FloatMatrixFactory factory) {
        this.factory = factory;
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructorDataTooSmall() {
        // XXX this test might start failing when we start allocating aligned
        // buffers... do something about it then
        factory.createVector(new float[1], 2, 0, 1, Direction.ROW);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructorNegativeSize() {
        factory.createVector(-1, Direction.DEFAULT);
    }

    @Test
    public void testConstructors() {
        factory.createVector(0, Direction.DEFAULT);
        factory.createVector(1, Direction.DEFAULT);
        factory.createVector(10, Direction.DEFAULT);
        factory.createVector(new float[0], 0, 0, 1, Direction.ROW);
        factory.createVector(new float[1], 1, 0, 1, Direction.ROW);
        // a really long constant vector
        factory.createVector(new float[1], Integer.MAX_VALUE, 0, 0, Direction.ROW);
        // TODO test for overflows in checkArgument checks
    }

    @Test
    public void testFill() {
        for (int i = 0; i < 5; i++) {
            FloatVector x = factory.createVector(i, Direction.DEFAULT);
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
            FloatVector x = factory.createVector(i, Direction.DEFAULT);
            FloatVector y = factory.createVector(i, Direction.DEFAULT);
            for (int j = 0; j < i; j++) {
                x.set(j, j);
                y.set(j, i - j);
            }
            FloatVector z = x.minus(y);
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
            FloatVector x = factory.createVector(i, Direction.DEFAULT);
            FloatVector y = factory.createVector(i, Direction.DEFAULT);
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
        final FloatVector vector = factory.createVector(10, Direction.DEFAULT);
        assertTrue("Vector must be a column vector by default", vector.isColumnVector());
        final FloatVector transposedVector = vector.transpose();
        assertTrue(transposedVector.isRowVector());
        assertFalse(vector.equals(transposedVector));
        final FloatVector originalVector = transposedVector.transpose();
        assertTrue(originalVector.isColumnVector());
        assertEquals(vector, originalVector);
    }
}
