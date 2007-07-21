package com.googlecode.array4j;

public abstract class AbstractArray<A extends Array<A>> implements Array<A> {
    protected static final void checkArgument(final boolean condition) {
        if (!condition) {
            throw new IllegalArgumentException();
        }
    }

    protected static final void checkArgument(final boolean condition, final String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    protected static final int checkArgumentNonNegative(final int value) {
        if (value < 0) {
            throw new IllegalArgumentException();
        }
        return value;
    }

    protected final int size;

    public AbstractArray(final int size) {
        checkArgument(size >= 0);
        this.size = size;
    }

    public final int size() {
        return size;
    }
}
