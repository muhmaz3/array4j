package net.lunglet.hdf;

public final class DataSetCreatePropList extends PropertyList {
    public static final DataSetCreatePropList DEFAULT = new DataSetCreatePropList();

    public DataSetCreatePropList() {
        super(H5Library.H5P_DEFAULT);
    }

    public DataSetCreatePropList(final int id) {
        super(id);
    }
}
