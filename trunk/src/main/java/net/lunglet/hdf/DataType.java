package net.lunglet.hdf;

public class DataType extends H5Object {
    private final boolean predefined;

    DataType(final int id, final boolean predefined) {
        super(id);
        this.predefined = predefined;
    }

    public final void close() {
        // If this datatype is not a predefined type, call H5Tclose on it.
        if (!predefined) {
            int err = H5Library.INSTANCE.H5Tclose(getId());
            if (err < 0) {
                throw new H5DataTypeException("H5Tclose failed");
            }
            invalidate();
        } else {
            // cannot close a predefined type
            throw new H5DataTypeException("Cannot close a predefined type");
        }
    }

    final void commit(final Group group, final String name) {
        // Call C routine to commit the transient datatype
        int err = H5Library.INSTANCE.H5Tcommit(group.getId(), name, getId());
        if (err < 0) {
            throw new H5DataTypeException("H5Tcommit failed");
        }
    }

    final boolean committed() {
        // Call C function to determine if a datatype is a named one
        int committed = H5Library.INSTANCE.H5Tcommitted(getId());
        if (committed > 0) {
            return true;
        } else if (committed == 0) {
            return false;
        } else {
            throw new RuntimeException("H5Tcommitted return negative value");
        }
    }

    public final long getSize() {
        // TODO do we need error checking here?
        return H5Library.INSTANCE.H5Tget_size(getId());
    }
}
