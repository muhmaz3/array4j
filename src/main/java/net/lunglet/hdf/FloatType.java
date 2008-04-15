package net.lunglet.hdf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FloatType extends AtomType<FloatType> {
    public static final FloatType IEEE_F32LE = new FloatType(H5Library.H5T_IEEE_F32LE, true);

    public static final FloatType IEEE_F64LE = new FloatType(H5Library.H5T_IEEE_F64LE, true);

    public static final FloatType NATIVE_FLOAT = new FloatType(H5Library.H5T_NATIVE_FLOAT, true);

    private final Logger logger = LoggerFactory.getLogger(FloatType.class);

    FloatType(final int id, final boolean predefined) {
        super(id, predefined);
        logger.debug("Created [id={}]", getId());
    }

    @Override
    public FloatType copy() {
        final int id;
        synchronized (H5Library.INSTANCE) {
            id = H5Library.INSTANCE.H5Tcopy(getId());
            if (id < 0) {
                throw new H5DataTypeException("H5Tcopy failed");
            }
        }
        return new FloatType(id, false);
    }
}
