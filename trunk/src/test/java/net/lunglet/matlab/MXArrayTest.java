package net.lunglet.matlab;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import com.sun.jna.NativeLong;
import org.junit.Test;

public final class MXArrayTest {
    @Test
    public void testBasics() {
        MXArray mat = MXLibrary.INSTANCE.mxCreateDoubleMatrix(new NativeLong(3), new NativeLong(2), 0);
        assertEquals(3, mat.getM().intValue());
        assertEquals(2, mat.getN().intValue());
        assertEquals("double", mat.getClassName());
        assertEquals(MXLibrary.DOUBLE_CLASS, mat.getClassID());
        assertTrue(mat.isDouble());
        mat.destroyArray();
        MXArray arr = MXLibrary.INSTANCE.mxCreateNumericArray(new NativeLong(2), new NativeLong[]{new NativeLong(3),
                new NativeLong(2)}, MXLibrary.INT32_CLASS, 0);
        arr.destroyArray();
    }

    @Test
    public void testConversion() {
        MXArray mat = MXArray.createDoubleMatrix(2, 3);
        assertTrue(mat.isDouble());
        double[] values = mat.getPr();
    }
}
