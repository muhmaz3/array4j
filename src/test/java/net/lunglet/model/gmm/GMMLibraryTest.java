package net.lunglet.model.gmm;

import java.nio.FloatBuffer;
import net.lunglet.array4j.Storage;
import net.lunglet.model.gmm.GMMLibrary.GMM;
import net.lunglet.model.gmm.GMMLibrary.GMMStats;
import net.lunglet.util.BufferUtils;
import org.junit.Test;

// TODO implement beam width

// XXX niko used 5 iterations for 2004 UBM

public final class GMMLibraryTest {
    @Test
    public void test() {
        GMM gmm = new GMM();
        gmm.dimension = 39;
        gmm.mixtures = 2048;
        int k = gmm.dimension * gmm.mixtures;

        FloatBuffer means = BufferUtils.createFloatBuffer(k, Storage.DIRECT);
        for (int i = 0; i < gmm.mixtures; i++) {
            for (int j = 0; j < gmm.dimension; j++) {
                means.put(0.0f);
            }
        }

        FloatBuffer precisions = BufferUtils.createFloatBuffer(k, Storage.DIRECT);
        for (int i = 0; i < gmm.mixtures; i++) {
            for (int j = 0; j < gmm.dimension; j++) {
                precisions.put(1.0f);
            }
        }

        FloatBuffer logweights = BufferUtils.createFloatBuffer(gmm.mixtures, Storage.DIRECT);
        for (int i = 0; i < gmm.mixtures; i++) {
            logweights.put((float) Math.log(1.0f / gmm.mixtures));
        }
        FloatBuffer lloffsets = BufferUtils.createFloatBuffer(gmm.mixtures, Storage.DIRECT);
        FloatBuffer work = BufferUtils.createFloatBuffer(gmm.mixtures, Storage.DIRECT);
        GMMLibrary.INSTANCE.array4j_gmm_init(gmm, means, precisions, logweights, lloffsets, work);

        if (false) {
            GMMLibrary.INSTANCE.array4j_gmm_print(gmm);
        }

        GMMStats stats = new GMMStats();
        FloatBuffer n = BufferUtils.createFloatBuffer(gmm.mixtures, Storage.DIRECT);
        FloatBuffer ex = BufferUtils.createFloatBuffer(k, Storage.DIRECT);
        FloatBuffer exx = BufferUtils.createFloatBuffer(k, Storage.DIRECT);
        GMMLibrary.INSTANCE.array4j_gmm_stats_init(stats, n, ex, exx);

        int count = 10000;
        FloatBuffer data = BufferUtils.createFloatBuffer(count * gmm.dimension, Storage.DIRECT);
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < gmm.dimension; j++) {
                data.put(2.0f);
            }
        }

        for (int i = 0; i < 1; i++) {
            long start = System.currentTimeMillis();
            GMMLibrary.INSTANCE.array4j_gmm_map_e(gmm, stats, count, data);
            System.out.println(System.currentTimeMillis() - start);

            if (false) {
//                System.out.println(work.get(0));
                System.out.println(n.get(0));
                System.out.println(ex.get(0));
                System.out.println(exx.get(0));
            }
        }
    }
}
