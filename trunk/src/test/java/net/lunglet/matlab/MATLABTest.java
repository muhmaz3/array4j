package net.lunglet.matlab;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Ignore;
import org.junit.Test;

public final class MATLABTest {
    private static MXArray createDoubleMatrix(final int m, final int n) {
        return MXLibrary.INSTANCE.mxCreateDoubleMatrix(new NativeLong(m), new NativeLong(n), MXLibrary.REAL);
    }

    private static void destroy(final MXArray ap) {
        MXLibrary.INSTANCE.mxDestroyArray(ap);
    }

    @Test
    public void testArrayToString() {
        String expectedValue = "hello1234";
        MXArray strArray = MXLibrary.INSTANCE.mxCreateString(expectedValue);
        assertTrue(MXLibrary.INSTANCE.mxIsChar(strArray));
        Pointer strPointer = MXLibrary.INSTANCE.mxArrayToString(strArray);
        String actualValue = strPointer.getString(0);
        MXLibrary.INSTANCE.mxFree(strPointer);
        destroy(strArray);
        assertEquals(actualValue, expectedValue);
    }

    @Test
    public void testDuplicateArray() {
        MXArray a = createDoubleMatrix(0, 0);
        destroy(a);
        MXArray b = MXLibrary.INSTANCE.mxDuplicateArray(a);
        destroy(b);
    }

    @Ignore
    public void testEngineOpenClose() {
        Engine engine = new Engine(false);
        assertFalse(engine.isVisible());
        engine.close();
        engine = new Engine(true);
        assertTrue(engine.isVisible());
        engine.setVisible(false);
        assertFalse(engine.isVisible());
        engine.close();
    }

    @Test
    public void testEpsInfNaN() {
        assertTrue(MXLibrary.INSTANCE.mxGetEps() < 1e-9);
        assertTrue(Double.isInfinite(MXLibrary.INSTANCE.mxGetInf()));
        assertTrue(Double.isNaN(MXLibrary.INSTANCE.mxGetNaN()));
        assertTrue(MXLibrary.INSTANCE.mxIsFinite(Double.MIN_VALUE));
        assertTrue(MXLibrary.INSTANCE.mxIsFinite(Double.MAX_VALUE));
        assertFalse(MXLibrary.INSTANCE.mxIsFinite(Double.NaN));
        assertFalse(MXLibrary.INSTANCE.mxIsFinite(Double.POSITIVE_INFINITY));
        assertFalse(MXLibrary.INSTANCE.mxIsFinite(Double.NEGATIVE_INFINITY));
    }

    @Test
    public void testFunctionHandle() {
        NativeLong dim = new NativeLong(1);
        MXArray handle = MXLibrary.INSTANCE.mxCreateNumericMatrix(dim, dim, MXLibrary.FUNCTION_CLASS, 0);
        assertTrue(MXLibrary.INSTANCE.mxIsFunctionHandle(handle));
        destroy(handle);
    }

    @Test
    public void testGetIsClass() {
        MXArray a = createDoubleMatrix(0, 0);
        assertEquals(MXLibrary.INSTANCE.mxGetClassName(a), "double");
        assertEquals(MXLibrary.DOUBLE_CLASS, MXLibrary.INSTANCE.mxGetClassID(a));
        assertTrue(MXLibrary.INSTANCE.mxIsClass(a, "double"));
        assertTrue(MXLibrary.INSTANCE.mxIsDouble(a));
        assertFalse(MXLibrary.INSTANCE.mxIsComplex(a));
        assertFalse(MXLibrary.INSTANCE.mxIsUint16(a));
        assertFalse(MXLibrary.INSTANCE.mxIsUint32(a));
        assertFalse(MXLibrary.INSTANCE.mxIsUint64(a));
        assertFalse(MXLibrary.INSTANCE.mxIsUint8(a));
        assertFalse(MXLibrary.INSTANCE.mxIsInt16(a));
        assertFalse(MXLibrary.INSTANCE.mxIsInt32(a));
        assertFalse(MXLibrary.INSTANCE.mxIsInt64(a));
        assertFalse(MXLibrary.INSTANCE.mxIsInt8(a));
        assertFalse(MXLibrary.INSTANCE.mxIsLogical(a));
        assertFalse(MXLibrary.INSTANCE.mxIsChar(a));
        assertFalse(MXLibrary.INSTANCE.mxIsFunctionHandle(a));
        destroy(a);
    }

    @Test
    public void testIs() {
        MXArray a = createDoubleMatrix(0, 0);
        assertTrue(MXLibrary.INSTANCE.mxIsEmpty(a));
        assertTrue(MXLibrary.INSTANCE.mxIsNumeric(a));
        assertFalse(MXLibrary.INSTANCE.mxIsObject(a));
        assertFalse(MXLibrary.INSTANCE.mxIsOpaque(a));
        assertFalse(MXLibrary.INSTANCE.mxIsCell(a));
        assertFalse(MXLibrary.INSTANCE.mxIsSparse(a));
        assertFalse(MXLibrary.INSTANCE.mxIsStruct(a));
        destroy(a);
    }

    @Test
    public void testSizes() {
        MXArray a = createDoubleMatrix(123, 456);
        assertEquals(8L, MXLibrary.INSTANCE.mxGetElementSize(a).longValue());
        assertEquals(123L, MXLibrary.INSTANCE.mxGetM(a).longValue());
        assertEquals(456L, MXLibrary.INSTANCE.mxGetN(a).longValue());
        assertEquals(2L, MXLibrary.INSTANCE.mxGetNumberOfDimensions(a).longValue());
        assertEquals(123L * 456L, MXLibrary.INSTANCE.mxGetNumberOfElements(a).longValue());
        Pointer dims = MXLibrary.INSTANCE.mxGetDimensions(a);
        assertEquals(123L, dims.getNativeLong(0).longValue());
        assertEquals(456L, dims.getNativeLong(NativeLong.SIZE).longValue());
        destroy(a);
    }

    @Test
    public void testUserBits() {
        MXArray a = createDoubleMatrix(0, 0);
        int bits = 0x1f;
        MXLibrary.INSTANCE.mxSetUserBits(a, bits);
        assertEquals(MXLibrary.INSTANCE.mxGetUserBits(a), bits);
        destroy(a);
    }

    @Ignore
    public void testVersion() throws IOException {
        Engine engine = new Engine(false);
        InputStream stream = getClass().getResourceAsStream("version.m");
        assertNotNull(stream);
        engine.eval(stream);
        engine.close();
    }
}
