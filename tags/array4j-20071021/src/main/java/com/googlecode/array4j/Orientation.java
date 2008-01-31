package com.googlecode.array4j;

// TODO rename to Order and split out the vector stuff

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

    public static final Orientation DEFAULT = COLUMN;

    public static final Orientation DEFAULT_FOR_VECTOR = COLUMN;

    public boolean same(final Orientation... orientations) {
        for (Orientation other : orientations) {
            if (!equals(other)) {
                return false;
            }
        }
        return true;
    }

    public abstract Orientation transpose();
}