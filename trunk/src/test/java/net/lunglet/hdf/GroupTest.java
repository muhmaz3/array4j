package net.lunglet.hdf;

import java.util.UUID;
import org.junit.Test;

public final class GroupTest {
    @Test
    public void testRootClose() {
        FileCreatePropList fcpl = FileCreatePropList.DEFAULT;
        FileAccessPropList fapl = new FileAccessPropListBuilder().setCore(1024, false).build();
        H5File h5 = new H5File(UUID.randomUUID().toString(), fcpl, fapl);
        fapl.close();
        h5.getRootGroup().close();
        h5.close();
    }
}
