package net.lunglet.io;

import java.util.ArrayList;
import java.util.Arrays;
import net.lunglet.hdf.DataSet;
import net.lunglet.hdf.Group;
import net.lunglet.hdf.H5File;

// TODO making opening in createGroup optional

public final class HDFUtils {
    public static interface DataSetFilter {
        boolean accept(DataSet dataset);
    }

    public static final DataSetFilter ALL_DATASETS_FILTER = new DataSetFilter() {
        @Override
        public boolean accept(final DataSet dataset) {
            return true;
        }
    };

    /**
     * Create a group and its parent groups.
     */
    public static Group createGroup(final Group root, final String name) {
        // TODO do more to guard against malformed names
        if (name.startsWith("/")) {
            throw new IllegalArgumentException();
        }
        String[] parts = name.split("/");
        String path = null;
        for (int i = 0; i < parts.length; i++) {
            if (path == null) {
                path = parts[i];
            } else {
                path = path + "/" + parts[i];
            }
            if (!root.existsGroup(path)) {
                root.createGroup(path);
            }
        }
        return root.openGroup(path);
    }

    public static Group createGroup(final H5File h5file, final String name) {
        if (name.startsWith("/")) {
            return createGroup(h5file.getRootGroup(), name.substring(1));
        } else {
            return createGroup(h5file.getRootGroup(), name);
        }
    }

    public static DataSet[] listDataSets(final Group group, final DataSetFilter filter) {
        ArrayList<DataSet> datasets = new ArrayList<DataSet>();
        for (DataSet dataset : group.getDataSets()) {
            if (filter.accept(dataset)) {
                datasets.add(dataset);
            }
            for (Group group2 : group.getGroups()) {
                datasets.addAll(Arrays.asList(listDataSets(group2, filter)));
                group2.close();
            }
        }
        return datasets.toArray(new DataSet[0]);
    }

    public static DataSet[] listDataSets(final H5File h5file, final DataSetFilter filter) {
        return listDataSets(h5file.getRootGroup(), filter);
    }

    private HDFUtils() {
    }
}
