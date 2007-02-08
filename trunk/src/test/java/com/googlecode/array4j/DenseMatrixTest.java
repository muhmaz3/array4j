package com.googlecode.array4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

public final class DenseMatrixTest {
    @Test
    public void testValueOf() {
        final double[][] values = new double[2][];
        values[0] = new double[]{1.0, 2.0};
        values[1] = new double[]{3.0, 4.0};
        final DenseMatrix mat = DenseMatrix.valueOf(values);
        assertNotNull(mat);
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                assertEquals(values[i][j], mat.get(i, j));
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfInvalidValues() {
        final double[][] values = new double[2][];
        values[0] = new double[]{1.0, 2.0};
        values[1] = new double[]{3.0};
        DenseMatrix.valueOf(values);
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DenseMatrixTest.class);
    }
}
