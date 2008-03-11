package net.lunglet.array4j.math;

public final class ArraysMath {
    public static int max(final int[] array) {
        if (array.length == 0) {
            throw new IllegalArgumentException();
        }
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    public static short max(final short[] array) {
        if (array.length == 0) {
            throw new IllegalArgumentException();
        }
        short max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    public static short min(final short[] array) {
        if (array.length == 0) {
            throw new IllegalArgumentException();
        }
        short min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    public static double[] square(final double[] x) {
        double[] xx = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            double v = x[i];
            xx[i] = v * v;
        }
        return xx;
    }

    public static float[] square(final float[] x) {
        float[] xx = new float[x.length];
        for (int i = 0; i < x.length; i++) {
            float v = x[i];
            xx[i] = v * v;
        }
        return xx;
    }

    private ArraysMath() {
    }
}
