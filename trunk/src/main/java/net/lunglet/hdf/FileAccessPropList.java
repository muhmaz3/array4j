package net.lunglet.hdf;

public final class FileAccessPropList extends PropertyList {
    public static final FileAccessPropList DEFAULT = new FileAccessPropList();

    public FileAccessPropList() {
        super(H5Library.H5P_DEFAULT);
    }

    public FileAccessPropList(final int id) {
        super(id);
    }
}
