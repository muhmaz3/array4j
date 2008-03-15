package net.lunglet.util;

public final class NumberUtils {
    public boolean isFinite(final double v) {
        return !Double.isInfinite(v) && !Double.isNaN(v);
    }

    public boolean isFinite(final float v) {
        return !Float.isInfinite(v) && !Float.isNaN(v);
    }
}
