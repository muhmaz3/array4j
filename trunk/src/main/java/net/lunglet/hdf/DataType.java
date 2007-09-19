package net.lunglet.hdf;

import java.util.HashSet;
import java.util.Set;

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
        for (DataType dtype : types) {
            int tri = H5Library.INSTANCE.H5Tequal(id, dtype.getId());
            if (tri < 0) {
                throw new H5DataTypeException("H5Tequal failed");
            }
            if (tri > 0) {
                return dtype;
            }
        }
        int dtypeClass = H5Library.INSTANCE.H5Tget_class(id);
        if (dtypeClass < 0) {
            throw new H5DataTypeException("H5Tget_class failed");
        }
        if (dtypeClass == DataTypeClass.STRING.intValue()) {
            return new StringType(id, false);
        }
        throw new AssertionError();
    }

    DataType(final int id, final boolean predefined) {
        super(id, predefined ? null : CLOSE_ACTION);
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
            throw new RuntimeException("H5Tcommitted failed");
        }
    }

    public final int getSize() {
        int size = H5Library.INSTANCE.H5Tget_size(getId());
        if (size == 0) {
            throw new H5DataTypeException("H5Tget_size failed");
        }
        return size;
    }
}
