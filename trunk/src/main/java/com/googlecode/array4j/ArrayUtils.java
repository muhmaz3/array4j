package com.googlecode.array4j;

public final class ArrayUtils {
    private ArrayUtils() {
    }

    public static int multiplyList(final int[] arr) {
        int s = 1;
        for (int l1 : arr) {
            s *= l1;
        }
        return s;
    }

    public static void checkExactly2d(final Array<?> array) {
        if (array.ndim() != 2) {
            throw new IllegalArgumentException("array must be 2-d");
        }
    }
}
