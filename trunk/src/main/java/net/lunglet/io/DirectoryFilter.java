package net.lunglet.io;

import java.io.File;
import java.io.FilenameFilter;

public final class DirectoryFilter implements FilenameFilter {
    public boolean accept(final File dir, final String name) {
        return new File(dir, name).isDirectory();
    }
}
