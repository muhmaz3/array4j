package net.lunglet.io;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

/**
 * Filter filenames that are directories.
 */
public final class DirectoryFilter implements FilenameFilter, FileFilter {
    @Override
    public boolean accept(final File dir, final String name) {
        return new File(dir, name).isDirectory();
    }

    @Override
    public boolean accept(final File file) {
        return file.isDirectory();
    }
}
