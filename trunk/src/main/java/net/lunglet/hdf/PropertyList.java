package net.lunglet.hdf;

abstract class PropertyList extends IdComponent {
    protected enum PropertyListClass {
        H5P_DATASET_CREATE(H5Library.H5P_DATASET_CREATE),
        H5P_DATASET_XFER(H5Library.H5P_DATASET_XFER),
        H5P_FILE_ACCESS(H5Library.H5P_FILE_ACCESS),
        H5P_FILE_CREATE(H5Library.H5P_FILE_CREATE),
        H5P_MOUNT(H5Library.H5P_MOUNT);

        private final int value;

        PropertyListClass(final int value) {
            this.value = value;
        }

        public int intValue() {
            return value;
        }
    }

    protected static int create(final PropertyListClass cls) {
        int id = H5Library.INSTANCE.H5Pcreate(cls.intValue());
        if (id < 0) {
            throw new H5PropertyListException("H5Pcreate failed");
        }
        return id;
    }

    public PropertyList(final int id) {
        super(id);
    }

    public final void close() {
        int err = H5Library.INSTANCE.H5Pclose(getId());
        if (err < 0) {
            throw new H5PropertyListException("H5Pclose failed");
        }
        invalidate();
    }

    @Override
    protected void finalize() throws Throwable {
        if (isValid()) {
            close();
        }
        super.finalize();
    }
}
