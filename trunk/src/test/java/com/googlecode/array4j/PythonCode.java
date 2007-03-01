package com.googlecode.array4j;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public final class PythonCode {
    private PythonCode() {
    }

    public static List<String> run(final String... code) {
        try {
            final ProcessBuilder pb = new ProcessBuilder("python.exe", "-");
            pb.directory(new File("C:\\Python24"));
            pb.redirectErrorStream(true);
            final Process p = pb.start();

            final String lineSeparator = System.getProperty("line.separator");
            final DataOutputStream out = new DataOutputStream(p.getOutputStream());
            for (String line : code) {
                out.writeBytes(line);
                out.writeBytes(lineSeparator);
            }
            out.close();

            final int exitValue = p.waitFor();

            final List<String> output = new ArrayList<String>();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.add(line);
            }

            if (exitValue != 0) {
                throw new RuntimeException("Python code evaluation failed: " + output.toString());
            }

            return output;
        } catch (final IOException e) {
            throw new RuntimeException(e);
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(final String[] args) {
        final String code = "import numpy as N; dt = N.dtype(N.float64); print dt.alignment";
        final List<String> output = run(code);
        for (String line : output) {
            System.out.println(line);
        }
    }
}
