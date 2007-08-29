package net.lunglet.hdf;

public final class FileAccessPropListBuilder {
    private final FileAccessPropList propList;

    public FileAccessPropListBuilder() {
        this.propList = new FileAccessPropList();
    }

    public FileAccessPropList build() {
        return propList;
    }

    public FileAccessPropListBuilder setCore(final long increment, final boolean backingStore) {
        int err = H5Library.INSTANCE.H5Pset_fapl_core(propList.getId(), increment, backingStore ? 1 : 0);
        if (err < 0) {
            throw new H5PropertyListException("H5Pset_fapl_core failed");
        }
        return this;
    }

    public FileAccessPropListBuilder setSec2() {
        int err = H5Library.INSTANCE.H5Pset_fapl_sec2(propList.getId());
        if (err < 0) {
            throw new H5PropertyListException("H5Pset_fapl_sec2 failed");
        }
        return this;
    }
}
