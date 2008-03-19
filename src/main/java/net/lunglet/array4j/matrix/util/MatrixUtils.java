package net.lunglet.array4j.matrix.util;

import net.lunglet.array4j.Direction;
import net.lunglet.array4j.matrix.Vector;

// TODO implement unmodifiableMatrix here?

/**
 * This class contains various utility methods for matrices.
 */
public final class MatrixUtils {
    public static boolean sameDirection(final Vector... vectors) {
        if (vectors.length == 0) {
            return true;
        }
        Direction[] directions = new Direction[vectors.length];
        for (int i = 0; i < vectors.length; i++) {
            directions[i] = vectors[i].direction();
        }
        return directions[0].same(directions);
    }

    private MatrixUtils() {
    }
}
