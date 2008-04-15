package net.lunglet.hdf;

import com.sun.jna.NativeLong;

abstract class AtomType<T> extends DataType {
    public AtomType(final int id, final boolean predefined) {
        super(id, predefined);
    }

    public abstract T copy();

    public final void setSize(final int size) {
        synchronized (H5Library.INSTANCE) {
            int err = H5Library.INSTANCE.H5Tset_size(getId(), new NativeLong(size));
            if (err < 0) {
                throw new H5DataTypeException("H5Tset_size failed");
            }
        }
    }
}
