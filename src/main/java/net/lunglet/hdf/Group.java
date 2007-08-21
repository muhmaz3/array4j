package net.lunglet.hdf;

public final class Group extends CommonFG implements H5Object {
    private final H5Object h5obj;

    public Group(final int id) {
        super(id);
        this.h5obj = new AbstractH5Object(id) {
        };
    }

    @Override
    public int getNumAttrs() {
        return h5obj.getNumAttrs();
    }

    @Override
    public String getFileName() {
        return h5obj.getFileName();
    }

    @Override
    protected int getLocId() {
        return getId();
    }
}
