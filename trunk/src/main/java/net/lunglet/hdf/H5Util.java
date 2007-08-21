package net.lunglet.hdf;

final class H5Util {
    private H5Util() {
    }

    static String getFileName(final int id) {
        // Preliminary call to H5Fget_name to get the length of the file name
        int nameSize = H5Library.INSTANCE.H5Fget_name(id, null, 0);

        // If H5Aget_name returns a negative value, raise an exception,
        if (nameSize < 0) {
            throw new H5IdComponentException("H5Fget_name failed");
        }

        // TODO implement the rest of this function
        return null;
    }
}
