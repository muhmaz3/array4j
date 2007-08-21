package net.lunglet.hdf;

import com.sun.jna.ptr.LongByReference;

public final class H5File extends CommonFG {
    /* absence of rdwr => rd-only */
    private static final int H5F_ACC_RDONLY = 0x0000;

    /* open for read and write */
    private static final int H5F_ACC_RDWR = 0x0001;

    /* overwrite existing files */
    private static final int H5F_ACC_TRUNC = 0x0002;

    /* fail if file already exists */
    private static final int H5F_ACC_EXCL = 0x0004;

    /* print debug info */
    private static final int H5F_ACC_DEBUG = 0x0008;

    /* create non-existing files */
    private static final int H5F_ACC_CREAT = 0x0010;

    public H5File(final String name) {
        // TODO make this constructor truncate the file
        this(name, H5F_ACC_RDWR | H5F_ACC_CREAT);
    }

    public H5File(final String name, final int flags) {
        this(name, flags, FileCreatePropList.DEFAULT, FileAccessPropList.DEFAULT);
    }

    public H5File(final String name, final int flags, final FileCreatePropList createPlist,
            final FileAccessPropList accessPlist) {
        super(init(name, flags, createPlist, accessPlist));
    }

    private static int init(final String name, final int flags, final FileCreatePropList createPlist,
            final FileAccessPropList accessPlist) {
        final int id;
        // These bits only set for creation, so if any of them are set,
        // create the file.
        if ((flags & (H5F_ACC_EXCL | H5F_ACC_TRUNC | H5F_ACC_DEBUG)) != 0) {
            int createPlistId = createPlist.getId();
            int accessPlistId = accessPlist.getId();
            id = H5Library.INSTANCE.H5Fcreate(name, flags, createPlistId, accessPlistId);
            if (id < 0) {
                // throw an exception when open/create fail
                throw new H5FileException("H5Fcreate failed");
            }
        } else {
            // Open the file if none of the bits above are set.
            int accessPlistId = accessPlist.getId();
            id = H5Library.INSTANCE.H5Fopen(name, flags, accessPlistId);
            if (id < 0) {
                // throw an exception when open/create fail
                throw new H5FileException("H5Fopen failed");
            }
        }
        return id;
    }

    public String getFileName() {
        return H5Util.getFileName(id);
    }

    public long getFileSize() {
        LongByReference psize = new LongByReference();
        int err = H5Library.INSTANCE.H5Fget_filesize(id, psize);
        if (err < 0) {
            throw new H5FileException("H5Fget_filesize failed");
        }
        return psize.getValue();
    }

    public void close() {
        int err = H5Library.INSTANCE.H5Fclose(id);
        if (err < 0) {
            throw new H5FileException("H5Fclose failed");
        }
        // reset the id because the file that it represents is now closed
        id = 0;
    }

    @Override
    protected int getLocId() {
        return getId();
    }
}
