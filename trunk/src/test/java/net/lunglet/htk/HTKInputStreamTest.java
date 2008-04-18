package net.lunglet.htk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.io.InputStream;
import net.lunglet.util.ArrayMath;
import org.junit.Test;

public final class HTKInputStreamTest {
    @Test
    public void testReadWaveform() throws IOException {
        InputStream stream = getClass().getResourceAsStream("ex1_01.htk");
        assertNotNull(stream);
        HTKInputStream in = new HTKInputStream(stream);
        in.mark(HTKHeader.SIZE);
        HTKHeader header = in.readHeader();
        assertEquals(HTKDataType.WAVEFORM, header.getDataType());
        assertEquals(2, header.getFrameSize());
        assertEquals(16000, header.getFrames());
        assertEquals(625, header.getFramePeriodHTK());
        assertEquals(6.25e-5f, header.getFramePeriod(), 0);
        in.reset();
        short[] waveform = in.readWaveform();
        assertEquals(16000, waveform.length);
        assertEquals(-560, ArrayMath.min(waveform));
        assertEquals(655, ArrayMath.max(waveform));
        in.close();
    }

    /**
     * <CODE>
     * SOURCEKIND = WAVEFORM<br/>
     * SOURCEFORMAT = HTK<br/>
     * SOURCERATE = 625<br/>
     * TARGETKIND = MFCC<br/>
     * TARGETRATE = 100000.0<br/>
     * WINDOWSIZE = 200000.0<br/>
     * NUMCHANS = 23<br/>
     * NUMCEPS = 12<br/>
     * SAVECOMPRESSED = F<br/>
     * SAVEWITHCRC = F<br/>
     * </CODE>
     * 
     * @throws IOException
     */
    @Test
    public void testReadMFCC() throws IOException {
        InputStream stream = getClass().getResourceAsStream("ex1_01.mfcc");
        assertNotNull(stream);
        HTKInputStream in = new HTKInputStream(stream);
        in.mark(HTKHeader.SIZE);
        HTKHeader header = in.readHeader();
        assertEquals(HTKDataType.MFCC, header.getDataType());
        assertFalse(header.hasChecksum());
        assertFalse(header.isCompressed());
        assertFalse(header.isAbsoluteEnergySuppressed());
        assertFalse(header.hasEnergy());
        assertFalse(header.hasDeltaCoefficients());
        assertFalse(header.hasAccelerationCoefficients());
        assertEquals(99, header.getFrames());
        // frame should contain 12 cepstral coefficients
        assertEquals(48, header.getFrameSize());
        in.reset();
        float[][] mfcc = in.readMFCC();
        assertEquals(99, mfcc.length);
        for (int i = 0; i < mfcc.length; i++) {
            assertEquals(12, mfcc[i].length);
            for (int j = 0; j < mfcc[i].length; j++) {
                assertTrue(mfcc[i][j] != 0.0f);
            }
        }
        assertEquals(-7.3721f, mfcc[0][0], 1e-5f);
        assertEquals(-4.3273f, mfcc[98][11], 1e-5f);
        in.close();
    }

    /**
     * <CODE>
     * SOURCERATE = 1250<br/>
     * TARGETKIND = MFCC_0_E_Z<br/>
     * </CODE>
     * 
     * @throws IOException
     */
    @Test
    public void testReadMFCC0EZ() throws IOException {
        InputStream stream = getClass().getResourceAsStream("ex1_01.mfcc_0_E_Z");
        assertNotNull(stream);
        HTKInputStream in = new HTKInputStream(stream);
        in.mark(HTKHeader.SIZE);
        HTKHeader header = in.readHeader();
        assertEquals(HTKDataType.MFCC, header.getDataType());
        assertFalse(header.hasChecksum());
        assertFalse(header.isCompressed());
        assertFalse(header.isAbsoluteEnergySuppressed());
        assertTrue(header.hasEnergy());
        assertFalse(header.hasDeltaCoefficients());
        assertFalse(header.hasAccelerationCoefficients());
        assertEquals(199, header.getFrames());
        // frame should contain 13 cepstral coefficients and energy
        assertEquals(56, header.getFrameSize());
        in.reset();
        float[][] mfcc = in.readMFCC();
        assertEquals(199, mfcc.length);
        for (int i = 0; i < mfcc.length; i++) {
            assertEquals(14, mfcc[i].length);
        }
        in.close();
    }
}
