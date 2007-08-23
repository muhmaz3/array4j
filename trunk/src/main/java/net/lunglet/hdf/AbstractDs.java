package net.lunglet.hdf;

abstract class AbstractDs extends H5Object {
    public AbstractDs(final int id) {
        super(id);
    }

    public abstract DataSpace getSpace();

    public abstract long getStorageSize();
}
