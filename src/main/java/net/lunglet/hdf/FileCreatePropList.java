package net.lunglet.hdf;

public final class FileCreatePropList extends PropertyList {
    public static final FileCreatePropList DEFAULT = new FileCreatePropList();

    public FileCreatePropList() {
        super(H5Library.H5P_DEFAULT);
    }

    public FileCreatePropList(final int id) {
        super(id);
    }
}
