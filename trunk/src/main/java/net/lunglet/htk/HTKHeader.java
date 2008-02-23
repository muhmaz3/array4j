package net.lunglet.htk;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class HTKHeader implements HTKFlags {
    /** Size of the HTK header in bytes. */
    public static final int SIZE = 12;

    private static final int CHECKSUM = 0x1000;

    private static final int COMPRESSED = 0x400;

    private final int framePeriod;

    private final int frames;

    private final short frameSize;

    private final short parmKind;

    public HTKHeader(final InputStream in) throws IOException {
        DataInputStream stream = new DataInputStream(in);
        this.frames = stream.readInt();
        this.framePeriod = stream.readInt();
        this.frameSize = stream.readShort();
        this.parmKind = stream.readShort();
    }

    public HTKDataType getDataType() {
        return HTKDataType.values()[parmKind & 0x3f];
    }

    /** Returns the frame period in seconds. */
    public float getFramePeriod() {
        return framePeriod / 1.0e7f;
    }

    /** Returns the frame period in HTK units (100ns). */
    public int getFramePeriodHTK() {
        return framePeriod;
    }

    /** Returns the number of frames. */
    public int getFrames() {
        return frames;
    }

    public short getFrameSize() {
        return frameSize;
    }

    public boolean hasAccelerationCoefficients() {
        return (parmKind & HAS_ACCELERATION) != 0;
    }

    public boolean hasCRCChecksum() {
        return (parmKind & CHECKSUM) != 0;
    }

    public boolean hasDeltaCoefficients() {
        return (parmKind & HAS_DELTA) != 0;
    }

    public boolean hasEnergy() {
        return (parmKind & HAS_ENERGY) != 0;
    }

    public boolean hasZeroMean() {
        return (parmKind & ZERO_MEAN) != 0;
    }

    public boolean isAbsoluteEnergySuppressed() {
        return (parmKind & SUPPRESS_ABSOLUTE_ENERGY) != 0;
    }

    public boolean isCompressed() {
        return (parmKind & COMPRESSED) != 0;
    }
}
