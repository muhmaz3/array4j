package net.lunglet.io;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class FileUtils {
    public static final FilenameFilter ALL_FILES_FILTER = new FilenameFilter() {
        public boolean accept(final File dir, final String name) {
            return new File(dir, name).isFile();
        }
    };

    public static File createTempDirectory(final String prefix, final String suffix) {
        return createTempDirectory(prefix, suffix, null);
    }

    public static File createTempDirectory(final String prefix, final String suffix, final File directory) {
        try {
            File file = File.createTempFile(prefix, suffix, directory);
            if (!file.delete()) {
                throw new RuntimeException();
            }
            if (!file.mkdirs()) {
                throw new RuntimeException();
            }
            return file;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static File[] listFiles(final File directory, final FilenameFilter filter) {
        return listFiles(directory, filter, false);
    }

    public static File[] listFiles(final File directory, final FilenameFilter filter, final boolean recurse) {
        List<File> files = new ArrayList<File>();
        File[] entries = directory.listFiles();
        // handle the case where directory doesn't exist
        if (entries == null) {
            return new File[0];
        }
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

    public static File[] listFiles(final String name, final FilenameFilter filter) {
        return listFiles(name, filter, false);
    }

    public static File[] listFiles(final String name, final FilenameFilter filter, final boolean recurse) {
        return listFiles(new File(name), filter, recurse);
    }

    private FileUtils() {
    }
}
