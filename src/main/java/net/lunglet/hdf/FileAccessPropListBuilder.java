package net.lunglet.hdf;

import com.sun.jna.NativeLong;

public final class FileAccessPropListBuilder {
    private FileAccessPropList propList;

    public FileAccessPropListBuilder() {
        this.propList = new FileAccessPropList();
    }

    public FileAccessPropList build() {
        return propList;
    }

    public void reset() {
        propList.close();
        propList = new FileAccessPropList();
    }

    public FileAccessPropListBuilder setCache(final int mdcNelmts, final int rdccNelmts, final long rdccNbytes,
            final double rdccw) {
        int id = propList.getId();
        synchronized (H5Library.INSTANCE) {
            int err = H5Library.INSTANCE.H5Pset_cache(id, mdcNelmts, rdccNelmts, new NativeLong(rdccNbytes), rdccw);
            if (err < 0) {
                throw new H5PropertyListException("H5Pset_cache failed");
            }
        }
        return this;
    }

    public FileAccessPropListBuilder setCore(final long increment, final boolean backingStore) {
        NativeLong incr = new NativeLong(increment);
        synchronized (H5Library.INSTANCE) {
            int err = H5Library.INSTANCE.H5Pset_fapl_core(propList.getId(), incr, backingStore ? 1 : 0);
            if (err < 0) {
                throw new H5PropertyListException("H5Pset_fapl_core failed");
            }
        }
        return this;
    }

    public void setMetaBlockSize(final long size) {
        synchronized (H5Library.INSTANCE) {
            int err = H5Library.INSTANCE.H5Pset_meta_block_size(propList.getId(), size);
            if (err < 0) {
                throw new H5PropertyListException("H5Pset_meta_block_size failed");
            }
        }
    }

    public FileAccessPropListBuilder setSec2() {
        synchronized (H5Library.INSTANCE) {
            int err = H5Library.INSTANCE.H5Pset_fapl_sec2(propList.getId());
            if (err < 0) {
                throw new H5PropertyListException("H5Pset_fapl_sec2 failed");
            }
        }
        return this;
    }

    public FileAccessPropListBuilder setSieveBufSize(final long size) {
        synchronized (H5Library.INSTANCE) {
            int err = H5Library.INSTANCE.H5Pset_sieve_buf_size(propList.getId(), size);
            if (err < 0) {
                throw new H5PropertyListException("H5Pset_sieve_buf_size failed");
            }
        }
        return this;
    }
}
