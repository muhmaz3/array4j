package net.lunglet.util;

import static org.junit.Assert.assertEquals;
import java.util.Random;
import net.lunglet.util.ArrayMath;
import net.lunglet.util.ArrayUtils;
import org.junit.Test;

// TODO test argmaxn with infs and NaNs

public final class ArrayMathTest {
    @Test
    public void testArgMaxN() {
        double[] values = {2.0f, 1.0f, 3.0f};
        int[] i0 = ArrayMath.argmaxn(values, 0);
        assertEquals(0, i0.length);
        int[] i1 = ArrayMath.argmaxn(values, 1);
        assertEquals(1, i1.length);
        assertEquals(2, i1[0]);
        int[] i2 = ArrayMath.argmaxn(values, 2);
        assertEquals(2, i2.length);
        assertEquals(2, i2[0]);
        assertEquals(0, i2[1]);
        int[] i3 = ArrayMath.argmaxn(values, 3);
        assertEquals(3, i3.length);
        assertEquals(2, i3[0]);
        assertEquals(0, i3[1]);
        assertEquals(1, i3[2]);
        values = new double[100];
        ArrayUtils.fillRandom(values, new Random());
        assertEquals(ArrayMath.max(values), values[ArrayMath.argmaxn(values, 1)[0]], 0);
    }
}
