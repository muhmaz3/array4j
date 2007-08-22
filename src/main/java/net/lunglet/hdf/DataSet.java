package net.lunglet.hdf;

import java.nio.ByteBuffer;

public final class DataSet extends AbstractDs {
    private final String name;

    DataSet(final int id, final String name) {
        super(id);
        this.name = name;
    }

    public void close() {
        int err = H5Library.INSTANCE.H5Dclose(getId());
        if (err < 0) {
            throw new H5DataSetException("H5Dclose failed");
        }
        invalidate();
    }

    public String getName() {
        return name;
    }

    public DataSpace getSpace() {
        int dataspaceId = H5Library.INSTANCE.H5Dget_space(getId());
        if (dataspaceId < 0) {
            throw new H5DataSetException("H5Dget_space failed");
        }
        return new DataSpace(dataspaceId);
    }

    public long getStorageSize() {
        return H5Library.INSTANCE.H5Dget_storage_size(getId());
    }

    public void read(final byte[] buf, final DataType memType) {
        read(buf, memType, DataSpace.ALL, DataSpace.ALL, DataSetMemXferPropList.DEFAULT);
    }

    public void read(final byte[] buf, final DataType memType, final DataSpace memSpace, final DataSpace fileSpace,
            final DataSetMemXferPropList xferPlist) {
        final int memTypeId = memType.getId();
        final int memSpaceId = memSpace.getId();
        final int fileSpaceId = fileSpace.getId();
        final int xferPlistId = xferPlist.getId();
        int err = H5Library.INSTANCE.H5Dread(getId(), memTypeId, memSpaceId, fileSpaceId, xferPlistId, buf);
        if (err < 0) {
            throw new H5DataSetException("H5Dread failed");
        }
    }

    public void read(final ByteBuffer buf, final DataType memType) {
        read(buf, memType, DataSpace.ALL, DataSpace.ALL, DataSetMemXferPropList.DEFAULT);
    }

    public void read(final ByteBuffer buf, final DataType memType, final DataSpace memSpace, final DataSpace fileSpace,
            final DataSetMemXferPropList xferPlist) {
        final int memTypeId = memType.getId();
        final int memSpaceId = memSpace.getId();
        final int fileSpaceId = fileSpace.getId();
        final int xferPlistId = xferPlist.getId();
        int err = H5Library.INSTANCE.H5Dread(getId(), memTypeId, memSpaceId, fileSpaceId, xferPlistId, buf);
        if (err < 0) {
            throw new H5DataSetException("H5Dread failed");
        }
    }

    public void write(final byte[] buf, final DataType memType) {
        write(buf, memType, DataSpace.ALL, DataSpace.ALL, DataSetMemXferPropList.DEFAULT);
    }

    public void write(final byte[] buf, final DataType memType, final DataSpace memSpace, final DataSpace fileSpace,
            final DataSetMemXferPropList xferPlist) {
        final int memTypeId = memType.getId();
        final int memSpaceId = memSpace.getId();
        final int fileSpaceId = fileSpace.getId();
        final int xferPlistId = xferPlist.getId();
        int err = H5Library.INSTANCE.H5Dwrite(getId(), memTypeId, memSpaceId, fileSpaceId, xferPlistId, buf);
        if (err < 0) {
            throw new H5DataSetException("H5Dwrite failed");
        }
    }

    public void write(final ByteBuffer buf, final DataType memType) {
        write(buf, memType, DataSpace.ALL, DataSpace.ALL, DataSetMemXferPropList.DEFAULT);
    }

    public void write(final ByteBuffer buf, final DataType memType, final DataSpace memSpace,
            final DataSpace fileSpace, final DataSetMemXferPropList xferPlist) {
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
