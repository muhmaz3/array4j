package net.lunglet.hdf;

public final class DataSpace extends IdComponent {
    private static final int H5S_ALL = 0;

    public static final DataSpace ALL = new DataSpace(H5S_ALL);

    DataSpace(final int type) {
        super(init(type));
    }

    DataSpace(final int[] dims) {
        this(dims, null);
    }

    DataSpace(final int[] dims, final int[] maxdims) {
        super(init(dims, maxdims));
    }

    public void close() {
        if (id != H5S_ALL) {
            // not a constant, should call H5Sclose
            int err = H5Library.INSTANCE.H5Sclose(id);
            if (err < 0) {
                throw new H5DataSpaceException("H5Sclose failed");
            }
            // reset id because the dataspace that it represents is now closed
            id = 0;
        } else {
            // cannot close a constant
            throw new H5DataSpaceException("Cannot close a constant");
        }
    }

    private static int init(final int[] dims, final int[] maxdims) {
        if (maxdims != null && dims.length != maxdims.length) {
            throw new IllegalArgumentException();
        }
        int rank = dims.length;
        int id = H5Library.INSTANCE.H5Screate_simple(rank, dims, maxdims);
        if (id < 0) {
            throw new H5DataSpaceException("H5Screate_simple failed");
        }
        return id;
    }

    private static int init(final int type) {
        int id = H5Library.INSTANCE.H5Screate(type);
        if (id < 0) {
            throw new H5DataSpaceException("H5Screate failed");
        }
        return id;
    }
}
