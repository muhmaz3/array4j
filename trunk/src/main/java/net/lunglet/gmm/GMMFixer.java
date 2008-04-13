package net.lunglet.gmm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.lunglet.array4j.matrix.FloatVector;
import net.lunglet.util.ArrayMath;
import net.lunglet.util.AssertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GMMFixer {
    public static class GMMFixerStats extends HashMap<Integer, GMMMAPStats> {
        private static final long serialVersionUID = 1L;
    }

    public static void addStats(final GMMFixerStats globalStats, final GMMFixerStats stats) {
        for (Map.Entry<Integer, GMMMAPStats> entry : globalStats.entrySet()) {
            int index = entry.getKey();
            if (!stats.containsKey(index)) {
                continue;
            }
            entry.getValue().add(stats.get(index));
        }
    }

    private final DiagCovGMM gmm;

    private final Logger logger = LoggerFactory.getLogger(GMMFixer.class);

    private final Map<Integer, DiagCovGMM> newMixtures;

    private final Map<Integer, Float> oldWeights;

    private final List<FloatVector> unchangedMeans;

    private final List<FloatVector> unchangedVariances;

    private final List<Float> unchangedWeights;

    public GMMFixer(final DiagCovGMM gmm, final float[] n, final int weakCount) {
        if (weakCount == 0) {
            throw new IllegalArgumentException("Nothing to fix");
        }
        if (weakCount >= gmm.getMixtureCount()) {
            throw new IllegalArgumentException("Too many weak components");
        }
        this.gmm = gmm;
        this.unchangedMeans = new ArrayList<FloatVector>();
        this.unchangedVariances = new ArrayList<FloatVector>();
        this.unchangedWeights = new ArrayList<Float>();

        // extract mixtures that aren't going to change
        int[] indexes = ArrayMath.argmax(n);
        FloatVector gmmWeights = gmm.getWeights();
        for (int i = weakCount; i < indexes.length - weakCount; i++) {
            int index = indexes[i];
            float weight = gmmWeights.get(index);
            logger.debug("Not splitting mixture {} with count {} and weight {}", new Object[]{index, n[index], weight});
            unchangedMeans.add(gmm.getMean(index));
            unchangedVariances.add(gmm.getVariance(index));
            unchangedWeights.add(weight);
        }

        if (logger.isDebugEnabled()) {
            for (int i = indexes.length - 1; i >= indexes.length - weakCount; i--) {
                int index = indexes[i];
                float weight = gmmWeights.get(index);
                logger.debug("Removing mixture {} with count {} and weight {}", new Object[]{index, n[index], weight});
            }
        }

        // create new split mixtures and a GMM to use to decide which split
        // mixture to update for each data element
        this.oldWeights = new HashMap<Integer, Float>();
        this.newMixtures = new HashMap<Integer, DiagCovGMM>();
        for (int i = 0; i < weakCount; i++) {
            int index = indexes[i];
            float weight = gmmWeights.get(index);
            logger.debug("Splitting mixture {} with count {} and weight {}", new Object[]{index, n[index], weight});
            FloatVector mean = gmm.getMean(index);
            FloatVector variance = gmm.getVariance(index);
            oldWeights.put(index, weight);
            DiagCovGMM newMixture = GMMUtils.splitAll(new DiagCovGMM(mean, variance));
            newMixtures.put(index, newMixture);
        }
    }

    public GMMFixer(final DiagCovGMM gmm, final GMMMAPStats stats, final double nthresh) {
        this(gmm, stats.getN(), GMMUtils.countWeak(gmm, stats, nthresh));
    }

    public GMMFixerStats add(final Iterable<? extends FloatVector> data) {
        GMMMAPStats globalStats = new GMMMAPStats(gmm);
        List<int[]> indexes = globalStats.add(data, 1);
        Iterator<int[]> indexesIter = indexes.iterator();
        GMMFixerStats stats = createStats();
        for (FloatVector x : data) {
            int index = indexesIter.next()[0];
            // skip data if not best matched to one of the splitting mixtures
            if (!newMixtures.containsKey(index)) {
                continue;
            }
            // add data to stats for new mixture
            stats.get(index).add(x);
        }
        return stats;
    }

    public GMMFixerStats createStats() {
        GMMFixerStats globalStats = new GMMFixerStats();
        for (Map.Entry<Integer, DiagCovGMM> entry : newMixtures.entrySet()) {
            globalStats.put(entry.getKey(), new GMMMAPStats(entry.getValue()));
        }
        return globalStats;
    }

    public void doEM(final Map<Integer, GMMMAPStats> stats) {
        for (Map.Entry<Integer, DiagCovGMM> entry : newMixtures.entrySet()) {
            int index = entry.getKey();
            GMMMAPStats mixtureStats = stats.get(index);
            logger.debug("Log likelihood for split mixture {} = {}", index, mixtureStats.getTotalLogLh());
            entry.getValue().doEM(mixtureStats);
        }
    }

    public DiagCovGMM done() {
        List<Float> weights = new ArrayList<Float>(unchangedWeights);
        List<FloatVector> means = new ArrayList<FloatVector>(unchangedMeans);
        List<FloatVector> variances = new ArrayList<FloatVector>(unchangedVariances);
        for (Map.Entry<Integer, DiagCovGMM> entry : newMixtures.entrySet()) {
            int index = entry.getKey();
            float oldWeight = oldWeights.get(index);
            DiagCovGMM newMixture = entry.getValue();
            for (int i = 0; i < newMixture.getMixtureCount(); i++) {
                means.add(newMixture.getMean(i));
                variances.add(newMixture.getVariance(i));
                weights.add(oldWeight * newMixture.getWeights().get(i));
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Weights for split mixture {} are {}, combined with old weight {}", new Object[]{index,
                        Arrays.toString(newMixture.getWeights().toArray()), oldWeight});
            }
        }
        DiagCovGMM fixedGMM = new DiagCovGMM(weights, means, variances);
        AssertUtils.assertEquals(gmm.getMixtureCount(), fixedGMM.getMixtureCount());
        return fixedGMM;
    }
}
