package net.lunglet.hdf;

import java.util.UUID;
import org.junit.Test;

public final class AttributeTest {
    @Test
    public void testStringAttribute() {
        FileCreatePropList fcpl = FileCreatePropList.DEFAULT;
        FileAccessPropList fapl = new FileAccessPropListBuilder().setCore(1024, false).build();
        H5File h5file = new H5File(UUID.randomUUID().toString(), fcpl, fapl);
        fapl.close();
        Group root = h5file.getRootGroup();
        Group foo = root.createGroup("/foo");
        foo.close();
        h5file.close();
    }
}
