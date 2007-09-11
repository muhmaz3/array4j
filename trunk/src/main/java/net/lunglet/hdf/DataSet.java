package net.lunglet.hdf;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public final class DataSet extends AbstractDs implements Comparable<DataSet> {
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

    public Attribute createAttribute(final String name, final DataType type, final DataSpace space) {
        return Attribute.create(getId(), name, type, space);
    }

    public Attribute openAttribute(final String name) {
        return Attribute.open(getId(), name);
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
        checkBuffer(buf, memType, memSpace, fileSpace);
        final int memTypeId = memType.getId();
        final int memSpaceId = memSpace.getId();
        final int fileSpaceId = fileSpace.getId();
        final int xferPlistId = xferPlist.getId();
        int err = H5Library.INSTANCE.H5Dread(getId(), memTypeId, memSpaceId, fileSpaceId, xferPlistId, buf);
        if (err < 0) {
            throw new H5DataSetException("H5Dread failed");
        }
    }

    public void read(final byte[] arr, final DataType memType) {
        read(arr, memType, DataSpace.ALL, DataSpace.ALL);
    }

    public void read(final byte[] arr, final DataType memType, final DataSpace memSpace, final DataSpace fileSpace) {
        read(arr, memType, memSpace, fileSpace, DataSetMemXferPropList.DEFAULT);
    }

    public void read(final byte[] arr, final DataType memType, final DataSpace memSpace, final DataSpace fileSpace,
            final DataSetMemXferPropList xferPlist) {
        read(ByteBuffer.wrap(arr), memType, memSpace, fileSpace, xferPlist);
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
        checkBuffer(buf, memType, memSpace, fileSpace);
        final int memTypeId = memType.getId();
        final int memSpaceId = memSpace.getId();
        final int fileSpaceId = fileSpace.getId();
        final int xferPlistId = xferPlist.getId();
        int err = H5Library.INSTANCE.H5Dwrite(getId(), memTypeId, memSpaceId, fileSpaceId, xferPlistId, buf);
        if (err < 0) {
            throw new H5DataSetException("H5Dwrite failed");
        }
    }

    public void write(final byte[] arr, final DataType memType) {
        write(arr, memType, DataSpace.ALL, DataSpace.ALL);
    }

    public void write(final byte[] arr, final DataType memType, final DataSpace memSpace, final DataSpace fileSpace) {
        write(arr, memType, memSpace, fileSpace, DataSetMemXferPropList.DEFAULT);
    }

    public void write(final byte[] arr, final DataType memType, final DataSpace memSpace, final DataSpace fileSpace,
            final DataSetMemXferPropList xferPlist) {
        write(ByteBuffer.wrap(arr), memType, memSpace, fileSpace, xferPlist);
    }

    @Override
    public DataType getType() {
        int typeId = H5Library.INSTANCE.H5Dget_type(getId());
        if (typeId < 0) {
            throw new H5DataSetException("H5Aget_type failed");
        }
        return DataType.createTypeFromId(typeId);
    }
}
