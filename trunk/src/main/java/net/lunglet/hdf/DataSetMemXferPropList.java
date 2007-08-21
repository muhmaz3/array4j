package net.lunglet.hdf;

public final class DataSetMemXferPropList extends PropertyList {
    public static final DataSetMemXferPropList DEFAULT = new DataSetMemXferPropList();

    public DataSetMemXferPropList() {
        super(H5Library.H5P_DEFAULT);
    }

    public DataSetMemXferPropList(final int id) {
        super(id);
    }
}
