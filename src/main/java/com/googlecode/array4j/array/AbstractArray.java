package com.googlecode.array4j.array;

import com.googlecode.array4j.util.AssertUtils;
import java.util.Arrays;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Abstract base class for multidimensional arrays.
 */
public abstract class AbstractArray implements Array {
    private static final long serialVersionUID = 1L;

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
        AbstractArray other = (AbstractArray) obj;
        return new EqualsBuilder().append(shape, other.shape).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(shape).toHashCode();
    }

    protected final int length() {
        return length;
    }

    public final int[] shape() {
        return Arrays.copyOf(shape, shape.length);
    }
}
