package com.googlecode.array4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

// TODO test DenseFloatVector as a FloatMatrix to make sure it doesn't break anything

public final class DenseFloatVectorTest {
    @Test(expected=IllegalArgumentException.class)
    public void testConstructorDataTooSmall() {
        new DenseFloatVector(new float[1], 2, 0, 1, Orientation.ROW);
    }

    @Test(expected=NegativeArraySizeException.class)
    public void testConstructorNegativeSize() {
        new DenseFloatVector(-1);
    }

    @Test
    public void testConstructors() {
       new DenseFloatVector();
       new DenseFloatVector(0);
       new DenseFloatVector(1);
       new DenseFloatVector(10);
       new DenseFloatVector(new float[0], 0, 0, 1, Orientation.ROW);
       new DenseFloatVector(new float[1], 1, 0, 1, Orientation.ROW);
       // a really long constant vector
       new DenseFloatVector(new float[1], Integer.MAX_VALUE, 0, 0, Orientation.ROW);
       // TODO test for overflows in checkArgument checks
    }

    @Test
    public void testTranspose() {
        final DenseFloatVector vector = new DenseFloatVector(10);
        assertTrue("Vector must be a column vector by default", vector.isColumnVector());
        final DenseFloatVector transposedVector = vector.transpose();
        assertTrue(transposedVector.isRowVector());
        assertFalse(vector.equals(transposedVector));
        final DenseFloatVector originalVector = transposedVector.transpose();
        assertTrue(originalVector.isColumnVector());
        assertEquals(vector, originalVector);
    }
}
