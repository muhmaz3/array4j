package com.googlecode.array4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.googlecode.array4j.DenseFloatVector;
import com.googlecode.array4j.Orientation;

// TODO test DenseFloatVector as a FloatMatrix to make sure it doesn't break anything

public final class DenseFloatVectorTest {
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

    @Test(expected=NegativeArraySizeException.class)
    public void testConstructorNegativeSize() {
        new DenseFloatVector(-1);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructorDataTooSmall() {
        new DenseFloatVector(new float[1], 2, 0, 1, Orientation.ROW);
    }

    @Test
    public void testTranspose() {
        DenseFloatVector vector = new DenseFloatVector(10);
        assertTrue(vector.isRowVector());
        DenseFloatVector transposedVector = vector.transpose();
        assertTrue(transposedVector.isColumnVector());
        assertFalse(vector.equals(transposedVector));
        DenseFloatVector originalVector = transposedVector.transpose();
        assertTrue(originalVector.isRowVector());
        assertEquals(vector, originalVector);
    }
}
