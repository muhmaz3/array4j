package net.lunglet.hdf;

import static org.junit.Assert.assertEquals;
import java.util.UUID;
import org.junit.Test;

public final class H5FileTest {
    @Test
    public void testGetFileName() {
        FileCreatePropList fcpl = FileCreatePropList.DEFAULT;
        FileAccessPropList fapl = new FileAccessPropListBuilder().setCore(1024, false).build();
        String name = UUID.randomUUID().toString();
        H5File h5 = new H5File(name, fcpl, fapl);
        fapl.close();
        assertEquals(name, h5.getFileName());
        h5.close();
    }
}
