package net.lunglet.hdf;

import com.sun.jna.NativeLong;
import com.sun.jna.ptr.LongByReference;
import java.io.File;
import java.nio.charset.Charset;
import java.util.Arrays;
import net.jcip.annotations.NotThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO include human readable flags in exceptions when opening
// fails, to make it easy to diagnose e.g. 'n RDWR open of a
// non-existant file

@NotThreadSafe
public final class H5File extends IdComponent {
    private static final CloseAction CLOSE_ACTION = new CloseAction() {
        @Override
        public void close(final int id) {
            int err = H5Library.INSTANCE.H5Fclose(id);
            if (err < 0) {
                throw new H5FileException("H5Fclose failed");
            }
        }
    };

    /* create non-existing files */
    private static final int H5F_ACC_CREAT = 0x0010;

    /* print debug info */
    private static final int H5F_ACC_DEBUG = 0x0008;

    /* fail if file already exists */
    private static final int H5F_ACC_EXCL = 0x0004;

    /* absence of rdwr => rd-only */
    public static final int H5F_ACC_RDONLY = 0x0000;

    /* open for read and write */
    public static final int H5F_ACC_RDWR = 0x0001;

    /* overwrite existing files */
    public static final int H5F_ACC_TRUNC = 0x0002;

    private static int init(final String name, final int flags, final FileCreatePropList createPlist,
            final FileAccessPropList accessPlist) {
        final int id;
        // Create file if any creation bits are set
        final int creationBits = H5F_ACC_EXCL | H5F_ACC_TRUNC | H5F_ACC_DEBUG;
        synchronized (H5Library.INSTANCE) {
            if ((flags & creationBits) != 0) {
                id = H5Library.INSTANCE.H5Fcreate(name, flags, createPlist.getId(), accessPlist.getId());
                if (id < 0) {
                    // throw an exception when open/create fail
                    throw new H5FileException("H5Fcreate failed for " + name, true);
                }
            } else {
                // Open the file if none of the bits above are set.
                id = H5Library.INSTANCE.H5Fopen(name, flags, accessPlist.getId());
                if (id < 0) {
                    // throw an exception when open/create fail
                    throw new H5FileException("H5Fopen failed for " + name, true);
                }
            }
        }
        return id;
    }

    private final Logger logger = LoggerFactory.getLogger(H5File.class);

    private final Group rootGroup;

    public H5File(final File file) {
        this(file, H5F_ACC_RDONLY);
    }

    public H5File(final File file, final int flags) {
        this(file, flags, FileCreatePropList.DEFAULT, FileAccessPropList.DEFAULT);
    }

    public H5File(final File file, final int flags, final FileCreatePropList fcpl, final FileAccessPropList fapl) {
        super(init(file.getPath(), flags, fcpl, fapl), CLOSE_ACTION);
        this.rootGroup = new Group(getId(), true);
        logger.debug("Created [id={}]", getId());
    }

    public H5File(final String name) {
        this(new File(name));
    }

    public H5File(final String name, final FileCreatePropList fcpl, final FileAccessPropList fapl) {
        this(name, H5F_ACC_TRUNC, fcpl, fapl);
    }

    public H5File(final String name, final int flags) {
        this(new File(name), flags);
    }

    public H5File(final String name, final int flags, final FileCreatePropList fcpl, final FileAccessPropList fapl) {
        this(new File(name), flags, fcpl, fapl);
    }

    public FileAccessPropList getAccessPropertyList() {
        final int id;
        synchronized (H5Library.INSTANCE) {
            id = H5Library.INSTANCE.H5Fget_access_plist(getId());
            if (id < 0) {
                throw new H5FileException("H5Fget_access_plist failed");
            }
        }
        return new FileAccessPropList(id);
    }

    public FileCreatePropList getCreatePropertyList() {
        final int id;
        synchronized (H5Library.INSTANCE) {
            id = H5Library.INSTANCE.H5Fget_create_plist(getId());
            if (id < 0) {
                throw new H5FileException("H5Fget_create_plist failed");
            }
        }
        return new FileCreatePropList(id);
    }

    public String getFileName() {
        final NativeLong size;
        synchronized (H5Library.INSTANCE) {
            size = H5Library.INSTANCE.H5Fget_name(getId(), null, new NativeLong(0));
            if (size.longValue() < 0) {
                throw new H5FileException("H5Fget_name failed");
            }
        }
        byte[] buf = new byte[size.intValue() + 1];
        synchronized (H5Library.INSTANCE) {
            NativeLong err = H5Library.INSTANCE.H5Fget_name(getId(), buf, new NativeLong(buf.length));
            if (err.longValue() < 0) {
                throw new H5FileException("H5Fget_name failed");
            }
        }
        return new String(Arrays.copyOf(buf, buf.length - 1), Charset.forName("US-ASCII"));
    }

    public long getFileSize() {
        LongByReference psize = new LongByReference();
        synchronized (H5Library.INSTANCE) {
            int err = H5Library.INSTANCE.H5Fget_filesize(getId(), psize);
            if (err < 0) {
                throw new H5FileException("H5Fget_filesize failed");
            }
        }
        return psize.getValue();
    }

    public long getFreeSpace() {
        synchronized (H5Library.INSTANCE) {
            long size = H5Library.INSTANCE.H5Fget_freespace(getId());
            if (size < 0) {
                throw new H5FileException("H5Fget_freespace failed");
            }
            return size;
        }
    }

    public Group getRootGroup() {
        return rootGroup;
    }
}
