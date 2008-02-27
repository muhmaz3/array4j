package net.lunglet.htk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.Test;

public final class HTKOutputStreamTest {
    @Test
    public void testWriteMFCC() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HTKOutputStream out = new HTKOutputStream(baos);
        float[][] mfcc = new float[99][14];
        // set frame period to 10ms
        int framePeriod = 10000000;
        int flags = HTKFlags.HAS_C0 | HTKFlags.HAS_ENERGY | HTKFlags.ZERO_MEAN;
        out.writeMFCC(mfcc, framePeriod, flags);
        out.close();
        HTKInputStream in = new HTKInputStream(new ByteArrayInputStream(baos.toByteArray()));
        in.mark(HTKHeader.SIZE);
        HTKHeader header = in.readHeader();
        assertEquals(framePeriod, header.getFramePeriodHTK());
        assertEquals(mfcc.length, header.getFrames());
        assertEquals(mfcc[0].length * 4, header.getFrameSize());
        assertTrue(header.hasEnergy());
        assertTrue(header.hasZeroMean());
        in.reset();
        float[][] mfcc2 = in.readMFCC();
        assertEquals(mfcc.length, mfcc2.length);
        for (int i = 0; i < mfcc.length; i++) {
            assertEquals(mfcc[i].length, mfcc2[i].length);
            for (int j = 0; j < mfcc[i].length; j++) {
                assertEquals(mfcc[i][j], mfcc2[i][j], 0);
            }
        }
        in.close();
    }
}
