package com.googlecode.array4j;

import java.util.Arrays;

import org.apache.commons.lang.builder.EqualsBuilder;

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

    protected final int[] shape;

    // TODO rename to length
    protected final int size;

    public AbstractArray(final int[] shape) {
        int size = 1;
        for (int dim : shape) {
            checkArgument(dim >= 0);
            size *= dim;
        }
        this.shape = Arrays.copyOf(shape, shape.length);
        this.size = size;
    }

    protected final void checkIndex(final int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(String.format("Index out of bounds [0,%d): %d", size, index));
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof AbstractArray)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        AbstractArray<?> other = (AbstractArray<?>) obj;
        return new EqualsBuilder().appendSuper(super.equals(obj)).append(size, other.size).isEquals();
    }

    public final int size() {
        return size;
    }

    public final int[] shape() {
        return Arrays.copyOf(shape, shape.length);
    }
}
