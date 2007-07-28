package net.lunglet.sound.sampled;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.spi.AudioFileReader;

public final class SphereAudioFileReader extends AudioFileReader {
    public static final AudioFileFormat.Type SPHERE = new AudioFileFormat.Type("SPHERE", "sph");

    private static void parseHeader(final byte[] buf) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buf)));
        // skip over first two lines in header
        String line = reader.readLine();
        line = reader.readLine();
        line = reader.readLine();
        while (line != null) {
            System.out.println(line);
            line = reader.readLine();
            String[] parts = line.split("\\s+", 3);
            if (parts[0].equals("end_head")) {
                break;
            }
            if (parts.length != 3) {
                throw new IOException("invalid header");
            }
            System.out.println(parts[0] + " " + parts[1] + " " + parts[2]);
        }
    }

    @Override
    public AudioFileFormat getAudioFileFormat(final InputStream stream) throws UnsupportedAudioFileException,
            IOException {
        DataInputStream dataStream = new DataInputStream(new BufferedInputStream(stream));
        Charset charset = Charset.forName("US-ASCII");
        try {
            byte[] buf = new byte[8];
            // mark to allow 16 bytes to be read to get everything needed
            // to check whether this is a SPEHERE file
            dataStream.mark(16);
            dataStream.readFully(buf);
            String nistHeader = new String(buf, charset);
            nistHeader = nistHeader.replaceAll("\\s+", "");
            if (!nistHeader.equals("NIST_1A")) {
                throw new UnsupportedAudioFileException("expected NIST header");
            }
        } catch (IOException e) {
            // IOExceptions at this point indicate that we're probably not
            // dealing with the Sphere format
            throw new UnsupportedAudioFileException();
        }
        byte[] buf = new byte[8];
        dataStream.readFully(buf);
        String sizeHeader = new String(buf, charset);
        // strip whitespace
        final int headerSize;
        try {
            headerSize = Integer.valueOf(sizeHeader.replaceAll("\\s+", ""));
            if (headerSize < 0) {
                throw new IOException("header size must be positive");
            }
        } catch (NumberFormatException e) {
            throw new IOException(e);
        }
        dataStream.reset();

        buf = new byte[headerSize];
        dataStream.readFully(buf);

        System.out.println("parsing header");
        parseHeader(buf);

        // TODO read sample rate from headers
//        float sampleRate = 8000.0f;
        float sampleRate = 16000.0f;
        int sampleSizeInBits = 8;
//        int sampleSizeInBits = 16;
        int channels = 2;
        // TODO might want to use NOT_SPECIFIED for frameSize
        int frameSize = 1;
        float frameRate = sampleRate;
        boolean bigEndian = false;
        Map<String, Object> formatProperties = new HashMap<String, Object>();
        Encoding encoding = Encoding.PCM_SIGNED;
        AudioFormat audioFormat = new AudioFormat(encoding, sampleRate, sampleSizeInBits, channels, frameSize,
                frameRate, bigEndian, formatProperties);
        // TODO this is hardcoded but shouldn't be
        int frameLength = 2400001;
        Map<String, Object> fileFormatProperties = new HashMap<String, Object>();
        return new AudioFileFormat(SPHERE, audioFormat, frameLength, fileFormatProperties);
    }

    @Override
    public AudioFileFormat getAudioFileFormat(final URL url) throws UnsupportedAudioFileException, IOException {
        throw new UnsupportedAudioFileException();
    }

    @Override
    public AudioFileFormat getAudioFileFormat(final File file) throws UnsupportedAudioFileException, IOException {
        return getAudioFileFormat(new FileInputStream(file));
    }

    @Override
    public AudioInputStream getAudioInputStream(final InputStream stream) throws UnsupportedAudioFileException,
            IOException {
        AudioFileFormat audioFileFormat = getAudioFileFormat(stream);
        AudioFormat audioFormat = audioFileFormat.getFormat();
        int frameLength = 2400001;
        return new AudioInputStream(stream, audioFormat, frameLength);
    }

    @Override
    public AudioInputStream getAudioInputStream(final URL url) throws UnsupportedAudioFileException, IOException {
        throw new UnsupportedAudioFileException();
    }

    @Override
    public AudioInputStream getAudioInputStream(final File file) throws UnsupportedAudioFileException, IOException {
        return getAudioInputStream(new FileInputStream(file));
    }
}
