package net.lunglet.hdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;

import org.junit.Test;

public final class DataSetTest {
    @Test
    public void testReadWrite() {
        FileCreatePropList fcpl = FileCreatePropList.DEFAULT;
        FileAccessPropList fapl = new FileAccessPropListBuilder().setCore(1024, false).build();
        H5File h5file = new H5File("memory", fcpl, fapl);
        Group root = h5file.getRootGroup();
        DataSet dataset = root.createDataSet("dataset", PredefinedType.IEEE_F64LE, 2, 2);
        DataSpace space = dataset.getSpace();
        assertEquals(4, space.getSimpleExtentNpoints());
        assertEquals(4, space.getSelectNPoints());
        DoubleBuffer buf = DoubleBuffer.allocate((int) space.getSelectNPoints());
        buf.put(new double[]{1.0, 2.0, 3.0, 4.0}, 0, 4);
        dataset.write(buf, PredefinedType.IEEE_F64LE);
        FloatBuffer buf2 = FloatBuffer.allocate(buf.capacity());
        dataset.read(buf2, PredefinedType.IEEE_F32LE);
        assertTrue(Arrays.equals(buf2.array(), new float[]{1.0f, 2.0f, 3.0f, 4.0f}));
        space.close();
        dataset.close();
        root.close();
        h5file.close();
    }
}
