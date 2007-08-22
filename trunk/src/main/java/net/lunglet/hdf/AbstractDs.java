package net.lunglet.hdf;

abstract class AbstractDs extends H5Object {
    public AbstractDs(final int id) {
        super(id);
    }

    public abstract long getStorageSize();

    public abstract DataSpace getSpace();
}
