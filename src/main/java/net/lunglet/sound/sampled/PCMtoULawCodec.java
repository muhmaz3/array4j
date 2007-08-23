package net.lunglet.sound.sampled;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.spi.FormatConversionProvider;

public final class PCMtoULawCodec extends FormatConversionProvider {
    private static final short BIAS = 0x84;

    private static final short CLIP = 32635;

    private static final byte[] EXP_LUT = {0, 0, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4,
            4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
            5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
            6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7,
            7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
            7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
            7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
            7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7};

    private static final boolean ZEROTRAP = false;

    static byte linear2ulaw(int sample) {
        // Get the sample into sign-magnitude.
        // set aside the sign
        int sign = (sample >>> 8) & 0x80;
        // get magnitude
        if (sign != 0) {
            sample = -sample;
        }
        // clip the magnitude
        if (sample > CLIP) {
            sample = CLIP;
        }

        // Convert from 16 bit linear to ulaw.
        sample += BIAS;
        int exponent = EXP_LUT[(sample >> 7) & 0xFF];
        int mantissa = (sample >>> (exponent + 3)) & 0x0F;
        byte ulawbyte = (byte) (~(sign | (exponent << 4) | mantissa));
        if (ZEROTRAP && ulawbyte == 0) {
            // optional CCITT trap
            ulawbyte = 0x02;
        }
        return ulawbyte;
    }

    @Override
    public AudioInputStream getAudioInputStream(final AudioFormat targetFormat, final AudioInputStream sourceStream) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AudioInputStream getAudioInputStream(final Encoding targetEncoding, final AudioInputStream sourceStream) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Encoding[] getSourceEncodings() {
        return new Encoding[]{};
    }

    @Override
    public Encoding[] getTargetEncodings() {
        return new Encoding[]{};
    }

    @Override
    public Encoding[] getTargetEncodings(final AudioFormat sourceFormat) {
        return new Encoding[]{};
    }

    @Override
    public AudioFormat[] getTargetFormats(final Encoding targetEncoding, final AudioFormat sourceFormat) {
        return new AudioFormat[]{};
    }
}
