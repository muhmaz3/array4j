package net.lunglet.hdf;

import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;

import net.lunglet.hdf.H5Library.H5G_iterate_t;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

public final class Group extends H5Object {
    private static final WeakHashMap<Integer, Group> GROUPS = new WeakHashMap<Integer, Group>();

    private static final int H5G_DATASET = 2;

    private static final int H5G_GROUP = 1;

    private static final int H5G_LINK = 0;

    private static final int H5G_TYPE = 3;

    private final String name;

    Group(final int id, final String name) {
        super(id);
        this.name = name;
    }

    public void close() {
        // don't close root group
        if (name.equals("/")) {
            invalidate();
            return;
        }
        int err = H5Library.INSTANCE.H5Gclose(getId());
        if (err < 0) {
            throw new H5GroupException("H5Gclose failed");
        }
        invalidate();
    }

    public DataSet createDataSet(final String name, final DataType dataType, final DataSpace dataSpace) {
        return createDataSet(name, dataType, dataSpace, DataSetCreatePropList.DEFAULT);
    }

    public DataSet createDataSet(final String name, final DataType dataType, final DataSpace dataSpace,
            final DataSetCreatePropList createPlist) {
        // Obtain identifiers for C API
        int typeId = dataType.getId();
        int spaceId = dataSpace.getId();
        int createPlistId = createPlist.getId();

        // Call C routine H5Dcreate to create the named dataset
        int datasetId = H5Library.INSTANCE.H5Dcreate(getId(), name, typeId, spaceId, createPlistId);

        // If the creation of the dataset failed, throw an exception
        if (datasetId < 0) {
            throw new H5GroupException("H5Dcreate failed");
        }

        // No failure, create and return the DataSet object
        return new DataSet(datasetId, createName(name));
    }

    public DataSet createDataSet(final String name, final DataType dataType, final long... dims) {
        return createDataSet(name, dataType, new DataSpace(dims));
    }

    public Group createGroup(final String name) {
        // Call C routine H5Gcreate to create the named group, giving the
        // location id which can be a file id or a group id
        int groupId = H5Library.INSTANCE.H5Gcreate(getId(), name, 0);

        // If the creation of the group failed, throw an exception
        if (groupId < 0) {
            throw new H5GroupException("H5Gcreate failed");
        }

        // No failure, create and return the Group object
        return new Group(groupId, createName(name));
    }

    private String createName(final String name) {
        StringBuilder builder = new StringBuilder();
        builder.append(getName());
        if (!getName().equals("/")) {
            builder.append("/");
        }
        builder.append(name);
        return builder.toString();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof Group)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        return new EqualsBuilder().appendSuper(super.equals(obj)).isEquals();
    }

    @Override
    protected void finalize() throws Throwable {
        if (isValid()) {
            close();
        }
        super.finalize();
    }

    public Set<DataSet> getDataSets() {
        final IntByReference idx = new IntByReference(0);
        final HashSet<DataSet> datasets = new HashSet<DataSet>();
        final H5G_iterate_t operator = new H5G_iterate_t() {
            @Override
            public int callback(final int locId, final String datasetName, final Pointer data) {
                int type = H5Library.INSTANCE.H5Gget_objtype_by_idx(locId, idx.getValue());
                if (type == H5G_DATASET) {
                    datasets.add(openDataSet(datasetName));
                }
                return 0;
            }
        };
        H5Library.INSTANCE.H5Giterate(getId(), ".", idx, operator, null);
        return datasets;
    }

    public Set<Group> getGroups() {
        final IntByReference idx = new IntByReference(0);
        final HashSet<Group> groups = new HashSet<Group>();
        final H5G_iterate_t operator = new H5G_iterate_t() {
            @Override
            public int callback(final int locId, final String groupName, final Pointer data) {
                int type = H5Library.INSTANCE.H5Gget_objtype_by_idx(locId, idx.getValue());
                if (type == H5G_GROUP) {
                    groups.add(openGroup(groupName));
                }
                return 0;
            }
        };
        H5Library.INSTANCE.H5Giterate(getId(), ".", idx, operator, null);
        return groups;
    }

    public String getName() {
        return name;
    }

    public DataSet openDataSet(final String name) {
        // Call C function H5Dopen to open the specified dataset, giving
        // the location id and the dataset's name
        int datasetId = H5Library.INSTANCE.H5Dopen(getId(), name);

        // If the dataset's opening failed, throw an exception
        if (datasetId < 0) {
            throw new H5GroupException("H5Dopen failed");
        }

        // No failure, create and return the DataSet object
        return new DataSet(datasetId, createName(name));
    }

    public Group openGroup(final String name) {
        // Call C routine H5Gopen to open the named group, giving the
        // location id which can be a file id or a group id
        int groupId = H5Library.INSTANCE.H5Gopen(getId(), name);

        // If the opening of the group failed, throw an exception
        if (groupId < 0) {
            throw new H5GroupException("H5Gopen failed");
        }

        // No failure, create and return the Group object
        return new Group(groupId, createName(name));
    }
}
