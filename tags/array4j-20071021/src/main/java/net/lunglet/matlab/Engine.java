package net.lunglet.matlab;

import com.sun.jna.Platform;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
public final class Engine {
    private static final int DEFAULT_OUTPUT_BUFFER_SIZE = 64 * 1024;

    private final EnginePointer ep;

    private final int outputBufferSize;

    public Engine() {
        this(false);
    }

    public Engine(final boolean visible) {
        this(Platform.isWindows() ? null : "matlab", visible, true, DEFAULT_OUTPUT_BUFFER_SIZE);
    }

    public Engine(final String startcmd, final boolean visible, final boolean singleUse, final int outputBufferSize) {
        if (startcmd != null && Platform.isWindows()) {
            throw new IllegalArgumentException("startcmd must be null on Windows");
        }
        if (singleUse) {
            IntByReference retstatus = new IntByReference();
            this.ep = EngineLibrary.INSTANCE.engOpenSingleUse(startcmd, null, retstatus);
            if (ep == null) {
                throw new RuntimeException("engOpenSingleUse failed (retstatus=" + retstatus.getValue() + ")");
            }
        } else {
            this.ep = EngineLibrary.INSTANCE.engOpen(startcmd);
            if (ep == null) {
                throw new RuntimeException("engOpen failed");
            }
        }
        this.outputBufferSize = outputBufferSize;
        EngineLibrary.INSTANCE.engOutputBuffer(ep, null, 0);
        setVisible(visible);
    }

    public void close() {
        EngineLibrary.INSTANCE.engClose(ep);
    }

    /**
     * @param command null-terminated command to evaluate
     */
    private String eval(final byte[] command) {
        if (command[command.length - 1] != 0) {
            throw new IllegalArgumentException("command must be null-terminated");
        }
        ByteBuffer buffer = ByteBuffer.allocateDirect(outputBufferSize);
        EngineLibrary.INSTANCE.engOutputBuffer(ep, buffer, buffer.capacity());
        // TODO might want engEvalString(ep, "evalin('base', expression)") here
        int result = EngineLibrary.INSTANCE.engEvalString(ep, command);
        if (result != 0) {
            throw new RuntimeException("MATLAB session no longer running");
        }
        EngineLibrary.INSTANCE.engOutputBuffer(ep, null, 0);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int i = 0; i < buffer.capacity(); i++) {
            byte value = buffer.get(i);
            if (value != 0) {
                baos.write(value);
            }
        }
        return new String(baos.toByteArray()).trim();
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
        baos.write(0);
        return eval(baos.toByteArray());
    }

    public String eval(final String command) {
        byte[] bytes = command.getBytes();
        byte[] nullBytes = new byte[bytes.length + 1];
        System.arraycopy(bytes, 0, nullBytes, 0, bytes.length);
        return eval(nullBytes);
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
