package net.lunglet.sound.sampled;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat.Encoding;

import org.junit.Test;

public final class ULawtoPCMCodecTest {
    @Test
    public void testULaw2Linear() {
        short[] expectedValues = {32124, 31100, 30076, 29052, 28028, 27004, 25980, 24956, 23932, 22908, 21884, 20860,
                19836, 18812, 17788, 16764, 15996, 15484, 14972, 14460, 13948, 13436, 12924, 12412, 11900, 11388,
                10876, 10364, 9852, 9340, 8828, 8316, 7932, 7676, 7420, 7164, 6908, 6652, 6396, 6140, 5884, 5628, 5372,
                5116, 4860, 4604, 4348, 4092, 3900, 3772, 3644, 3516, 3388, 3260, 3132, 3004, 2876, 2748, 2620, 2492,
                2364, 2236, 2108, 1980, 1884, 1820, 1756, 1692, 1628, 1564, 1500, 1436, 1372, 1308, 1244, 1180, 1116,
                1052, 988, 924, 876, 844, 812, 780, 748, 716, 684, 652, 620, 588, 556, 524, 492, 460, 428, 396, 372,
                356, 340, 324, 308, 292, 276, 260, 244, 228, 212, 196, 180, 164, 148, 132, 120, 112, 104, 96, 88, 80,
                72, 64, 56, 48, 40, 32, 24, 16, 8, 0, -32124, -31100, -30076, -29052, -28028, -27004, -25980, -24956,
                -23932, -22908, -21884, -20860, -19836, -18812, -17788, -16764, -15996, -15484, -14972, -14460, -13948,
                -13436, -12924, -12412, -11900, -11388, -10876, -10364, -9852, -9340, -8828, -8316, -7932, -7676,
                -7420, -7164, -6908, -6652, -6396, -6140, -5884, -5628, -5372, -5116, -4860, -4604, -4348, -4092,
                -3900, -3772, -3644, -3516, -3388, -3260, -3132, -3004, -2876, -2748, -2620, -2492, -2364, -2236,
                -2108, -1980, -1884, -1820, -1756, -1692, -1628, -1564, -1500, -1436, -1372, -1308, -1244, -1180,
                -1116, -1052, -988, -924, -876, -844, -812, -780, -748, -716, -684, -652, -620, -588, -556, -524, -492,
                -460, -428, -396, -372, -356, -340, -324, -308, -292, -276, -260, -244, -228, -212, -196, -180, -164,
                -148, -132, -120, -112, -104, -96, -88, -80, -72, -64, -56, -48, -40, -32, -24, -16, -8, 0};
        byte ulawbyte = Byte.MIN_VALUE;
        for (int i = 0; i < expectedValues.length; i++) {
            short sample = ULawtoPCMCodec.ulaw2linear(ulawbyte);
            assertEquals(expectedValues[i], sample);
            ulawbyte++;
        }
    }

    @Test
    public void testConversion() throws IOException, UnsupportedAudioFileException {
        Type fileType = RawAudioFileWriter.RAW;

        // read little-endian signed PCM data
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        AudioSystem.write(SphereAudioFileReaderTest.getAudioInputStream("ex4_01.wav"), fileType, baos);
        byte[] expectedBytes = baos.toByteArray();
        baos.reset();
        // read ulaw data
        AudioInputStream sourceStream = SphereAudioFileReaderTest.getAudioInputStream("ex4.wav");
        AudioInputStream targetStream = AudioSystem.getAudioInputStream(Encoding.PCM_SIGNED, sourceStream);
        AudioSystem.write(targetStream, fileType, baos);
        byte[] actualBytes = baos.toByteArray();
        // compare the bytes
        assertEquals(32000, expectedBytes.length);
        assertEquals(expectedBytes.length, actualBytes.length);
        assertTrue(Arrays.equals(expectedBytes, actualBytes));
    }

    @Test
    public void testClipOutput() throws LineUnavailableException, IOException, InterruptedException,
            UnsupportedAudioFileException {
        AudioInputStream sourceStream = SphereAudioFileReaderTest.getAudioInputStream("ex4.wav");
        AudioInputStream targetStream = AudioSystem.getAudioInputStream(Encoding.PCM_SIGNED, sourceStream);
        DataLine.Info info = new DataLine.Info(Clip.class, targetStream.getFormat());
        Clip clip = (Clip) AudioSystem.getLine(info);
        clip.addLineListener(new LineListener() {
            @Override
            public void update(final LineEvent event) {
                Object source = event.getSource();
                synchronized (source) {
                    source.notifyAll();
                }
            }
        });
        clip.open(targetStream);
        clip.start();
        while (true) {
            synchronized (clip) {
                clip.wait();
            }
            if (!clip.isRunning()) {
                break;
            }
        }
        clip.close();
    }
}
