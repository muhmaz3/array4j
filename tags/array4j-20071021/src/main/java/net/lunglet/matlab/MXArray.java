package net.lunglet.matlab;

import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

public class MXArray extends PointerType {
    public MXArray() {
    }

    public MXArray(final Pointer p) {
        super(p);
    }
}
