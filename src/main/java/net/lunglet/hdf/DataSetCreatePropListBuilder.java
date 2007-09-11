package net.lunglet.hdf;

// TODO provide a way to abort the build

public final class DataSetCreatePropListBuilder {
    private static final int H5D_FILL_TIME_ERROR = -1;

    private static final int H5D_FILL_TIME_ALLOC = 0;

    private static final int H5D_FILL_TIME_NEVER = 1;

    private static final int H5D_FILL_TIME_IFSET = 2;

    public enum FillTime {
        IFSET(H5D_FILL_TIME_IFSET), ALLOC(H5D_FILL_TIME_ALLOC), NEVER(H5D_FILL_TIME_NEVER);

        private final int value;

        FillTime(final int value) {
            this.value = value;
        }

        public int intValue() {
            return value;
        }
    }

    public enum Layout {
        COMPACT,
        CONTIGUOUS,
        CHUNKED
    }

    private final DataSetCreatePropList propList;

    public DataSetCreatePropListBuilder() {
        this.propList = new DataSetCreatePropList();
    }

    public DataSetCreatePropList build() {
        return propList;
    }

    public DataSetCreatePropListBuilder setFillTime(final FillTime fillTime) {
        int err = H5Library.INSTANCE.H5Pset_fill_time(propList.getId(), fillTime.intValue());
        if (err < 0) {
            throw new H5PropertyListException("H5Pset_fill_time failed");
        }
        return this;
    }
}
