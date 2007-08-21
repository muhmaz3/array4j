package net.lunglet.hdf;

abstract class CommonFG extends IdComponent {
    public CommonFG(final int id) {
        super(id);
    }

    protected abstract int getLocId();

    public Group createGroup(final String name) {
        // Call C routine H5Gcreate to create the named group, giving the
        // location id which can be a file id or a group id
        int groupId = H5Library.INSTANCE.H5Gcreate(getLocId(), name, 0);

        // If the creation of the group failed, throw an exception
        if (groupId < 0) {
            throw new H5GroupException("H5Gcreate failed");
        }

        // No failure, create and return the Group object
        return new Group(groupId);
    }

    public Group openGroup(final String name) {
        // Call C routine H5Gopen to open the named group, giving the
        // location id which can be a file id or a group id
        int groupId = H5Library.INSTANCE.H5Gopen(getLocId(), name);

        // If the opening of the group failed, throw an exception
        if (groupId < 0) {
            throw new H5GroupException("H5Gopen failed");
        }

        // No failure, create and return the Group object
        return new Group(groupId);
    }

    public DataSet createDataSet(final String name, final DataType dataType, final int[] dims) {
        return createDataSet(name, dataType, new DataSpace(dims));
    }

    public DataSet createDataSet(final String name, final DataType dataType, final DataSpace dataSpace) {
        return createDataSet(name, dataType, dataSpace, DataSetCreatePropList.DEFAULT);
    }

    public DataSet createDataSet(final String name, final DataType dataType, final DataSpace dataSpace,
            final DataSetCreatePropList createPlist) {
        if (dataType.committed()) {
            throw new IllegalArgumentException();
        }
        // Obtain identifiers for C API
        int typeId = dataType.getId();
        int spaceId = dataSpace.getId();
        int createPlistId = createPlist.getId();

        // Call C routine H5Dcreate to create the named dataset
        int datasetId = H5Library.INSTANCE.H5Dcreate(getLocId(), name, typeId, spaceId, createPlistId);

        // If the creation of the dataset failed, throw an exception
        if (datasetId < 0) {
            throw new H5GroupException("H5Dcreate failed");
        }

        // No failure, create and return the DataSet object
        return new DataSet(datasetId);
    }
}
