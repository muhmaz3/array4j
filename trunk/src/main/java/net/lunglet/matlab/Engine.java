package net.lunglet.matlab;

import com.sun.jna.ptr.ByteByReference;
import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
public final class Engine {
    private final EnginePointer ep;

    public Engine() {
        this(false);
    }

    public Engine(final boolean visible) {
        this.ep = EngineLibrary.INSTANCE.engOpen(null);
        EngineLibrary.INSTANCE.engOutputBuffer(ep, null, 0);
        setVisible(visible);
    }

    public String eval(final String command) {
        int result = EngineLibrary.INSTANCE.engEvalString(ep, command);
        if (result != 0) {
            throw new RuntimeException("MATLAB session no longer running");
        }
        // TODO return contents of temporary output buffer
        return null;
    }

    public MXArray getVariable(final String name) {
        return EngineLibrary.INSTANCE.engGetVariable(ep, name);
    }

    public void putVariable(final String name, final MXArray value) {
        int result = EngineLibrary.INSTANCE.engPutVariable(ep, name, value);
        if (result != 0) {
            throw new RuntimeException("engPutVariable failed");
        }
    }

    public void close() {
        EngineLibrary.INSTANCE.engClose(ep);
    }

    public boolean isVisible() {
        ByteByReference visible = new ByteByReference();
        int result = EngineLibrary.INSTANCE.engGetVisible(ep, visible);
        if (result != 0) {
            throw new RuntimeException("engGetVisible failed");
        }
        return visible.getByte(0) == 1;
    }

    public void setVisible(final boolean visible) {
        int result = EngineLibrary.INSTANCE.engSetVisible(ep, visible);
        if (result != 0) {
            throw new RuntimeException("engSetVisible failed");
        }
    }
}
