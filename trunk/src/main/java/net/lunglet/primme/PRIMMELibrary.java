package net.lunglet.primme;

import com.sun.jna.Callback;
import com.sun.jna.DefaultTypeMapper;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ToNativeContext;
import com.sun.jna.ToNativeConverter;
import com.sun.jna.TypeMapper;
import com.sun.jna.ptr.IntByReference;
import java.nio.DoubleBuffer;
import java.util.HashMap;
import java.util.Map;

public interface PRIMMELibrary extends Library {
    final class Loader {
        static PRIMMELibrary loadLibrary() {
            Map<String, TypeMapper> options = new HashMap<String, TypeMapper>();
            DefaultTypeMapper mapper = new DefaultTypeMapper();
            mapper.addToNativeConverter(PresetMethod.class, new ToNativeConverter() {
                public Class<PresetMethod> nativeType() {
                    return PresetMethod.class;
                }

                public Object toNative(final Object obj, final ToNativeContext context) {
                    return Integer.valueOf(((PresetMethod) obj).ordinal());
                }
            });
            options.put(Library.OPTION_TYPE_MAPPER, mapper);
            return (PRIMMELibrary) Native.loadLibrary("primme", PRIMMELibrary.class, options);
        }

        private Loader() {
        }
    }

    interface MatrixMatvecCallback extends Callback {
        void callback(Pointer x, Pointer y, IntByReference blockSize, PRIMMEParams params);
    }

    PRIMMELibrary INSTANCE = Loader.loadLibrary();

    int dprimme(DoubleBuffer evals, DoubleBuffer evecs, DoubleBuffer resNorms, PRIMMEParams params);

    void primme_Free(PRIMMEParams params);

    void primme_initialize(PRIMMEParams params);

    int primme_set_method(PresetMethod method, PRIMMEParams params);

    void primme_display_params(PRIMMEParams params);
}
