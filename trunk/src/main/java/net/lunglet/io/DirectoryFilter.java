package net.lunglet.io;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Filter filenames that are directories.
 */
public final class DirectoryFilter implements FilenameFilter {
    public boolean accept(final File dir, final String name) {
        return new File(dir, name).isDirectory();
    }
}
