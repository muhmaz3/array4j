package net.lunglet.hdf;

public final class IntType extends AtomType<IntType> {
    public static final IntType STD_I32LE = new IntType(H5Library.H5T_STD_I32LE, true);

    public static final IntType STD_I8BE = new IntType(H5Library.H5T_STD_I8BE, true);

    IntType(final int id, final boolean predefined) {
        super(id, predefined);
    }

    @Override
    public IntType copy() {
        int id = H5Library.INSTANCE.H5Tcopy(getId());
        if (id < 0) {
            throw new H5DataTypeException("H5Tcopy failed");
        }
        return new IntType(id, false);
    }
}
