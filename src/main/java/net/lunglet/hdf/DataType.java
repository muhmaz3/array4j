package net.lunglet.hdf;

public class DataType extends AbstractH5Object {
    private final boolean predefined;

    DataType(final int id, final boolean predefined) {
        super(id);
        this.predefined = predefined;
    }

    public final void close() {
        // If this datatype is not a predefined type, call H5Tclose on it.
        if (!predefined) {
            int err = H5Library.INSTANCE.H5Tclose(id);
            if (err < 0) {
                throw new H5DataTypeException("H5Tclose failed");
            }
            // reset the id because the datatype that it represents is now
            // closed
            id = 0;
        } else {
            // cannot close a predefined type
            throw new H5DataTypeException("Cannot close a predefined type");
        }
    }

    final boolean committed() {
        // Call C function to determine if a datatype is a named one
        int committed = H5Library.INSTANCE.H5Tcommitted(id);
        if (committed > 0) {
            return true;
        } else if (committed == 0) {
            return false;
        } else {
            throw new RuntimeException("H5Tcommitted return negative value");
        }
    }

    final void commit(final CommonFG loc, final String name) {
        // get location id for C API
        int locId = loc.getLocId();
        // Call C routine to commit the transient datatype
        int err = H5Library.INSTANCE.H5Tcommit(locId, name, id);
        if (err < 0) {
            throw new H5DataTypeException("H5Tcommit failed");
        }
    }
}
