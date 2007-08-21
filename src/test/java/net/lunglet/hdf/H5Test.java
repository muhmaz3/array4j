package net.lunglet.hdf;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.junit.Test;

import com.sun.jna.ptr.IntByReference;

public final class H5Test {
    @Test
    public void testH5get_libversion() {
        IntByReference pmajnum = new IntByReference();
        IntByReference pminnum = new IntByReference();
        IntByReference prelnum = new IntByReference();
        int err = H5Library.INSTANCE.H5get_libversion(pmajnum, pminnum, prelnum);
        assertEquals(0, err);
        assertEquals(1, pmajnum.getValue());
        assertEquals(6, pminnum.getValue());
        assertEquals(5, prelnum.getValue());
    }

    @Test
    public void test() {
        ByteBuffer buf = ByteBuffer.allocateDirect(4 * 6).order(ByteOrder.nativeOrder());
        FloatBuffer floatBuf = buf.asFloatBuffer();
        floatBuf.put(0, 1.0f);
        floatBuf.put(1, 2.0f);
        floatBuf.put(2, 3.0f);
        floatBuf.put(3, 4.0f);
        floatBuf.put(4, 5.0f);
        floatBuf.put(5, 6.0f);

        new File("test.h5").delete();
        H5File file = new H5File("test.h5");
        DataType dtype = PredefinedType.IEEE_F32LE;
        DataSet ds1 = file.createDataSet("ds1", dtype, new int[]{2, 3});
//        ds1.write(buf, dtype);
        ds1.close();
        file.close();
    }
}
