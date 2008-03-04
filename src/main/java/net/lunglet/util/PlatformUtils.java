package net.lunglet.util;

import com.sun.jna.Platform;

public final class PlatformUtils {
    private PlatformUtils() {
    }

    public static boolean isWindows() {
        return Platform.isWindows();
    }
}
