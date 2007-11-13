package com.googlecode.array4j.dense;

import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.Order;
import com.googlecode.array4j.Storage;
import net.jcip.annotations.NotThreadSafe;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@NotThreadSafe
final class FloatDenseMatrixImpl extends AbstractFloatDense implements FloatDenseMatrix {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for internal use.
     */
    FloatDenseMatrixImpl(final AbstractFloatDense base, final int rows, final int columns, final int offset,
            final int stride, final Order order) {
        super(base, rows, columns, offset, stride, order);
    }

    /**
     * Copy constructor.
     */
    public FloatDenseMatrixImpl(final FloatMatrix other) {
        this(other.rows(), other.columns(), orderLike(other), storageLike(other));
        copy(other, this);
    }

    /**
     * Construct with specified dimensions, order and storage.
     */
    public FloatDenseMatrixImpl(final int rows, final int columns, final Order order, final Storage storage) {
        super(rows, columns, order, storage);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof FloatDenseMatrixImpl)) {
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
    public FloatDenseMatrix transpose() {
        return new FloatDenseMatrixImpl(this, columns, rows, offset, stride, order.transpose());
    }
}
