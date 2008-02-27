package net.lunglet.htk;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

// TODO allow byte order to be specified (big endian is currently assumed)

public final class HTKOutputStream extends DataOutputStream {
    private static void checkFlags(final int flags) {
        if (flags < 0x40 || flags > 0xffc0) {
            throw new IllegalArgumentException("Invalid flags");
        }
    }

    private static void checkFramePeriod(final int framePeriod) {
        if (framePeriod <= 0) {
            throw new IllegalArgumentException("Frame period must be positive");
        }
    }

    public HTKOutputStream(final File file) throws FileNotFoundException {
        this(new BufferedOutputStream(new FileOutputStream(file)));
    }

    public HTKOutputStream(final OutputStream out) {
        this(out, false, false);
    }

    public HTKOutputStream(final OutputStream out, final boolean compress, final boolean checksum) {
        super(out);
    }

    public HTKOutputStream(final String name) throws FileNotFoundException {
        this(name, false, false);
    }

    public HTKOutputStream(final String name, final boolean compress, final boolean checksum)
            throws FileNotFoundException {
        this(new BufferedOutputStream(new FileOutputStream(name)), compress, checksum);
    }

    public void writeMFCC(final float[][] mfcc, final int framePeriod, final int flags) throws IOException {
        checkFramePeriod(framePeriod);
        checkFlags(flags);
        writeInt(mfcc.length);
        writeInt(framePeriod);
        writeShort(mfcc.length > 0 ? mfcc[0].length * 4 : 0);
        writeShort(flags | HTKDataType.MFCC.ordinal());
        for (int i = 0; i < mfcc.length; i++) {
            for (int j = 0; j < mfcc[i].length; j++) {
                writeFloat(mfcc[i][j]);
            }
        }
    }

    /**
     * Write waveform from little endian buffer.
     */
    public void writeWaveform(final byte[] b, final int framePeriod) throws IOException {
        if (b.length % 2 != 0) {
            throw new IllegalArgumentException();
        }
        checkFramePeriod(framePeriod);
        // write number of samples
        writeInt(b.length / 2);
        writeInt(framePeriod);
        // write sample size
        writeShort(2);
        // write parameter kind
        writeShort(HTKDataType.WAVEFORM.ordinal());
        // write buffer in big endian order
        for (int i = 0; i < b.length - 1; i += 2) {
            write(b[i + 1]);
            write(b[i]);
        }
    }

    public void writeWaveform(final short[] b, final int framePeriod) throws IOException {
        writeInt(b.length);
        writeInt(framePeriod);
        writeShort(2);
        writeShort(HTKDataType.WAVEFORM.ordinal());
        for (int i = 0; i < b.length; i++) {
            write(b[i]);
        }
    }
}
