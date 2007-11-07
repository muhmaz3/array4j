package com.googlecode.array4j;

/**
 * Vector direction.
 */
public enum Direction {
    BOTH {
        @Override
        public Direction transpose() {
            return this;
        }
    },
    COLUMN {
        @Override
        public Direction transpose() {
            return ROW;
        }
    },
    ROW {
        @Override
        public Direction transpose() {
            return COLUMN;
        }
    };

    public static final Direction DEFAULT = COLUMN;

    /** Check if all directions are the same. */
    public boolean same(final Direction... directions) {
        for (Direction other : directions) {
            if (!equals(other) && !equals(BOTH) && !other.equals(BOTH)) {
                return false;
            }
        }
        return true;
    }

    public abstract Direction transpose();
}
