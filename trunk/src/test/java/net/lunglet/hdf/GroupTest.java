package net.lunglet.hdf;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.UUID;
import org.junit.Test;

public final class GroupTest {
    private static H5File createH5File() {
        FileCreatePropList fcpl = FileCreatePropList.DEFAULT;
        FileAccessPropList fapl = new FileAccessPropListBuilder().setCore(1024, false).build();
        H5File h5file = new H5File(UUID.randomUUID().toString(), fcpl, fapl);
        fapl.close();
        return h5file;
    }

    @Test
    public void testDoubleClose() {
        H5File h5file = createH5File();
        Group group1 = h5file.getRootGroup().createGroup("group1");
        assertTrue(group1.isOpen());
        group1.close();
        assertFalse(group1.isOpen());
        group1.close();
        h5file.close();
    }

    @Test
    public void testRootClose() {
        H5File h5file = createH5File();
        h5file.getRootGroup().close();
        h5file.close();
    }
}
