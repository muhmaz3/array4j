package com.googlecode.array4j;

/**
 * Storage order.
 */
public enum Order {
    /** Column-major storage order. */
    COLUMN {
        @Override
        public Direction direction() {
            return Direction.COLUMN;
        }

        @Override
        public Order transpose() {
            return ROW;
        }
    },
    /** Row-major storage order. */
    ROW {
        @Override
        public Direction direction() {
            return Direction.ROW;
        }

        @Override
        public Order transpose() {
            return COLUMN;
        }
    };

    public static final Order DEFAULT = COLUMN;

    public abstract Direction direction();

    /** Check if all storage orders are the same. */
    public boolean same(final Order... orders) {
        for (Order other : orders) {
            if (!equals(other)) {
                return false;
            }
        }
        return true;
    }

    public abstract Order transpose();
}
