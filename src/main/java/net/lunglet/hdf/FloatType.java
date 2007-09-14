package net.lunglet.hdf;

public final class FloatType extends AtomType<FloatType> {
    public static final FloatType IEEE_F32LE = new FloatType(H5Library.H5T_IEEE_F32LE, true);

    public static final FloatType IEEE_F64LE = new FloatType(H5Library.H5T_IEEE_F64LE, true);

    public static final FloatType NATIVE_FLOAT = new FloatType(H5Library.H5T_NATIVE_FLOAT, true);

    FloatType(final int id, final boolean predefined) {
        super(id, predefined);
    }

    @Override
    public FloatType copy() {
        int id = H5Library.INSTANCE.H5Tcopy(getId());
        if (id < 0) {
            throw new H5DataTypeException("H5Tcopy failed");
        }
        return new FloatType(id, false);
    }
}
