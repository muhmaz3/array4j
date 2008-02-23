package net.lunglet.htk;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class HTKHeader {
    /** Include delta coefficients. */
    private static final int INCLUDE_DELTA = 0x100;

    /** Includes energy terms. */
    private static final int INCLUDE_ENERGY = 0x40;

    /** Size of the HTK header in bytes. */
    public static final int SIZE = 12;

    /** Suppress absolute energy. */
    private static final int SUPPRESS_ENERGY = 0x80;

    /** Zero mean static coefficients. */
    private static final int ZERO_MEAN = 0x800;

    private final short parmKind;

    private final int samplePeriod;

    private final int samples;

    private final short sampleSize;

    public HTKHeader(final InputStream in) throws IOException {
        DataInputStream stream = new DataInputStream(in);
        this.samples = stream.readInt();
        this.samplePeriod = stream.readInt();
        this.sampleSize = stream.readShort();
        this.parmKind = stream.readShort();

        if ((parmKind & INCLUDE_ENERGY) != 0) {
            System.out.println("ENEGRY!");
        }
        if ((parmKind & SUPPRESS_ENERGY) != 0) {
            System.out.println("supress abs energy");
        }
        if ((parmKind & INCLUDE_DELTA) != 0) {
            System.out.println("has deltas");
        }
    }

    public HTKDataType getDataType() {
        return HTKDataType.values()[parmKind & 0x3f];
    }

    /** Returns the sample period in seconds. */
    public float getSamplePeriod() {
        return samplePeriod / 1.0e7f;
    }

    /** Returns the samp[le period in HTK units (100ns). */
    public int getSamplePeriodHTK() {
        return samplePeriod;
    }

    /** Returns the number of samples. */
    public int getSamples() {
        return samples;
    }

    public short getSampleSize() {
        return sampleSize;
    }

    public boolean hasDeltaIncluded() {
        return (parmKind & INCLUDE_DELTA) != 0;
    }

    public boolean hasEnergyIncluded() {
        return (parmKind & INCLUDE_ENERGY) != 0;
    }

    public boolean hasEnergySuppressed() {
        return (parmKind & SUPPRESS_ENERGY) != 0;
    }

    public boolean hasZeroMean() {
        return (parmKind & ZERO_MEAN) != 0;
    }
}
