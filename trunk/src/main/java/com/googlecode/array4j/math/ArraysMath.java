package com.googlecode.array4j.math;

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

    private ArraysMath() {
    }
}
