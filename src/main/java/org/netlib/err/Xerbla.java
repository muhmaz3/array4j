package org.netlib.err;

public final class Xerbla {
    public static void xerbla(final String srname, final int info) {
        throw new RuntimeException("On entry to " + srname + " parameter number " + info + " had an illegal value");
    }
    
    private Xerbla() {
    }
}
