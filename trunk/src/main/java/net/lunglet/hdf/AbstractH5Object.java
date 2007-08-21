package net.lunglet.hdf;

abstract class AbstractH5Object extends IdComponent implements H5Object {
    public AbstractH5Object(final int id) {
        super(id);
    }

    public final int getNumAttrs() {
        return 0;
    }

    public String getFileName() {
        return H5Util.getFileName(id);
    }
}
