package net.lunglet.io;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

public final class FilenameSuffixFilter implements FilenameFilter, FileFilter {
    private final boolean ignoreCase;

    private final String suffix;

    public FilenameSuffixFilter(final String suffix) {
        this(suffix, false);
    }

    public FilenameSuffixFilter(final String suffix, final boolean ignoreCase) {
        this.suffix = suffix;
        this.ignoreCase = ignoreCase;
    }

    @Override
    public boolean accept(final File dir, final String name) {
        return accept(new File(dir, name));
    }

    @Override
    public boolean accept(final File file) {
        if (!file.isFile()) {
            return false;
        }
        if (ignoreCase) {
            return file.getName().toLowerCase().endsWith(suffix.toLowerCase());
        } else {
            return file.getName().endsWith(suffix);
        }
    }
}
