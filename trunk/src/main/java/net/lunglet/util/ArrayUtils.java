package net.lunglet.util;

import java.util.Arrays;

public final class ArrayUtils {
    public static float[][] copyOf(final float[][] x, final int newLength) {
        float[][] y = new float[newLength][];
        for (int i = 0; i < y.length; i++) {
            y[i] = Arrays.copyOf(x[i], x[i].length);
        }
        return y;
    }

    private ArrayUtils() {
    }
}
