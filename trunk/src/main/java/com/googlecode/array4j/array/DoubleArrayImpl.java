package com.googlecode.array4j.array;

import java.nio.DoubleBuffer;
import net.jcip.annotations.NotThreadSafe;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@NotThreadSafe
final class DoubleArrayImpl extends AbstractArray implements DoubleArray {
    private static final long serialVersionUID = 1L;

    private transient DoubleBuffer data;

    public DoubleArrayImpl(final int... shape) {
        super(shape);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof DoubleArrayImpl)) {
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
}
