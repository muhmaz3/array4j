package net.lunglet.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

public final class ArrayUtilsTest {
    @Test
    public void testCopyOf() {
        float[][] values = {{1.0f, 2.0f}, {3.0f, 4.0f, 5.0f}};
        float[][] values2 = ArrayUtils.copyOf(values, values.length);
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                float expected = values[i][j];
                assertEquals(expected, values2[i][j], 0);
                values[i][j] *= 10.0f;
                // check that modification of original didn't change copy
                assertEquals(expected, values2[i][j], 0);
            }
            values[i] = null;
            assertNotNull(values2[i]);
        }
    }
}
