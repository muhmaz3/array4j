package net.lunglet.spro;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

public final class SProLibraryTest {
    @Test
    public void testEnums() {
        assertEquals(0, SProWindow.NULL.ordinal());
        assertEquals(1, SProWindow.HAMMING.ordinal());
        assertEquals(2, SProWindow.HANNING.ordinal());
        assertEquals(3, SProWindow.BLACKMAN.ordinal());
        assertEquals(0, SProSignalFormat.PCM16.ordinal());
        assertEquals(1, SProSignalFormat.WAVE.ordinal());
        assertEquals(2, SProSignalFormat.SPHERE.ordinal());
        assertEquals(0, SProConvertionMode.DUPLICATE.ordinal());
        assertEquals(1, SProConvertionMode.REPLACE.ordinal());
        assertEquals(2, SProConvertionMode.UPDATE.ordinal());
    }

    @Test
    public void testInstance() {
        assertNotNull(SProLibrary.INSTANCE);
    }

    @Test
    public void testSignal() {
        SProSignal signal = SProSignal.createInstance(123);
        signal.normalize(false);
        signal.normalize(true);
        signal.free();
    }

    @Test
    public void testBuffer() {
        SProBuffer buf = SProBuffer.createInstance(0, 0);
        buf.free();
    }
}
