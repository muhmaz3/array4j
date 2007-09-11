package net.lunglet.hdf;

public class DataType extends H5Object {
    private final boolean predefined;

    static DataType createTypeFromId(final int id) {
        if (id < 0) {
            throw new IllegalArgumentException();
        }
        DataType dtype = new DataType(id, false);
        for (PredefinedType type : PredefinedType.TYPES) {
            if (dtype.equals(type)) {
                dtype.close();
                return type;
            }
        }
        // TODO support other types
        throw new UnsupportedOperationException();
    }

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

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof DataType)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        DataType other = (DataType) obj;
        int tri = H5Library.INSTANCE.H5Tequal(getId(), other.getId());
        if (tri < 0) {
            throw new H5DataTypeException("H5Tequal failed");
        }
        return tri > 0;
    }

    final void commit(final Group group, final String name) {
        int err = H5Library.INSTANCE.H5Tcommit(group.getId(), name, getId());
        if (err < 0) {
            throw new H5DataTypeException("H5Tcommit failed");
        }
    }

    final boolean committed() {
        int committed = H5Library.INSTANCE.H5Tcommitted(getId());
        if (committed > 0) {
            return true;
        } else if (committed == 0) {
            return false;
        } else {
            throw new RuntimeException("H5Tcommitted return negative value");
        }
    }

    public final int getSize() {
        // TODO do we need error checking here?
        return H5Library.INSTANCE.H5Tget_size(getId());
    }
}
