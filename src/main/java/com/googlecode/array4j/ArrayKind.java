package com.googlecode.array4j;

public enum ArrayKind {
    GENBOOL {
        @Override
        public boolean isAssignableFrom(final ArrayType type) {
            return false;
        }
    },
    SIGNED {
        @Override
        public boolean isAssignableFrom(final ArrayType type) {
            return false;
        }
    },
    UNSIGNED {
        @Override
        public boolean isAssignableFrom(final ArrayType type) {
            return false;
        }
    },
    FLOATING {
        @Override
        public boolean isAssignableFrom(final ArrayType type) {
            return false;
        }
    },
    COMPLEX {
        @Override
        public boolean isAssignableFrom(final ArrayType type) {
            return false;
        }
    };

    public abstract boolean isAssignableFrom(ArrayType type);
}
