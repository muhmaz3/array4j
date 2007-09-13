package net.lunglet.hdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.UUID;
import org.junit.Test;

public final class DataSetTest {
    @Test
    public void testAttributes() {
        FileCreatePropList fcpl = FileCreatePropList.DEFAULT;
        FileAccessPropList fapl = new FileAccessPropListBuilder().setCore(1024, false).build();
        H5File h5file = new H5File(UUID.randomUUID().toString(), fcpl, fapl);
        fapl.close();
        Group root = h5file.getRootGroup();
        DataSet dataset = root.createDataSet("dataset", PredefinedType.IEEE_F64LE, 2, 2);

        DataType dtype = PredefinedType.STD_I32LE;
        DataSpace space = new DataSpace(7L);
        Attribute attr = dataset.createAttribute("attr", dtype, space);
        IntBuffer buf = IntBuffer.allocate(7);
        for (int i = 0; i < buf.capacity(); i++) {
            buf.put(i, i + 1);
        }
        attr.write(buf, dtype);
        attr.close();
        attr = dataset.openAttribute("attr");
        // TODO fix this
        assertEquals("/dataset/attr", attr.getName());
        DataType dtype2 = attr.getType();
        assertSame(dtype, dtype2);
        ByteBuffer buf2 = ByteBuffer.allocate(4 * buf.capacity()).order(ByteOrder.nativeOrder());
        attr.read(buf2, dtype);
        for (int i = 0; i < buf.capacity(); i++) {
            assertEquals(buf.get(i), buf2.asIntBuffer().get(i));
        }
        int[] buf3 = new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE, -1, 0, 1, 123, -123};
        attr.write(buf3);
        int[] buf4 = new int[buf3.length];
        attr.read(buf4);
        for (int i = 0; i < buf3.length; i++) {
            assertEquals(buf3[i], buf4[i]);
        }
        attr.close();

        dataset.close();
        root.close();
        h5file.close();
    }

    @Test
    public void testReadWrite() {
        FileCreatePropList fcpl = FileCreatePropList.DEFAULT;
        FileAccessPropList fapl = new FileAccessPropListBuilder().setCore(1024, false).build();
        H5File h5file = new H5File(UUID.randomUUID().toString(), fcpl, fapl);
        fapl.close();
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
