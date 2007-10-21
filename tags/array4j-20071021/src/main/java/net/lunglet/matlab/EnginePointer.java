package net.lunglet.matlab;

import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

public class EnginePointer extends PointerType {
    public EnginePointer() {
    }

    public EnginePointer(final Pointer p) {
        super(p);
    }
}
