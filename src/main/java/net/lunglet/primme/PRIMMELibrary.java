package net.lunglet.primme;

import com.sun.jna.Callback;
import com.sun.jna.DefaultTypeMapper;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.TypeMapper;
import java.nio.DoubleBuffer;
import java.util.HashMap;
import java.util.Map;

public interface PRIMMELibrary extends Library {
    final class Loader {
        static PRIMMELibrary loadLibrary() {
            Map<String, TypeMapper> options = new HashMap<String, TypeMapper>();
            DefaultTypeMapper mapper = new DefaultTypeMapper();
            options.put(Library.OPTION_TYPE_MAPPER, mapper);
            return (PRIMMELibrary) Native.loadLibrary("primme", PRIMMELibrary.class, options);
        }

        private Loader() {
        }
    }

    interface MatrixMatvecCallback extends Callback {
    }

    PRIMMELibrary INSTANCE = Loader.loadLibrary();

    int dprimme(DoubleBuffer evals, DoubleBuffer evecs, DoubleBuffer resNorms, PRIMMEParams params);

    void primme_Free(PRIMMEParams params);

    void primme_initialize(PRIMMEParams params);

    int primme_set_method(PresetMethod method, PRIMMEParams params);
}
