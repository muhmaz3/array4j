package net.lunglet.hdf;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public final class DataSet extends AbstractDs {
    private static final int FLOAT_SIZE = 4;

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
        } else {
            throw new AssertionError();
        }
        checkBufferSize(size, memType, memSpace, fileSpace);
    }

    private void checkBufferSize(final int size, final DataType memType, final DataSpace memSpace,
            final DataSpace fileSpace) {
        final long minSize;
        if (DataSpace.ALL.equals(memSpace)) {
            if (DataSpace.ALL.equals(fileSpace)) {
                minSize = getStorageSize();
            } else {
                minSize = memType.getSize() * fileSpace.getSimpleExtentNpoints();
            }
        } else {
            long memSpaceSize = memSpace.getSimpleExtentNpoints();
            if (!DataSpace.ALL.equals(fileSpace)) {
                if (memSpaceSize != fileSpace.getSimpleExtentNpoints()) {
                    throw new IllegalArgumentException();
                }
            }
            minSize = memType.getSize() * memSpaceSize;
        }
        if (size < minSize) {
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
    protected void finalize() throws Throwable {
        if (isValid()) {
            close();
        }
        super.finalize();
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
        read(buf, memType, DataSpace.ALL, DataSpace.ALL, DataSetMemXferPropList.DEFAULT);
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
        read(buf, memType, DataSpace.ALL, DataSpace.ALL, DataSetMemXferPropList.DEFAULT);
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

    public void write(final Buffer buf, final DataType memType) {
        write(buf, memType, DataSpace.ALL, DataSpace.ALL, DataSetMemXferPropList.DEFAULT);
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
        write(buf, memType, DataSpace.ALL, DataSpace.ALL, DataSetMemXferPropList.DEFAULT);
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
