package net.lunglet.hdf;

public final class StringType extends AtomType<StringType> {
    public static final StringType C_S1 = new StringType(H5Library.H5T_C_S1, true);

    StringType(final int id, final boolean predefined) {
        super(id, predefined);
    }

    @Override
    public StringType copy() {
        int id = H5Library.INSTANCE.H5Tcopy(getId());
        if (id < 0) {
            throw new H5DataTypeException("H5Tcopy failed");
        }
        return new StringType(id, false);
    }
}
