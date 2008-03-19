package net.lunglet.array4j.math;

import static org.junit.Assert.assertEquals;
import java.util.Random;
import net.lunglet.util.ArrayUtils;
import org.junit.Test;

// TODO test argmaxn with infs and NaNs

public final class ArraysMathTest {
    @Test
    public void testArgMaxN() {
        double[] values = {2.0f, 1.0f, 3.0f};
        int[] i0 = ArraysMath.argmaxn(values, 0);
        assertEquals(0, i0.length);
        int[] i1 = ArraysMath.argmaxn(values, 1);
        assertEquals(1, i1.length);
        assertEquals(2, i1[0]);
        int[] i2 = ArraysMath.argmaxn(values, 2);
        assertEquals(2, i2.length);
        assertEquals(2, i2[0]);
        assertEquals(0, i2[1]);
        int[] i3 = ArraysMath.argmaxn(values, 3);
        assertEquals(3, i3.length);
        assertEquals(2, i3[0]);
        assertEquals(0, i3[1]);
        assertEquals(1, i3[2]);
        values = new double[100];
        ArrayUtils.fillRandom(values, new Random());
        assertEquals(ArraysMath.max(values), values[ArraysMath.argmaxn(values, 1)[0]], 0);
    }
}
