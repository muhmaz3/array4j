package net.lunglet.hdf;

public final class FileCreatePropList extends PropertyList {
    public static final FileCreatePropList DEFAULT = new FileCreatePropList(H5Library.H5P_DEFAULT);

    FileCreatePropList() {
        this(create(PropertyListClass.H5P_FILE_CREATE));
    }

    private FileCreatePropList(final int id) {
        super(id);
    }
}
