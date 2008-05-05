package net.lunglet.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.UUID;
import net.lunglet.hdf.FileAccessPropList;
import net.lunglet.hdf.FileAccessPropListBuilder;
import net.lunglet.hdf.FileCreatePropList;
import net.lunglet.hdf.Group;
import net.lunglet.hdf.H5File;
import org.junit.Test;

// TODO test that correct group is returned by createGroup

public final class HDFUtilsTest {
    @Test
    public void testCreateGroup() {
        FileCreatePropList fcpl = FileCreatePropList.DEFAULT;
        FileAccessPropList fapl = new FileAccessPropListBuilder().setCore(1024, false).build();
        String name = UUID.randomUUID().toString();
        H5File h5file = new H5File(name, fcpl, fapl);
        fapl.close();

        HDFUtils.createGroup(h5file, "group1");
        Group group12 = HDFUtils.createGroup(h5file, "/group12");
        assertEquals("/group12", group12.getName());
        HDFUtils.createGroup(h5file.getRootGroup(), "group1");
        HDFUtils.createGroup(h5file.getRootGroup(), "group2");
        HDFUtils.createGroup(h5file, "/group2/group3");
        HDFUtils.createGroup(h5file, "/group2/group3/group4");
        HDFUtils.createGroup(h5file, "/group5/group6/group7");
        HDFUtils.createGroup(h5file, "/group5/group6");

        Group group3 = h5file.getRootGroup().openGroup("/group2/group3");
        HDFUtils.createGroup(group3, "group8");
        HDFUtils.createGroup(group3, "group9");
        Group group11 = HDFUtils.createGroup(group3, "group10/group11");
        assertEquals("/group2/group3/group10/group11", group11.getName());
        group3.close();

        Group root = h5file.getRootGroup();
        assertTrue(root.existsGroup("/group1"));
        assertTrue(root.existsGroup("/group2"));
        assertTrue(root.existsGroup("/group2/group3"));
        assertTrue(root.existsGroup("/group2/group3/group4"));
        assertTrue(root.existsGroup("/group5"));
        assertTrue(root.existsGroup("/group5/group6"));
        assertTrue(root.existsGroup("/group5/group6/group7"));
        assertTrue(root.existsGroup("/group2/group3/group8"));
        assertTrue(root.existsGroup("/group2/group3/group9"));
        assertTrue(root.existsGroup("/group2/group3/group10/group11"));
        assertTrue(root.existsGroup("/group12"));
        root.close();

        h5file.close();
    }
}
