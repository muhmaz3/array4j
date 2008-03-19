package net.lunglet.array4j;

/**
 * Vector direction.
 */
public enum Direction {
    COLUMN {
        @Override
        public Order order() {
            return Order.COLUMN;
        }

        @Override
        public Direction transpose() {
            return ROW;
        }
    },
    ROW {
        @Override
        public Order order() {
            return Order.ROW;
        }

        @Override
        public Direction transpose() {
            return COLUMN;
        }
    };

    public static final Direction DEFAULT = COLUMN;

    public abstract Order order();

    /** Check if all directions are the same. */
    public boolean same(final Direction... directions) {
        for (Direction other : directions) {
            if (!equals(other)) {
                return false;
            }
        }
        return true;
    }

    public abstract Direction transpose();
}
