package net.lunglet.util.zip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class ZipUtils {
    public static File[] extractAll(final ZipInputStream zis, final File destinationDirectory) throws IOException {
        ArrayList<File> files = new ArrayList<File>();
        byte[] buf = new byte[131072];
        ZipEntry entry = zis.getNextEntry();
        while (entry != null) {
            if (entry.isDirectory()) {
                files.add(new File(destinationDirectory, entry.getName()));
            } else {
                File directory = new File(destinationDirectory, entry.getName()).getParentFile();
                if (!directory.exists() && !directory.mkdirs()) {
                    throw new RuntimeException();
                }
                File file = new File(destinationDirectory, entry.getName());
                FileOutputStream fos = new FileOutputStream(file);
                int n;
                while ((n = zis.read(buf, 0, buf.length)) > -1) {
                    fos.write(buf, 0, n);
                }
                fos.close();
                files.add(file);
            }
            zis.closeEntry();
            entry = zis.getNextEntry();
        }
        Collections.sort(files);
        return files.toArray(new File[0]);
    }

    private ZipUtils() {
    }
}
