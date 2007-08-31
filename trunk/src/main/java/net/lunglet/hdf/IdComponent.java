package net.lunglet.hdf;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

abstract class IdComponent {
    private final int id;

    private boolean valid;

    public IdComponent(final int id) {
        this.id = id;
        this.valid = true;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof IdComponent)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        IdComponent other = (IdComponent) obj;
        return new EqualsBuilder().append(getId(), other.getId()).isEquals();
    }

    protected final int getId() {
        if (!valid) {
            throw new IllegalStateException();
        }
        return id;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    protected final void invalidate() {
        this.valid = false;
    }

    protected final boolean isValid() {
        return valid;
    }
}
