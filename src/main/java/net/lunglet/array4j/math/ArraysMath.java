package net.lunglet.array4j.math;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

public final class ArraysMath {
    public static int[] argmaxn(final double[] x, final int n) {
        if (n < 0 || n > x.length) {
            throw new IllegalArgumentException();
        }
        TreeSet<Integer> indicesSet = new TreeSet<Integer>(new Comparator<Integer>() {
            @Override
            public int compare(final Integer o1, final Integer o2) {
                return Double.compare(x[o1], x[o2]);
            }
        });
        for (int i = 0; i < x.length; i++) {
            indicesSet.add(i);
        }
        int[] indices = new int[n];
        Iterator<Integer> iter = indicesSet.descendingIterator();
        for (int i = 0; i < indices.length; i++) {
            indices[i] = iter.next();
        }
        return indices;
    }

    public static double max(final double[] array) {
        if (array.length == 0) {
            throw new IllegalArgumentException();
        }
        double max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

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
