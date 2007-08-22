package net.lunglet.hdf;

public final class Attribute extends AbstractDs {
    public Attribute(final int id) {
        super(id);
    }

    @Override
    public DataSpace getSpace() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getStorageSize() {
        throw new UnsupportedOperationException();
    }
}
