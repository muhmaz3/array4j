package net.lunglet.htk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import com.googlecode.array4j.math.ArraysMath;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;

public final class HTKInputStreamTest {
    @Test
    public void testReadWaveform() throws IOException {
        InputStream stream = getClass().getResourceAsStream("ex1_01.htk");
        assertNotNull(stream);
        HTKInputStream in = new HTKInputStream(stream);
        in.mark(HTKHeader.SIZE);
        HTKHeader header = in.readHeader();
        assertEquals(2, header.getSampleSize());
        assertEquals(16000, header.getSamples());
        assertEquals(625, header.getSamplePeriodHTK());
        assertEquals(6.25e-5f, header.getSamplePeriod(), 0);
        in.reset();
        short[] waveform = in.readWaveform();
        assertEquals(16000, waveform.length);
        assertEquals(-560, ArraysMath.min(waveform));
        assertEquals(655, ArraysMath.max(waveform));
        in.close();
    }
}
