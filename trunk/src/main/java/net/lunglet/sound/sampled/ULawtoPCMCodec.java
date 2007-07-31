package net.lunglet.sound.sampled;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.spi.FormatConversionProvider;

public final class ULawtoPCMCodec extends FormatConversionProvider {
    private static final short[] EXP_LUT = {0, 132, 396, 924, 1980, 4092, 8316, 16764};

    static short ulaw2linear(byte ulawbyte) {
        ulawbyte = (byte) ~ulawbyte;
        short sign = (short) (ulawbyte & 0x80);
        short exponent = (short) ((ulawbyte >> 4) & 0x07);
        short mantissa = (short) (ulawbyte & 0x0F);
        short sample = (short) (EXP_LUT[exponent] + (mantissa << (exponent + 3)));
        if (sign != 0) {
            sample = (short) -sample;
        }
        return sample;
    }

    private final class ULawtoPCMInputStream extends InputStream {
        private final InputStream stream;

        private final ByteBuffer buf;

        public ULawtoPCMInputStream(final InputStream stream) {
            this.stream = stream;
            this.buf = ByteBuffer.wrap(new byte[2]).order(ByteOrder.LITTLE_ENDIAN);
            buf.position(buf.capacity());
        }

        @Override
        public int read() throws IOException {
            if (buf.hasRemaining()) {
                return buf.get() & 0xff;
            }
            int temp = stream.read();
            if (temp == -1) {
                System.out.println("done with underlying stream");
                return -1;
            }
            buf.putShort(0, ulaw2linear((byte) temp));
            buf.position(0);
            return buf.get() & 0xff;
        }
    }

    @Override
    public AudioInputStream getAudioInputStream(final Encoding targetEncoding, final AudioInputStream sourceStream) {
        AudioFormat format = getTargetFormats(targetEncoding, sourceStream.getFormat())[0];
        return new AudioInputStream(new ULawtoPCMInputStream(sourceStream), format, sourceStream.getFrameLength());
    }

    @Override
    public AudioInputStream getAudioInputStream(final AudioFormat targetFormat, final AudioInputStream sourceStream) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Encoding[] getSourceEncodings() {
        return new Encoding[]{Encoding.ULAW};
    }

    @Override
    public Encoding[] getTargetEncodings() {
        return new Encoding[]{Encoding.PCM_SIGNED};
    }

    @Override
    public Encoding[] getTargetEncodings(final AudioFormat sourceFormat) {
        if (Encoding.ULAW.equals(sourceFormat.getEncoding())) {
            return new Encoding[]{Encoding.PCM_SIGNED};
        }
        return new Encoding[]{};
    }

    @Override
    public AudioFormat[] getTargetFormats(final Encoding targetEncoding, final AudioFormat sourceFormat) {
        if (Encoding.ULAW.equals(sourceFormat.getEncoding())) {
            return new AudioFormat[]{new AudioFormat(Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), 16,
                    sourceFormat.getChannels(), 2 * sourceFormat.getChannels(), sourceFormat.getFrameRate(), false)};
        }
        return new AudioFormat[]{};
    }
}
