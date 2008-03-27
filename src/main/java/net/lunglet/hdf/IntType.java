package net.lunglet.hdf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class IntType extends AtomType<IntType> {
    public static final IntType STD_I32LE = new IntType(H5Library.H5T_STD_I32LE, true);

    public static final IntType STD_I8BE = new IntType(H5Library.H5T_STD_I8BE, true);

    private final Logger logger = LoggerFactory.getLogger(IntType.class);

    IntType(final int id, final boolean predefined) {
        super(id, predefined);
        logger.info("Created [id={}]", getId());
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
