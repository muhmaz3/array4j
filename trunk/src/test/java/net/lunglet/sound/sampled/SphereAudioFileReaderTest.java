package net.lunglet.sound.sampled;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.spi.AudioFileReader;

import org.junit.Test;

import com.sun.media.sound.JDK13Services;

public final class SphereAudioFileReaderTest {
    /** Maximum size for any of the test segments. */
    private static final int MAX_SIZE = 131072;

    private static String md5sum(final InputStream in, final int size) throws IOException {
        DataInputStream dis = new DataInputStream(in);
        byte[] buf = new byte[size];
        dis.readFully(buf);
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        md5.update(buf);
        StringBuilder md5StringBuilder = new StringBuilder();
        for (byte b : md5.digest()) {
            md5StringBuilder.append(String.format("%02x", b));
        }
        return md5StringBuilder.toString();
    }

    @Test
    public void testAudioFileReaderRegistered() {
        List<?> providers = JDK13Services.getProviders(AudioFileReader.class);
        boolean found = false;
        for (Object provider : providers) {
            if (provider instanceof SphereAudioFileReader) {
                found = true;
            }
        }
        assertTrue(found);
    }

    private AudioInputStream getAudioInputStream(final String name, final int size, final String md5sum)
            throws IOException, UnsupportedAudioFileException {
        InputStream in = this.getClass().getResourceAsStream(name);
        assertNotNull(in);
        BufferedInputStream bis = new BufferedInputStream(in);
        bis.mark(MAX_SIZE);
        assertEquals(md5sum, md5sum(bis, size));
        bis.reset();
        AudioInputStream ais = AudioSystem.getAudioInputStream(bis);
        assertNotNull(ais);
        return ais;
    }

    @Test
    public void testEx101() throws UnsupportedAudioFileException, IOException {
        AudioInputStream ais = getAudioInputStream("ex1_01.wav", 33024, "8650a5d1f1471f184b1cf3c3e3b2b29b");
        AudioFormat format = ais.getFormat();
    }

    @Test
    public void testEx110() throws UnsupportedAudioFileException, IOException {
        AudioInputStream ais = getAudioInputStream("ex1_10.wav", 33024, "6bc80e6a6d82ea4a69cbd133bdd2a3b9");
        AudioFormat format = ais.getFormat();
    }

    @Test
    public void testEx201() throws UnsupportedAudioFileException, IOException {
        AudioInputStream ais = getAudioInputStream("ex2_01.wav", 33024, "473003484dd492ce0b2adcaf98ec920a");
        AudioFormat format = ais.getFormat();
    }

    @Test
    public void testEx210() throws UnsupportedAudioFileException, IOException {
        AudioInputStream ais = getAudioInputStream("ex2_10.wav", 33024, "5660441a332795be89759db8e0c2ccbc");
        AudioFormat format = ais.getFormat();
    }

    @Test
    public void testEx4() throws UnsupportedAudioFileException, IOException {
        AudioInputStream ais = getAudioInputStream("ex4.wav", 17024, "73e22ce2e73ac6f110b7ebe140fcc69b");
        AudioFormat format = ais.getFormat();
    }

    @Test
    public void testEx401() throws UnsupportedAudioFileException, IOException {
        AudioInputStream ais = getAudioInputStream("ex4_01.wav", 33024, "b4deddb6704665087f0ada78211d44c5");
        AudioFormat format = ais.getFormat();
    }

    @Test
    public void testEx410() throws UnsupportedAudioFileException, IOException {
        AudioInputStream ais = getAudioInputStream("ex4_10.wav", 33024, "eda37911758e16afbf4de61db3a76e39");
        AudioFormat format = ais.getFormat();
    }

    @Test
    public void testEx5() throws UnsupportedAudioFileException, IOException {
        AudioInputStream ais = getAudioInputStream("ex5.wav", 33024, "4e0c04f165bf30eed0bf0aea75d3cccc");
        AudioFormat format = ais.getFormat();
    }

    @Test
    public void testEx5p() throws UnsupportedAudioFileException, IOException {
        AudioInputStream ais = getAudioInputStream("ex5_p.wav", 65024, "4db87101722bc8086d103c2479ad6a5d");
        AudioFormat format = ais.getFormat();
    }

    @Test
    public void testEx5p01() throws UnsupportedAudioFileException, IOException {
        AudioInputStream ais = getAudioInputStream("ex5_p01.wav", 65024, "6a59a8a072d735feda583787e8203a6a");
        AudioFormat format = ais.getFormat();
    }

    @Test
    public void testEx5c1() throws UnsupportedAudioFileException, IOException {
        AudioInputStream ais = getAudioInputStream("ex5_c1.wav", 17024, "e3113334cf3b347607ede08ddcfd0134");
        AudioFormat format = ais.getFormat();
    }

    @Test
    public void testEx5c1p() throws UnsupportedAudioFileException, IOException {
        AudioInputStream ais = getAudioInputStream("ex5_c1_p.wav", 33024, "a95d9a49344c822f8a60e5bcd48ca982");
        AudioFormat format = ais.getFormat();
    }

    @Test
    public void testEx5c2() throws UnsupportedAudioFileException, IOException {
        AudioInputStream ais = getAudioInputStream("ex5_c2.wav", 17024, "f7df3b17aad8337f9748fdd7fb8359de");
        AudioFormat format = ais.getFormat();
    }

    @Test
    public void testEx5c2p() throws UnsupportedAudioFileException, IOException {
        AudioInputStream ais = getAudioInputStream("ex5_c2_p.wav", 33024, "ec3ec5b50c8ee47df0043b0a903e4d49");
        AudioFormat format = ais.getFormat();
    }

    @Test
    public void testEx512() throws UnsupportedAudioFileException, IOException {
        AudioInputStream ais = getAudioInputStream("ex5_12.wav", 17024, "8b19477ffdc4ed94abb65af950bb4cf8");
        AudioFormat format = ais.getFormat();
    }

    @Test
    public void testEx512p() throws UnsupportedAudioFileException, IOException {
        AudioInputStream ais = getAudioInputStream("ex5_12_p.wav", 33024, "9a49dabcc340912a76057b687c29d51a");
        AudioFormat format = ais.getFormat();
    }

    @Test
    public void testEx6() throws UnsupportedAudioFileException, IOException {
        AudioInputStream ais = getAudioInputStream("ex6.wav", 65024, "20f533bb92c820e65f18a16ec0b85075");
        AudioFormat format = ais.getFormat();
    }
}
