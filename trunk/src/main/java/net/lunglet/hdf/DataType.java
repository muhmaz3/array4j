package net.lunglet.hdf;

import com.sun.jna.NativeLong;
import java.util.HashSet;
import java.util.Set;

// TODO implement createTypeFromId with a TreeSet? will need Comparable on IdComponent

public class DataType extends H5Object {
    private static final CloseAction CLOSE_ACTION = new CloseAction() {
        @Override
        public void close(final int id) {
            int err = H5Library.INSTANCE.H5Tclose(id);
            if (err < 0) {
                throw new H5DataTypeException("H5Tclose failed");
            }
        }
    };

    static DataType createTypeFromId(final int id) {
        if (id < 0) {
            throw new IllegalArgumentException();
        }
        // TODO this set should be static, but if we put it in this class, some
        // of the fields might still be null when initializing it
        Set<DataType> types = new HashSet<DataType>();
        types.add(FloatType.IEEE_F32LE);
        types.add(FloatType.IEEE_F64LE);
        types.add(FloatType.NATIVE_FLOAT);
        types.add(IntType.STD_I32LE);
        types.add(IntType.STD_I8BE);
        types.add(StringType.C_S1);
        for (DataType predefinedType : types) {
            final int tri;
            synchronized (H5Library.INSTANCE) {
                tri = H5Library.INSTANCE.H5Tequal(id, predefinedType.getId());
                if (tri < 0) {
                    throw new H5DataTypeException("H5Tequal failed");
                }
            }
            if (tri > 0) {
                CLOSE_ACTION.close(id);
                return predefinedType;
            }
        }
        DataType dtype = null;
        try {
            final int dtypeClass;
            synchronized (H5Library.INSTANCE) {
                dtypeClass = H5Library.INSTANCE.H5Tget_class(id);
                if (dtypeClass < 0) {
                    throw new H5DataTypeException("H5Tget_class failed");
                }
            }
            if (dtypeClass == DataTypeClass.STRING.intValue()) {
                dtype = new StringType(id, false);
            }
            throw new AssertionError();
        } finally {
            if (dtype != null) {
                return dtype;
            }
            CLOSE_ACTION.close(id);
        }
    }

    DataType(final int id, final boolean predefined) {
        super(id, predefined ? null : CLOSE_ACTION);
    }

    final void commit(final Group group, final String name) {
        synchronized (H5Library.INSTANCE) {
            int err = H5Library.INSTANCE.H5Tcommit(group.getId(), name, getId());
            if (err < 0) {
                throw new H5DataTypeException("H5Tcommit failed");
            }
        }
    }

    final boolean committed() {
        final int committed;
        synchronized (H5Library.INSTANCE) {
            committed = H5Library.INSTANCE.H5Tcommitted(getId());
        }
        if (committed > 0) {
            return true;
        } else if (committed == 0) {
            return false;
        } else {
            throw new H5DataTypeException("H5Tcommitted failed");
        }
    }

    public final long getSize() {
        final NativeLong size;
        synchronized (H5Library.INSTANCE) {
            size = H5Library.INSTANCE.H5Tget_size(getId());
            if (size.longValue() == 0) {
                throw new H5DataTypeException("H5Tget_size failed");
            }
        }
        return size.longValue();
    }
}
