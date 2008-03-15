package net.lunglet.spro;

import com.sun.jna.PointerType;

public final class SProSignal extends PointerType {
    public static SProSignal createInstance(final int n) {
        SProSignal sig = SProLibrary.INSTANCE.sig_alloc(n);
        if (sig == null) {
            throw new RuntimeException("sig_alloc failed");
        }
        return sig;
    }

    public void free() {
        SProLibrary.INSTANCE.sig_free(this);
    }

    public double normalize(final boolean norm) {
        return SProLibrary.INSTANCE.sig_normalize(this, norm ? 1 : 0);
    }
}
