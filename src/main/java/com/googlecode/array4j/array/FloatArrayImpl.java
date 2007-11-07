package com.googlecode.array4j.array;

import com.googlecode.array4j.AbstractArray;
import com.googlecode.array4j.FloatArray;
import java.nio.FloatBuffer;
import net.jcip.annotations.NotThreadSafe;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@NotThreadSafe
final class FloatArrayImpl extends AbstractArray implements FloatArray {
    private static final long serialVersionUID = 1L;

    private transient FloatBuffer data;

    public FloatArrayImpl(final int... shape) {
        super(shape);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof FloatArrayImpl)) {
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
