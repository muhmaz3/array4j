package com.googlecode.array4j;

public enum Order {
    COLUMN {
        @Override
        public Order transpose() {
            return ROW;
        }
    },
    ROW {
        @Override
        public Order transpose() {
            return COLUMN;
        }
    };

    public static final Order DEFAULT = COLUMN;

    public boolean same(final Order... orientations) {
        for (Order other : orientations) {
            if (!equals(other)) {
                return false;
            }
        }
        return true;
    }

    public abstract Order transpose();
}
