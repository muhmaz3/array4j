package net.lunglet.hdf;

abstract class H5Object extends IdComponent {
    public H5Object(final int id) {
        super(id);
    }

    public String getFileName() {
        // Preliminary call to H5Fget_name to get the length of the file name
        int nameSize = H5Library.INSTANCE.H5Fget_name(getId(), null, 0);

        // If H5Aget_name returns a negative value, raise an exception,
        if (nameSize < 0) {
            throw new H5IdComponentException("H5Fget_name failed");
        }

        // TODO implement the rest of this function
        throw new UnsupportedOperationException();
    }

    public final int getNumAttrs() {
        return 0;
    }
}
