package net.lunglet.util;

public final class NumberUtils {
    public static int castLongToInt(final long i) {
        if (i < Integer.MIN_VALUE || i > Integer.MAX_VALUE) {
            throw new IllegalArgumentException();
        }
        return (int) i;
    }

    public boolean isFinite(final double v) {
        return !Double.isInfinite(v) && !Double.isNaN(v);
    }

    public boolean isFinite(final float v) {
        return !Float.isInfinite(v) && !Float.isNaN(v);
    }
}
