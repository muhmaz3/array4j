package com.googlecode.array4j.matrix.dense;

import com.googlecode.array4j.Direction;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.matrix.FloatVector;
import net.jcip.annotations.NotThreadSafe;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@NotThreadSafe
final class FloatDenseVectorImpl extends AbstractFloatDense implements FloatDenseVector {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for internal use.
     */
    FloatDenseVectorImpl(final AbstractFloatDense base, final int length, final int offset, final int stride,
            final Direction direction) {
        super(base, length, offset, stride, direction);
    }

    /**
     * Copy constructor.
     */
    public FloatDenseVectorImpl(final FloatVector other) {
        this(other.length(), other.direction(), storageLike(other));
        copy(other, this);
    }

    /**
     * Construct with specified length, direction and storage.
     */
    public FloatDenseVectorImpl(final int length, final Direction direction, final Storage storage) {
        super(length, direction, storage);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof FloatDenseVectorImpl)) {
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

    /** {@inheritDoc} */
    @Override
    public FloatDenseVector transpose() {
        return new FloatDenseVectorImpl(this, length, offset, stride, direction().transpose());
    }
}
