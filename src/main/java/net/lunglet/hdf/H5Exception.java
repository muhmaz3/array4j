package net.lunglet.hdf;

import com.sun.jna.Pointer;
import net.lunglet.hdf.H5Library.H5E_error_t;
import net.lunglet.hdf.H5Library.H5E_walk_t;

public class H5Exception extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public static String getErrorMessage() {
        final String[] message = new String[1];
        H5E_walk_t callback = new H5E_walk_t() {
            @Override
            public int callback(final int n, final H5E_error_t desc, final Pointer data) {
                if (message[0] != null) {
                    return 0;
                }
                StringBuilder builder = new StringBuilder();
                if (false) {
                    builder.append(String.format("#%03d", n));
                    builder.append(": ");
                }
                builder.append(desc.func_name);
                builder.append("() [");
                builder.append(desc.file_name);
                builder.append(":");
                builder.append(desc.line);
                builder.append("]: ");
                builder.append(desc.desc);
                builder.append(", ");
                String major = H5Library.INSTANCE.H5Eget_major(desc.maj_num);
                builder.append(String.format("major(%02d): %s", desc.maj_num, major));
                builder.append(", ");
                String minor = H5Library.INSTANCE.H5Eget_minor(desc.min_num);
                builder.append(String.format("minor(%02d): %s", desc.min_num, minor));
                message[0] = builder.toString();
                return 0;
            }
        };
        H5Library.INSTANCE.H5Ewalk(H5Library.H5E_WALK_UPWARD, callback, null);
        return message[0];
    }

    public H5Exception() {
        super();
    }

    public H5Exception(final String message) {
        super(message);
    }

    public H5Exception(final String message, final boolean includeError) {
        super(message + (includeError ? " (" + getErrorMessage() + ")" : ""));
    }
}
