package net.lunglet.hdf;

import java.util.Arrays;

public final class DataSpace extends IdComponent {
    private static final int H5S_ALL = 0;

    private static final int H5S_UNLIMITED = -1;

    public static final DataSpace ALL = new DataSpace(H5S_ALL);

    // TODO use an enum for type
    private static int init(final int type) {
        int id = H5Library.INSTANCE.H5Screate(type);
        if (id < 0) {
            throw new H5DataSpaceException("H5Screate failed");
        }
        return id;
    }

    private static int init(final long[] dims, final long[] maxdims) {
        if (maxdims != null && dims.length != maxdims.length) {
            throw new IllegalArgumentException();
        }
        int rank = dims.length;
        long[] rdims = reverseArray(dims);
        final long[] rmaxdims;
        if (maxdims != null) {
            rmaxdims = reverseArray(maxdims);
        } else {
            rmaxdims = null;
        }
        int id = H5Library.INSTANCE.H5Screate_simple(rank, rdims, rmaxdims);
        if (id < 0) {
            throw new H5DataSpaceException("H5Screate_simple failed");
        }
        return id;
    }

    private static long[] reverseArray(final long[] arr) {
        // TODO might want to reverse inplace instead
        long[] rarr = new long[arr.length];
        for (int i = 0; i < arr.length; i++) {
            rarr[i] = arr[arr.length - i - 1];
        }
        return rarr;
    }

    DataSpace(final int id) {
        super(id);
    }

    public DataSpace(final long[] dims) {
        this(dims, null);
    }

    public DataSpace(final long[] dims, final long[] maxdims) {
        super(init(dims, maxdims));
    }

    public void close() {
        if (getId() != H5S_ALL) {
            // not a constant, should call H5Sclose
            int err = H5Library.INSTANCE.H5Sclose(getId());
            if (err < 0) {
                throw new H5DataSpaceException("H5Sclose failed");
            }
            invalidate();
        } else {
            // cannot close a constant
            throw new H5DataSpaceException("Cannot close a constant");
        }
    }

    @Override
    protected void finalize() throws Throwable {
        if (isValid()) {
            close();
        }
        super.finalize();
    }

    public long[] getDims() {
        long[] dims = new long[getNDims()];
        int err = H5Library.INSTANCE.H5Sget_simple_extent_dims(getId(), dims, null);
        if (err < 0) {
            throw new H5DataSpaceException("H5Sget_simple_extent_dims failed");
        }
        return reverseArray(dims);
    }

    public long[] getMaxDims() {
        long[] maxdims = new long[getNDims()];
        int err = H5Library.INSTANCE.H5Sget_simple_extent_dims(getId(), null, maxdims);
        if (err < 0) {
            throw new H5DataSpaceException("H5Sget_simple_extent_dims failed");
        }
        return reverseArray(maxdims);
    }

    private int getNDims() {
        int ndims = H5Library.INSTANCE.H5Sget_simple_extent_ndims(getId());
        if (ndims < 0) {
            throw new H5DataSpaceException("H5Sget_simple_extent_ndims failed");
        }
        return ndims;
    }

    public long getSimpleExtentNpoints() {
        return 0L;
    }

    @Override
    public String toString() {
        if (isValid()) {
            return "DataSpace[dims=" + Arrays.toString(getDims()) + ", maxdims=" + Arrays.toString(getMaxDims()) + "]";
        } else {
            return "DataSpace[invalid]";
        }
    }
}
