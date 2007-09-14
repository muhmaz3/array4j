package net.lunglet.hdf;

import static org.junit.Assert.assertEquals;
import java.util.UUID;
import org.junit.Test;

public final class AttributeTest {
    @Test
    public void testStringAttribute() {
        FileCreatePropList fcpl = FileCreatePropList.DEFAULT;
        FileAccessPropList fapl = new FileAccessPropListBuilder().setCore(1024, false).build();
        H5File h5file = new H5File(UUID.randomUUID().toString(), fcpl, fapl);
        fapl.close();
        String name = "attrname";
        String expectedValue = "attrval123";
        h5file.getRootGroup().createAttribute(name, expectedValue);
        String actualValue = h5file.getRootGroup().getStringAttribute(name);
        assertEquals(expectedValue, actualValue);
        h5file.close();
    }
}
