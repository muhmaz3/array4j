package net.lunglet.hdf;

import java.nio.ByteBuffer;

public final class DataSet extends AbstractDs {
    public DataSet(final int id) {
        super(id);
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
        int err = H5Library.INSTANCE.H5Dread(id, memTypeId, memSpaceId, fileSpaceId, xferPlistId, buf);
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
        int err = H5Library.INSTANCE.H5Dwrite(id, memTypeId, memSpaceId, fileSpaceId, xferPlistId, buf);
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
        int err = H5Library.INSTANCE.H5Dwrite(id, memTypeId, memSpaceId, fileSpaceId, xferPlistId, buf);
        if (err < 0) {
            throw new H5DataSetException("H5Dwrite failed");
        }
    }

    public void close() {
        int err = H5Library.INSTANCE.H5Dclose(id);
        if (err < 0) {
            throw new H5DataSetException("H5Dclose failed");
        }
        // reset the id because the group that it represents is now closed
        id = 0;
    }
}
