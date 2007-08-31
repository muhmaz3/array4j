package net.lunglet.hdf;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

public final class DataSet extends AbstractDs implements Comparable<DataSet> {
    private static final int FLOAT_SIZE = Float.SIZE >>> 3;

    private static final int DOUBLE_SIZE = Double.SIZE >>> 3;

    private final String name;

    DataSet(final int id, final String name) {
        super(id);
        this.name = name;
    }

    private void checkBufferSize(final Buffer buf, final DataType memType, final DataSpace memSpace,
            final DataSpace fileSpace) {
        final int size;
        if (buf instanceof ByteBuffer) {
            size = buf.capacity();
        } else if (buf instanceof FloatBuffer) {
            size = FLOAT_SIZE * buf.capacity();
        } else if (buf instanceof DoubleBuffer) {
            size = DOUBLE_SIZE * buf.capacity();
        } else {
            // TODO add other buffers above
            throw new AssertionError();
        }
        checkBufferSize(size, memType, memSpace, fileSpace);
    }

    private void checkBufferSize(final int size, final DataType memType, final DataSpace memSpace,
            final DataSpace fileSpace) {
        final DataSpace space;
        if (memSpace.equals(DataSpace.ALL)) {
            if (fileSpace.equals(DataSpace.ALL)) {
                space = getSpace();
            } else {
                space = fileSpace;
            }
        } else {
            space = memSpace;
        }
        // TODO need a better check here that takes offset and bounding box into
        // account, because this check won't catch all buffers that are too
        // small
        long requiredSize = memType.getSize() * space.getSelectNPoints();
        if (size < requiredSize) {
            throw new IllegalArgumentException();
        }
    }

    public void close() {
        int err = H5Library.INSTANCE.H5Dclose(getId());
        if (err < 0) {
            throw new H5DataSetException("H5Dclose failed");
        }
        invalidate();
    }

    @Override
    public int compareTo(final DataSet o) {
        return getName().compareTo(o.getName());
    }

    public String getName() {
        return name;
    }

    @Override
    public DataSpace getSpace() {
        int dataspaceId = H5Library.INSTANCE.H5Dget_space(getId());
        if (dataspaceId < 0) {
            throw new H5DataSetException("H5Dget_space failed");
        }
        return new DataSpace(dataspaceId);
    }

    @Override
    public long getStorageSize() {
        return H5Library.INSTANCE.H5Dget_storage_size(getId());
    }

    public void read(final Buffer buf, final DataType memType) {
        read(buf, memType, DataSpace.ALL, DataSpace.ALL);
    }

    public void read(final Buffer buf, final DataType memType, final DataSpace memSpace, final DataSpace fileSpace) {
        read(buf, memType, memSpace, fileSpace, DataSetMemXferPropList.DEFAULT);
    }

    public void read(final Buffer buf, final DataType memType, final DataSpace memSpace, final DataSpace fileSpace,
            final DataSetMemXferPropList xferPlist) {
        checkBufferSize(buf, memType, memSpace, fileSpace);
        final int memTypeId = memType.getId();
        final int memSpaceId = memSpace.getId();
        final int fileSpaceId = fileSpace.getId();
        final int xferPlistId = xferPlist.getId();
        int err = H5Library.INSTANCE.H5Dread(getId(), memTypeId, memSpaceId, fileSpaceId, xferPlistId, buf);
        if (err < 0) {
            throw new H5DataSetException("H5Dread failed");
        }
    }

    public void read(final byte[] buf, final DataType memType) {
        read(buf, memType, DataSpace.ALL, DataSpace.ALL);
    }

    public void read(final byte[] buf, final DataType memType, final DataSpace memSpace, final DataSpace fileSpace) {
        read(buf, memType, memSpace, fileSpace, DataSetMemXferPropList.DEFAULT);
    }

    public void read(final byte[] buf, final DataType memType, final DataSpace memSpace, final DataSpace fileSpace,
            final DataSetMemXferPropList xferPlist) {
        checkBufferSize(buf.length, memType, memSpace, fileSpace);
        final int memTypeId = memType.getId();
        final int memSpaceId = memSpace.getId();
        final int fileSpaceId = fileSpace.getId();
        final int xferPlistId = xferPlist.getId();
        int err = H5Library.INSTANCE.H5Dread(getId(), memTypeId, memSpaceId, fileSpaceId, xferPlistId, buf);
        if (err < 0) {
            throw new H5DataSetException("H5Dread failed");
        }
    }

    @Override
    public String toString() {
        if (isValid()) {
            return "DataSet[name=" + getName() + "]";
        } else {
            return "DataSet[invalid]";
        }
    }

    public void write(final Buffer buf, final DataType memType) {
        write(buf, memType, DataSpace.ALL, DataSpace.ALL);
    }

    public void write(final Buffer buf, final DataType memType, final DataSpace memSpace, final DataSpace fileSpace) {
        write(buf, memType, memSpace, fileSpace, DataSetMemXferPropList.DEFAULT);
    }

    public void write(final Buffer buf, final DataType memType, final DataSpace memSpace,
            final DataSpace fileSpace, final DataSetMemXferPropList xferPlist) {
        checkBufferSize(buf, memType, memSpace, fileSpace);
        final int memTypeId = memType.getId();
        final int memSpaceId = memSpace.getId();
        final int fileSpaceId = fileSpace.getId();
        final int xferPlistId = xferPlist.getId();
        int err = H5Library.INSTANCE.H5Dwrite(getId(), memTypeId, memSpaceId, fileSpaceId, xferPlistId, buf);
        if (err < 0) {
            throw new H5DataSetException("H5Dwrite failed");
        }
    }

    public void write(final byte[] buf, final DataType memType) {
        write(buf, memType, DataSpace.ALL, DataSpace.ALL);
    }

    public void write(final byte[] buf, final DataType memType, final DataSpace memSpace, final DataSpace fileSpace) {
        write(buf, memType, memSpace, fileSpace, DataSetMemXferPropList.DEFAULT);
    }

    public void write(final byte[] buf, final DataType memType, final DataSpace memSpace, final DataSpace fileSpace,
            final DataSetMemXferPropList xferPlist) {
        checkBufferSize(buf.length, memType, memSpace, fileSpace);
        final int memTypeId = memType.getId();
        final int memSpaceId = memSpace.getId();
        final int fileSpaceId = fileSpace.getId();
        final int xferPlistId = xferPlist.getId();
        int err = H5Library.INSTANCE.H5Dwrite(getId(), memTypeId, memSpaceId, fileSpaceId, xferPlistId, buf);
        if (err < 0) {
            throw new H5DataSetException("H5Dwrite failed");
        }
    }
}
