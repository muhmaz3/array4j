package com.googlecode.array4j;

public final class VectorSupport {
    // TODO get rid of this duplication
    private static void checkArgument(final boolean condition) {
        if (!condition) {
            throw new IllegalArgumentException();
        }
    }

    public static int columns(final int size, final Orientation orientation) {
        checkArgument(size >= 0);
        if (orientation.equals(Orientation.ROW)) {
            return size;
        } else {
            if (size == 0) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    public static int rows(final int size, final Orientation orientation) {
        checkArgument(size >= 0);
        if (orientation.equals(Orientation.COLUMN)) {
            return size;
        } else {
            if (size == 0) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    private VectorSupport() {
    }
}
