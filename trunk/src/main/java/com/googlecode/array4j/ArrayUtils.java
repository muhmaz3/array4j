package com.googlecode.array4j;

public final class ArrayUtils {
    private ArrayUtils() {
    }

    public static void checkExactly2d(final Array array) {
        if (array.ndim() != 2) {
            throw new IllegalArgumentException("array must be 2-d");
        }
    }
}
