package net.lunglet.model.gmm;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.nio.FloatBuffer;

public interface GMMLibrary extends Library {
    GMMLibrary INSTANCE = (GMMLibrary) Native.loadLibrary("array4j", GMMLibrary.class);

    public static class GMM extends Structure implements Structure.ByReference {
        public int dimension;

        public int mixtures;

        public Pointer means;

        public Pointer precisions;

        public Pointer logweights;

        public Pointer lloffsets;

        public Pointer work;
    }

    public static class GMMStats extends Structure implements Structure.ByReference {
        double t;

        public Pointer n;

        public Pointer ex;

        public Pointer exx;
    }

    void array4j_gmm_print(GMM gmm);

    void array4j_gmm_init(GMM gmm, FloatBuffer means, FloatBuffer precisions, FloatBuffer logweights,
            FloatBuffer lloffsets, FloatBuffer work);

    void array4j_gmm_stats_init(GMMStats stats, FloatBuffer n, FloatBuffer ex, FloatBuffer exx);

    void array4j_gmm_stats_merge(GMMStats stats1, GMMStats stats2);

    void array4j_gmm_map_e(GMM gmm, GMMStats stats, int n, FloatBuffer data);

    void array4j_gmm_map_m(GMM gmm, GMMStats stats);

    void array4j_gmm_posteriors(GMM gmm, FloatBuffer x);
}
