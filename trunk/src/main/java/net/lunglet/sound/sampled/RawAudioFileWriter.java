package net.lunglet.sound.sampled;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.spi.AudioFileWriter;

public final class RawAudioFileWriter extends AudioFileWriter {
    public static final Type RAW = new Type("RAW", "raw");

    @Override
    public Type[] getAudioFileTypes() {
        return new Type[]{RAW};
    }

    @Override
    public Type[] getAudioFileTypes(final AudioInputStream stream) {
        return new Type[]{RAW};
    }

    @Override
    public int write(final AudioInputStream stream, final Type fileType, final OutputStream out) throws IOException {
        byte[] buf = new byte[1024];
        int bytesWritten = 0;
        while (true) {
            int bytesRead = stream.read(buf);
            if (bytesRead == -1) {
                break;
            }
            out.write(buf, 0, bytesRead);
            bytesWritten += bytesRead;
        }
        return bytesWritten;
    }

    @Override
    public int write(final AudioInputStream stream, final Type fileType, final File out) throws IOException {
        return write(stream, fileType, new FileOutputStream(out));
    }
}
