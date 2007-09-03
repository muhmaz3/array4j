package net.lunglet.io;

import java.io.File;
import java.io.FilenameFilter;

public final class FilenameSuffixFilter implements FilenameFilter {
    private final boolean ignoreCase;

    private final String suffix;

    public FilenameSuffixFilter(final String suffix) {
        this(suffix, false);
    }

    public FilenameSuffixFilter(final String suffix, final boolean ignoreCase) {
        this.suffix = suffix;
        this.ignoreCase = ignoreCase;
    }

    public boolean accept(final File dir, final String name) {
        File file = new File(dir, name);
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
