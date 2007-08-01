package com.googlecode.array4j;

public enum Orientation {
    COLUMN {
        @Override
        public Orientation transpose() {
            return ROW;
        }
    },
    ROW {
        @Override
        public Orientation transpose() {
            return COLUMN;
        }
    };

    public static final Orientation DEFAULT = ROW;

    public static final Orientation DEFAULT_FOR_VECTOR = COLUMN;

    public abstract Orientation transpose();

    public boolean same(final Orientation... orientations) {
        for (Orientation other : orientations) {
            if (!equals(other)) {
                return false;
            }
        }
        return true;
    }
}
