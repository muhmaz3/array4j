package net.lunglet.htk;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public final class HTKDataTypeTest {
    @Test
    public void test() {
        assertEquals(0, HTKDataType.WAVEFORM.ordinal());
        assertEquals(1, HTKDataType.LPC.ordinal());
        assertEquals(2, HTKDataType.LPREFC.ordinal());
        assertEquals(3, HTKDataType.LPCEPSTRA.ordinal());
        assertEquals(4, HTKDataType.LPDELCEP.ordinal());
        assertEquals(5, HTKDataType.IREFC.ordinal());
        assertEquals(6, HTKDataType.MFCC.ordinal());
        assertEquals(7, HTKDataType.FBANK.ordinal());
        assertEquals(8, HTKDataType.MELSPEC.ordinal());
        assertEquals(9, HTKDataType.USER.ordinal());
        assertEquals(10, HTKDataType.DISCRETE.ordinal());
        assertEquals(11, HTKDataType.PLP.ordinal());
    }
}
