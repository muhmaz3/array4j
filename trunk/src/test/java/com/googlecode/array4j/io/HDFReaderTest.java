package com.googlecode.array4j.io;

import static org.junit.Assert.assertEquals;
import com.googlecode.array4j.packed.FloatPackedMatrix;
import java.util.UUID;
import net.lunglet.hdf.DataSet;
import net.lunglet.hdf.FileAccessPropList;
import net.lunglet.hdf.FileAccessPropListBuilder;
import net.lunglet.hdf.FileCreatePropList;
import net.lunglet.hdf.Group;
import net.lunglet.hdf.H5File;
import net.lunglet.hdf.PredefinedType;
import org.junit.Test;

public final class HDFReaderTest {
    @Test
    public void testReadFloatPackedMatrix() {
        FileCreatePropList fcpl = FileCreatePropList.DEFAULT;
        FileAccessPropList fapl = new FileAccessPropListBuilder().setCore(1024, false).build();
        H5File h5 = new H5File(UUID.randomUUID().toString(), fcpl, fapl);
        Group group = h5.getRootGroup().createGroup("/foo");
        group.close();
        DataSet ds = h5.getRootGroup().createDataSet("/foo/bar", PredefinedType.IEEE_F32LE, 3);
        ds.write(new float[]{1.0f, 2.0f, 3.0f});
        ds.close();
        HDFReader reader = new HDFReader(h5);
        FloatPackedMatrix x = FloatPackedMatrix.createSymmetric(2);
        reader.read(x, "/foo/bar");
        reader.close();
        assertEquals(1.0f, x.get(0, 0), 0);
        assertEquals(2.0f, x.get(0, 1), 0);
        assertEquals(2.0f, x.get(1, 0), 0);
        assertEquals(3.0f, x.get(1, 1), 0);
    }
}
