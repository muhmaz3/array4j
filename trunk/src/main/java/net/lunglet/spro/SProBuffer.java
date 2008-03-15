package net.lunglet.spro;

import com.sun.jna.NativeLong;
import com.sun.jna.PointerType;

public final class SProBuffer extends PointerType {
    public static SProBuffer createInstance(final int dim, final int size) {
        if (dim < 0 || dim > Short.MAX_VALUE) {
            throw new IllegalArgumentException();
        }
        if (size < 0) {
            throw new IllegalArgumentException();
        }
        short shortDim = (short) dim;
        SProBuffer buf = SProLibrary.INSTANCE.spf_buf_alloc(shortDim, new NativeLong(size));
        if (buf == null) {
            throw new RuntimeException("spf_buf_alloc failed");
        }
        return buf;
    }

    public void free() {
        SProLibrary.INSTANCE.spf_buf_free(this);
    }
}
