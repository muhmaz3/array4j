package net.lunglet.matlab;

import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

public class Engine extends PointerType {
    public Engine() {
    }

    public Engine(final Pointer p) {
        super(p);
    }
}