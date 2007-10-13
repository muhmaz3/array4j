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

    @Ignore
    public void testVersion() throws IOException {
        Engine engine = new Engine(false);
        InputStream stream = getClass().getResourceAsStream("version.m");
        assertNotNull(stream);
        engine.eval(stream);
        engine.close();
    }

    private static MXArray createDoubleMatrix(final int m, final int n) {
        return MXLibrary.INSTANCE.mxCreateDoubleMatrix(new NativeLong(m), new NativeLong(n), MXLibrary.REAL);
    }

    private static void destroy(final MXArray ap) {
        MXLibrary.INSTANCE.mxDestroyArray(ap);
    }

    @Test
    public void testSizes() {
        MXArray a = createDoubleMatrix(123, 456);
        assertEquals(8L, MXLibrary.INSTANCE.mxGetElementSize(a).longValue());
        destroy(a);
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
    public void testArrayToString() {
        String expectedValue = "hello1234";
        MXArray strArray = MXLibrary.INSTANCE.mxCreateString(expectedValue);
        Pointer strPointer = MXLibrary.INSTANCE.mxArrayToString(strArray);
        String actualValue = strPointer.getString(0);
        MXLibrary.INSTANCE.mxFree(strPointer);
        destroy(strArray);
        assertEquals(actualValue, expectedValue);
    }

    @Test
    public void testIs() {
        MXArray a = createDoubleMatrix(0, 0);
        assertTrue(MXLibrary.INSTANCE.mxIsNumeric(a));
        assertFalse(MXLibrary.INSTANCE.mxIsObject(a));
        assertFalse(MXLibrary.INSTANCE.mxIsOpaque(a));
        assertFalse(MXLibrary.INSTANCE.mxIsCell(a));
        assertFalse(MXLibrary.INSTANCE.mxIsSparse(a));
        assertFalse(MXLibrary.INSTANCE.mxIsStruct(a));
        destroy(a);
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
}
