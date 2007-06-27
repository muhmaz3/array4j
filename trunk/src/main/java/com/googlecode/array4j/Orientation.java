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

    public abstract Orientation transpose();
}
