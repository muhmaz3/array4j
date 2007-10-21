package net.lunglet.hdf;

public final class DataSetCreatePropList extends PropertyList {
    public static final DataSetCreatePropList DEFAULT = new DataSetCreatePropList(H5Library.H5P_DEFAULT);

    DataSetCreatePropList() {
        this(create(PropertyListClass.H5P_DATASET_CREATE));
    }

    private DataSetCreatePropList(final int id) {
        super(id);
    }
}
