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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.spi.AudioFileReader;

public final class SphereAudioFileReader extends AudioFileReader {
    private static final String CHANNELS_PROPERTY = "channel_count";

    private static final String ENCODING_PROPERTY = "sample_coding";

    private static final String ENDIAN_PROPERTY = "sample_byte_format";

    private static final String FRAME_LENGTH_PROPERTY = "sample_count";

    private static final String SAMPLE_RATE_PROPERTY = "sample_rate";

    private static final String SAMPLE_SIZE_PROPERTY = "sample_n_bytes";

    public static final AudioFileFormat.Type SPHERE = new AudioFileFormat.Type("SPHERE", "sph");

    private static final List<String> ULAW_ENCODINGS = Arrays.asList(new String[]{"mu-law", "ulaw"});

    private static void checkRequiredHeader(final Map<String, Object> properties, final String key) throws IOException {
        if (!properties.containsKey(key)) {
            throw new IOException(key + " property is required");
        }
    }

    private static Map<String, Object> parseProperties(final byte[] buf) throws IOException {
        Map<String, Object> properties = new HashMap<String, Object>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buf)));
        // skip over first two lines in header
        String line = reader.readLine();
        line = reader.readLine();
        line = reader.readLine();
        while (line != null) {
            String[] parts = line.split(" ", 3);
            if (parts[0].equals("end_head")) {
                break;
            }
            if (parts.length != 3) {
                throw new IOException("invalid header");
            }
            String key = parts[0];
            final Object value;
            if (parts[1].startsWith("-s")) {
                String sizeString = parts[1].substring(2);
                final int length;
                try {
                    length = Integer.valueOf(sizeString);
                } catch (NumberFormatException e) {
                    throw new IOException(e);
                }
                if (parts[2].length() != length) {
                    throw new IOException("invalid header");
                }
                value = parts[2];
            } else if (parts[1].equals("-i")) {
                try {
                    value = Integer.valueOf(parts[2]);
                } catch (NumberFormatException e) {
                    throw new IOException(e);
                }
            } else if (parts[1].equals("-r")) {
                try {
                    value = Double.valueOf(parts[2]);
                } catch (NumberFormatException e) {
                    throw new IOException(e);
                }
            } else {
                throw new IOException("invalid header");
            }
            properties.put(key, value);
            line = reader.readLine();
        }
        return properties;
    }

    @Override
    public AudioFileFormat getAudioFileFormat(final File file) throws UnsupportedAudioFileException, IOException {
        InputStream stream = new BufferedInputStream(new FileInputStream(file));
        try {
            return getAudioFileFormat(stream);
        } finally {
            stream.close();
        }
    }

    @Override
    public AudioFileFormat getAudioFileFormat(final InputStream stream) throws UnsupportedAudioFileException,
            IOException {
        DataInputStream dataStream = new DataInputStream(stream);
        Charset charset = Charset.forName("US-ASCII");
        try {
            byte[] buf = new byte[8];
            // mark to allow 16 bytes to be read to get everything needed
            // to check whether this is a SPHERE file
            dataStream.mark(16);
            dataStream.readFully(buf);
            String nistHeader = new String(buf, charset);
            nistHeader = nistHeader.replaceAll("\\s+", "");
            if (!nistHeader.equals("NIST_1A")) {
                throw new UnsupportedAudioFileException("expected NIST header");
            }
        } catch (IOException e) {
            // IOExceptions at this point indicate that we're probably not
            // dealing with the SPHERE format
            throw new UnsupportedAudioFileException();
        }

        // read header size
        byte[] buf = new byte[8];
        dataStream.readFully(buf);
        String sizeHeader = new String(buf, charset);
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

        // parse properties from the header
        buf = new byte[headerSize];
        dataStream.readFully(buf);
        Map<String, Object> properties = parseProperties(buf);

        // sample rate
        checkRequiredHeader(properties, SAMPLE_RATE_PROPERTY);
        float sampleRate = (Integer) properties.get(SAMPLE_RATE_PROPERTY);
        float frameRate = sampleRate;

        // sample size
        checkRequiredHeader(properties, SAMPLE_SIZE_PROPERTY);
        int sampleSize = (Integer) properties.get(SAMPLE_SIZE_PROPERTY);
        int sampleSizeInBits = sampleSize * 8;

        // channels
        checkRequiredHeader(properties, CHANNELS_PROPERTY);
        int channels = (Integer) properties.get(CHANNELS_PROPERTY);
        int frameSize = sampleSize * channels;

        // endianness
        final boolean bigEndian;
        if (properties.containsKey(ENDIAN_PROPERTY)) {
            String value = (String) properties.get(ENDIAN_PROPERTY);
            if (value.startsWith("1")) {
                bigEndian = true;
            } else if (value.startsWith("0")) {
                bigEndian = false;
            } else {
                throw new IOException("invalid " + ENDIAN_PROPERTY + " property");
            }
        } else {
            bigEndian = true;
        }

        // encoding
        checkRequiredHeader(properties, ENCODING_PROPERTY);
        final Encoding encoding;
        if ("pcm".equals(properties.get(ENCODING_PROPERTY))) {
            encoding = Encoding.PCM_SIGNED;
        } else if (ULAW_ENCODINGS.contains(properties.get(ENCODING_PROPERTY))) {
            encoding = Encoding.ULAW;
        } else {
            throw new IOException("sample_coding property is required");
        }

        checkRequiredHeader(properties, FRAME_LENGTH_PROPERTY);
        int frameLength = (Integer) properties.get(FRAME_LENGTH_PROPERTY);

        return new AudioFileFormat(SPHERE, new AudioFormat(encoding, sampleRate, sampleSizeInBits, channels, frameSize,
                frameRate, bigEndian, properties), frameLength);
    }

    @Override
    public AudioFileFormat getAudioFileFormat(final URL url) throws UnsupportedAudioFileException, IOException {
        throw new UnsupportedAudioFileException();
    }

    @Override
    public AudioInputStream getAudioInputStream(final File file) throws UnsupportedAudioFileException, IOException {
        return getAudioInputStream(new FileInputStream(file));
    }

    @Override
    public AudioInputStream getAudioInputStream(final InputStream stream) throws UnsupportedAudioFileException,
            IOException {
        BufferedInputStream bis = new BufferedInputStream(stream);
        AudioFileFormat audioFileFormat = getAudioFileFormat(bis);
        AudioFormat audioFormat = audioFileFormat.getFormat();
        return new AudioInputStream(bis, audioFormat, audioFileFormat.getFrameLength());
    }

    @Override
    public AudioInputStream getAudioInputStream(final URL url) throws UnsupportedAudioFileException, IOException {
        throw new UnsupportedAudioFileException();
    }
}
