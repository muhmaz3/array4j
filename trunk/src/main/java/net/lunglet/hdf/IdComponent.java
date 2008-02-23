package net.lunglet.hdf;

import java.io.Closeable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

abstract class IdComponent implements Closeable {
    private final NativeIdComponent nativeId;

    public IdComponent(final int id, final CloseAction closeAction) {
        this.nativeId = new NativeIdComponent(this, id, closeAction);
        NativeIdComponent.cleanup();
    }

    public final void close() {
        nativeId.close();
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
        return nativeId.getId();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    protected final boolean isOpen() {
        return nativeId.isOpen();
    }
}
