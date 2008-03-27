package net.lunglet.hdf;

import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.NativeLongByReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FileAccessPropList extends PropertyList {
    public static final FileAccessPropList DEFAULT = new FileAccessPropList(H5Library.H5P_DEFAULT);

    private final Logger logger = LoggerFactory.getLogger(FileAccessPropList.class);

    FileAccessPropList() {
        this(create(PropertyListClass.H5P_FILE_ACCESS));
    }

    FileAccessPropList(final int id) {
        super(id);
        logger.debug("Created [id={}]", getId());
    }

    private void getCache(final IntByReference mdcNelmts, final IntByReference rdccNelmts,
            final NativeLongByReference rdccNbytes, final DoubleByReference rdccw) {
        int err = H5Library.INSTANCE.H5Pget_cache(getId(), mdcNelmts, rdccNelmts, rdccNbytes, rdccw);
        if (err < 0) {
            throw new H5PropertyListException("H5Pget_cache failed");
        }
    }

    public int getMetadataCacheElements() {
        IntByReference mdcNelmts = new IntByReference();
        getCache(mdcNelmts, null, null, null);
        return mdcNelmts.getValue();
    }

    public int getRawDataChunkCacheElements() {
        IntByReference rdccNelmts = new IntByReference();
        getCache(null, rdccNelmts, null, null);
        return rdccNelmts.getValue();
    }

    public long getRawDataChunkCacheSize() {
        NativeLongByReference rdccNbytes = new NativeLongByReference();
        getCache(null, null, rdccNbytes, null);
        return rdccNbytes.getValue().longValue();
    }
}
