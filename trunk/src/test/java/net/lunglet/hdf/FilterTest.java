package net.lunglet.hdf;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public final class FilterTest {
    @Test
    public void testAvailable() {
        assertTrue(H5Library.INSTANCE.H5Zfilter_avail(Filter.DEFLATE.intValue()) >= 0);
        assertTrue(H5Library.INSTANCE.H5Zfilter_avail(Filter.FLETCHER32.intValue()) >= 0);
        assertTrue(H5Library.INSTANCE.H5Zfilter_avail(Filter.SHUFFLE.intValue()) >= 0);
        assertTrue(H5Library.INSTANCE.H5Zfilter_avail(Filter.SZIP.intValue()) >= 0);
    }
}
