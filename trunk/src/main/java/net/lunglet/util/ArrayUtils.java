package net.lunglet.util;

import java.util.Arrays;
import java.util.Random;

public final class ArrayUtils {
    public static int[] castOf(final long[] original) {
        return castOf(original, original.length);
    }

    public static int[] castOf(final long[] original, final int newLength) {
        int[] cast = new int[newLength];
        int length = Math.min(cast.length, original.length);
        for (int i = 0; i < length; i++) {
            long o = original[i];
            if (o < Integer.MIN_VALUE || o > Integer.MAX_VALUE) {
                throw new IllegalArgumentException();
            }
            cast[i] = (int) o;
        }
        return cast;
    }

    public static float[][] copyOf(final float[][] x, final int newLength) {
        float[][] y = new float[newLength][];
        for (int i = 0; i < y.length; i++) {
            if (x[i] != null) {
                y[i] = Arrays.copyOf(x[i], x[i].length);
            }
        }
        return y;
    }

    /**
     * Assigns a random value to each element of the specified array of doubles.
     */
    public static void fillRandom(final double[] x, final Random rng) {
        for (int i = 0; i < x.length; i++) {
            x[i] = rng.nextDouble();
        }
    }

    private ArrayUtils() {
    }
}
