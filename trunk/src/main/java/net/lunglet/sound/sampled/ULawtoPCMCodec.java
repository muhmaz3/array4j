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

        private final byte[] bytebuf;

        private final ByteBuffer shortbuf;

        public ULawtoPCMInputStream(final AudioInputStream stream) {
            this.stream = stream;
            // buffer size to use when reading from underlying stream
            int bufsize = 65536 * stream.getFormat().getChannels();
            this.bytebuf = new byte[bufsize];
            // buffer to contain PCM output samples
            this.shortbuf =  ByteBuffer.allocate(2 * bufsize).order(ByteOrder.LITTLE_ENDIAN);
            // there is nothing to output initially, so limit the buffer to 0
            shortbuf.limit(0);
        }

        @Override
        public int read() throws IOException {
            // if there is remaining data in the output buffer, return that
            // first before reading from the underlying stream again
            if (shortbuf.hasRemaining()) {
                // mask with 0xff so that -1 isn't returned
                return shortbuf.get() & 0xff;
            }
            int bytesRead = stream.read(bytebuf);
            if (bytesRead == -1) {
                return -1;
            }
            shortbuf.clear();
            for (int i = 0; i < bytesRead; i++) {
                shortbuf.putShort(ulaw2linear((byte) bytebuf[i]));
            }
            shortbuf.position(0);
            // set limit to 2 * bytesRead because we're converting from 1-byte
            // ulaw samples to 2-byte PCM samples
            shortbuf.limit(2 * bytesRead);
            // mask with 0xff so that -1 isn't returned
            return shortbuf.get() & 0xff;
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
