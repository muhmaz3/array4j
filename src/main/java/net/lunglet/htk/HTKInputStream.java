package net.lunglet.htk;

import com.googlecode.array4j.FloatMatrix;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public final class HTKInputStream extends DataInputStream {
    public HTKInputStream(final String name) throws FileNotFoundException {
        this(new BufferedInputStream(new FileInputStream(name)));
    }

    public HTKInputStream(final InputStream stream) {
        super(stream);
    }

    public HTKHeader readHeader() throws IOException {
        return new HTKHeader(this);
    }

    public FloatMatrix readMFCC() throws IOException {
        throw new UnsupportedOperationException();
    }

    public short[] readWaveform() throws IOException {
        HTKHeader header = readHeader();
        if (!header.getDataType().equals(HTKDataType.WAVEFORM)) {
            throw new IOException("Not WAVEFORM data");
        }
        if (header.getSampleSize() != 2) {
            throw new IOException("WAVEFORM sample size != 2");
        }
        short[] buf = new short[header.getSamples()];
        for (int i = 0; i < buf.length; i++) {
            buf[i] = readShort();
        }
        return buf;
    }
}
