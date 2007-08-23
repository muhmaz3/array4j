package net.lunglet.hdf;

public final class PredefinedType extends AtomType {
    public static final PredefinedType IEEE_F32LE = new PredefinedType(H5Library.H5T_IEEE_F32LE);

    public static final PredefinedType IEEE_F64LE = new PredefinedType(H5Library.H5T_IEEE_F64LE);

    public static final PredefinedType NATIVE_FLOAT = new PredefinedType(H5Library.H5T_NATIVE_FLOAT);

    public static final PredefinedType STD_I32LE = new PredefinedType(H5Library.H5T_STD_I32LE);

    public static final PredefinedType STD_I8BE = new PredefinedType(H5Library.H5T_STD_I8BE);

    private PredefinedType(final int id) {
        super(id, true);
    }
}
