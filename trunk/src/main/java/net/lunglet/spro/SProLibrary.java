package net.lunglet.spro;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;

public interface SProLibrary extends Library, SProConstants {
    SProLibrary INSTANCE = (SProLibrary) Native.loadLibrary("spro", SProLibrary.class);

    SProSignal sig_alloc(int n);

    void sig_free(SProSignal s);

    double sig_normalize(SProSignal s, int norm);

    SProBuffer spf_buf_alloc(short dim, NativeLong nativeLong);

    void spf_buf_free(SProBuffer buf);
}
