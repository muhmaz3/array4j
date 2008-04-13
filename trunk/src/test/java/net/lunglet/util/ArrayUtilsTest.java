package net.lunglet.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;
public final class ArrayUtilsTest {
    @Test
    public void testArgSort() {
        float[] a = {3.0f, 1.0f, 2.0f};
        // sort in ascending order
        assertArrayEquals(new int[]{1, 2, 0}, ArrayUtils.argsort(a, true));
        // sort in descending order
        assertArrayEquals(new int[]{0, 2, 1}, ArrayUtils.argsort(a, false));
    }

    @Test
    public void testCopyOf() {
        float[][] values = {{1.0f, 2.0f}, null, {3.0f, 4.0f, 5.0f}};
        float[][] values2 = ArrayUtils.copyOf(values, values.length);
        for (int i = 0; i < values.length; i++) {
            // check that null row is handled properly
            if (values[i] == null) {
                assertNull(values2[i]);
                continue;
            }
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
