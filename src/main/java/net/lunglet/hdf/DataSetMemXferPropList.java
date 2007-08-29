package net.lunglet.hdf;

public final class DataSetMemXferPropList extends PropertyList {
    public static final DataSetMemXferPropList DEFAULT = new DataSetMemXferPropList(H5Library.H5P_DEFAULT);

    DataSetMemXferPropList() {
        this(create(PropertyListClass.H5P_DATASET_XFER));
    }

    private DataSetMemXferPropList(final int id) {
        super(id);
    }
}
