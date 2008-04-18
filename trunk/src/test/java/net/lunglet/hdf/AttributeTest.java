package net.lunglet.hdf;

import static org.junit.Assert.assertEquals;
import java.util.UUID;
import org.junit.Ignore;
import org.junit.Test;

public final class AttributeTest {
    @Ignore
    @Test
    public void testSize() {
        FileCreatePropList fcpl = FileCreatePropList.DEFAULT;
        FileAccessPropList fapl = new FileAccessPropListBuilder().setCore(1024, false).build();
        H5File h5file = new H5File(UUID.randomUUID().toString(), fcpl, fapl);
        fapl.close();
        // XXX this size is too large for an attribute?
        DataSpace space = new DataSpace(16372);
        Attribute attr = h5file.getRootGroup().createAttribute("attr", IntType.STD_I32LE, space);
        attr.close();
        space.close();
        h5file.close();
    }

    @Test
    public void testStringAttribute() {
        FileCreatePropList fcpl = FileCreatePropList.DEFAULT;
        FileAccessPropList fapl = new FileAccessPropListBuilder().setCore(1024, false).build();
        H5File h5file = new H5File(UUID.randomUUID().toString(), fcpl, fapl);
        fapl.close();
        String name = "attrname";
        String expectedValue = "attrval123";
        Group group = h5file.getRootGroup().createGroup("group");
        group.createAttribute(name, expectedValue);
        String actualValue = group.getStringAttribute(name);
        assertEquals(expectedValue, actualValue);
        // XXX this doesn't work on 64-bit Linux with HDF5 1.6.5
        if (false) {
            h5file.getRootGroup().createAttribute(name, expectedValue);
            String actualValue2 = h5file.getRootGroup().getStringAttribute(name);
        }
        h5file.close();
    }
}
