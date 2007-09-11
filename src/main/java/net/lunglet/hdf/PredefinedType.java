package net.lunglet.hdf;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class PredefinedType extends AtomType {
    public static final PredefinedType IEEE_F32LE;

    public static final PredefinedType IEEE_F64LE;

    public static final PredefinedType NATIVE_FLOAT;

    public static final PredefinedType STD_I32LE;

    public static final PredefinedType STD_I8BE;

    public static final Set<PredefinedType> TYPES;

    static {
        Set<PredefinedType> types = new HashSet<PredefinedType>();
        IEEE_F32LE = new PredefinedType(H5Library.H5T_IEEE_F32LE);
        types.add(IEEE_F32LE);
        IEEE_F64LE = new PredefinedType(H5Library.H5T_IEEE_F64LE);
        types.add(IEEE_F64LE);
        NATIVE_FLOAT = new PredefinedType(H5Library.H5T_NATIVE_FLOAT);
        types.add(NATIVE_FLOAT);
        STD_I32LE = new PredefinedType(H5Library.H5T_STD_I32LE);
        types.add(STD_I32LE);
        STD_I8BE = new PredefinedType(H5Library.H5T_STD_I8BE);
        types.add(STD_I8BE);
        TYPES = Collections.unmodifiableSet(types);
    }

    private PredefinedType(final int id) {
        super(id, true);
    }
}
