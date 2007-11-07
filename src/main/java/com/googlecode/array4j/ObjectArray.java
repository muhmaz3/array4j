package com.googlecode.array4j;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

// TODO implement BigIntegerArray on top of ObjectArray

// TODO implement BigDecimalArray on top of ObjectArray

// TODO possibly derive them from AbstractNumberArray<E extends Number>

public class ObjectArray<T> extends AbstractArray {
    private static final long serialVersionUID = 1L;

    public ObjectArray(final int[] shape) {
        super(shape);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof ObjectArray)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        return new EqualsBuilder().appendSuper(super.equals(obj)).isEquals();
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).toHashCode();
    }

    public final T[] toArray() {
        throw new UnsupportedOperationException();
    }
}
