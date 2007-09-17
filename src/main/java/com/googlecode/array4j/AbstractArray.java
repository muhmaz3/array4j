package com.googlecode.array4j;

import com.googlecode.array4j.util.AssertUtils;
import java.util.Arrays;

import org.apache.commons.lang.builder.EqualsBuilder;

// TODO if we go for FloatDenseMatrix and FloatDenseVector as
// interfaces, we can move length method up to AbstractMatrix

public abstract class AbstractArray<A extends Array<A>> implements Array<A> {
    protected final int length;

    protected final int[] shape;

    public AbstractArray(final int[] shape) {
        long length = 1;
        for (int dim : shape) {
            AssertUtils.checkArgument(dim >= 0);
            length *= dim;
            if (length < 0 || length > Integer.MAX_VALUE) {
                throw new IllegalArgumentException();
            }
        }
        this.shape = Arrays.copyOf(shape, shape.length);
        this.length = (int) length;
    }

    protected final void checkIndex(final int index) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException(String.format("Index out of bounds [0,%d): %d", length, index));
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
        // length is calculated from shape, so only check shape
        return new EqualsBuilder().append(shape, other.shape).isEquals();
    }

    public final int length() {
        return length;
    }

    public final int[] shape() {
        return Arrays.copyOf(shape, shape.length);
    }
}
