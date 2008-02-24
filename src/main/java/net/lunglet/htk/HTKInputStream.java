package net.lunglet.htk;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

// TODO allow byte order to be specified (big endian is currently assumed)

public final class HTKInputStream extends DataInputStream {
    public HTKInputStream(final File file) throws FileNotFoundException {
        this(new BufferedInputStream(new FileInputStream(file)));
    }

    public HTKInputStream(final InputStream stream) {
        super(stream);
    }

    public HTKInputStream(final String name) throws FileNotFoundException {
        this(new File(name));
    }

    public HTKHeader readHeader() throws IOException {
        return new HTKHeader(this);
    }

    public float[][] readMFCC() throws IOException {
        HTKHeader header = readHeader();
        if (!header.getDataType().equals(HTKDataType.MFCC)) {
            throw new IOException("Not MFCC data");
        }
        if (header.getFrameSize() % 4 != 0) {
            throw new IOException("MFCC frame size not a multiple of 4 bytes");
        }
        float[][] mfcc = new float[header.getFrames()][];
        for (int i = 0; i < mfcc.length; i++) {
            mfcc[i] = new float[header.getFrameSize() / 4];
            for (int j = 0; j < mfcc[i].length; j++) {
                mfcc[i][j] = readFloat();
            }
        }
        return mfcc;
    }

    public short[] readWaveform() throws IOException {
        HTKHeader header = readHeader();
        if (!header.getDataType().equals(HTKDataType.WAVEFORM)) {
            throw new IOException("Not WAVEFORM data");
        }
        if (header.getFrameSize() != 2) {
            throw new IOException("WAVEFORM frame size must be 2 bytes");
        }
        short[] buf = new short[header.getFrames()];
        for (int i = 0; i < buf.length; i++) {
            buf[i] = readShort();
        }
        return buf;
    }
}
