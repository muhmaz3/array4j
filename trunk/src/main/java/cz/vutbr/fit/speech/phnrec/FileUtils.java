package cz.vutbr.fit.speech.phnrec;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class FileUtils {
    public static FilenameFilter ALL_FILES = new FilenameFilter() {
        public boolean accept(final File dir, final String name) {
            return new File(dir, name).isFile();
        }
    };

    public static final class FilenameSuffixFilter implements FilenameFilter {
        private final String suffix;

        private final boolean ignoreCase;

        public FilenameSuffixFilter(final String suffix) {
            this(suffix, false);
        }

        public FilenameSuffixFilter(final String suffix, final boolean ignoreCase) {
            this.suffix = suffix;
            this.ignoreCase = ignoreCase;
        }

        public boolean accept(final File dir, final String name) {
            File file = new File(dir, name);
            if (ignoreCase) {
                return file.isFile() && file.getName().toLowerCase().endsWith(suffix.toLowerCase());
            } else {
                return file.isFile() && file.getName().endsWith(suffix);
            }
        }
    }

    public static final class DirectoryFilter implements FilenameFilter {
        public boolean accept(final File dir, final String name) {
            return new File(dir, name).isDirectory();
        }
    }

    public static File[] listFiles(final File directory, final FilenameFilter filter, final boolean recurse) {
        List<File> files = new ArrayList<File>();
        File[] entries = directory.listFiles();
        for (File entry : entries) {
            if (filter == null || filter.accept(directory, entry.getName())) {
                files.add(entry);
            }

            if (recurse && entry.isDirectory()) {
                files.addAll(Arrays.asList(listFiles(entry, filter, recurse)));
            }
        }
        return files.toArray(new File[0]);
    }
}
