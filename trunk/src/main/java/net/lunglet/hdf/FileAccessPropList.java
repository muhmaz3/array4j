package net.lunglet.hdf;

public final class FileAccessPropList extends PropertyList {
    public static final FileAccessPropList DEFAULT = new FileAccessPropList(H5Library.H5P_DEFAULT);

    FileAccessPropList() {
        this(create(PropertyListClass.H5P_FILE_ACCESS));
    }

    private FileAccessPropList(final int id) {
        super(id);
    }
}
