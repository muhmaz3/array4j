package net.lunglet.hdf;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sun.jna.ptr.IntByReference;

public final class H5Test {
    @Test
    public void testH5get_libversion() {
        IntByReference pmajnum = new IntByReference();
        IntByReference pminnum = new IntByReference();
        IntByReference prelnum = new IntByReference();
        int err = H5Library.INSTANCE.H5get_libversion(pmajnum, pminnum, prelnum);
        assertEquals(0, err);
        assertEquals(1, pmajnum.getValue());
        assertEquals(6, pminnum.getValue());
        assertEquals(6, prelnum.getValue());
    }
}
