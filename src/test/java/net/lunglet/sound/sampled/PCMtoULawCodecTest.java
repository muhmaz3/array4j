package net.lunglet.sound.sampled;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public final class PCMtoULawCodecTest {
    @Test
    public void testLinear2ULaw() {
        assertEquals(0, PCMtoULawCodec.linear2ulaw(Short.MIN_VALUE));
        assertEquals(0, PCMtoULawCodec.linear2ulaw(-32767));
        assertEquals(15, PCMtoULawCodec.linear2ulaw(-16384));
        assertEquals(127, PCMtoULawCodec.linear2ulaw(-1));
        assertEquals(255, PCMtoULawCodec.linear2ulaw(0) & 0xff);
        assertEquals(255, PCMtoULawCodec.linear2ulaw(1) & 0xff);
        assertEquals(143, PCMtoULawCodec.linear2ulaw(16384) & 0xff);
        assertEquals(128, PCMtoULawCodec.linear2ulaw(Short.MAX_VALUE) & 0xff);
    }
}
