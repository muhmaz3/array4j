package net.lunglet.matlab;

import com.sun.jna.Platform;
import com.sun.jna.ptr.ByteByReference;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
public final class Engine {
    private final EnginePointer ep;

    public Engine() {
        this(false);
    }

    public Engine(final boolean visible) {
        this(Platform.isWindows() ? null : "matlab", visible);
    }

    public Engine(final String startcmd, final boolean visible) {
        this.ep = EngineLibrary.INSTANCE.engOpen(startcmd);
        EngineLibrary.INSTANCE.engOutputBuffer(ep, null, 0);
        setVisible(visible);
    }

    public void close() {
        EngineLibrary.INSTANCE.engClose(ep);
    }

    private String eval(final byte[] command) {
        int result = EngineLibrary.INSTANCE.engEvalString(ep, command);
        if (result != 0) {
            throw new RuntimeException("MATLAB session no longer running");
        }
        // TODO return contents of temporary output buffer
        return null;
    }

    public String eval(final InputStream stream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] b = new byte[16384];
        int len = 0;
        while (len != -1) {
            len = stream.read(b);
            if (len > 0) {
                baos.write(b, 0, len);
            }
        }
        return eval(baos.toByteArray());
    }

    public String eval(final String command) {
        return eval(command.getBytes());
    }

    public MXArray getVariable(final String name) {
        return EngineLibrary.INSTANCE.engGetVariable(ep, name);
    }

    public boolean isVisible() {
        ByteByReference visible = new ByteByReference();
        int result = EngineLibrary.INSTANCE.engGetVisible(ep, visible);
        if (result != 0) {
            throw new RuntimeException("engGetVisible failed");
        }
        return visible.getByte(0) == 1;
    }

    public void putVariable(final String name, final MXArray value) {
        int result = EngineLibrary.INSTANCE.engPutVariable(ep, name, value);
        if (result != 0) {
            throw new RuntimeException("engPutVariable failed");
        }
    }

    public void setVisible(final boolean visible) {
        int result = EngineLibrary.INSTANCE.engSetVisible(ep, visible);
        if (result != 0) {
            throw new RuntimeException("engSetVisible failed");
        }
    }
}
