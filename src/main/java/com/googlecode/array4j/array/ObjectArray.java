package com.googlecode.array4j.array;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

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
