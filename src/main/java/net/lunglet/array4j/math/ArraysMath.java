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

    private ArraysMath() {
    }
}
