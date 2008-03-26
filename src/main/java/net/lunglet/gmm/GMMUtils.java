package net.lunglet.gmm;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import net.lunglet.array4j.matrix.FloatVector;
import net.lunglet.array4j.matrix.dense.DenseFactory;
import net.lunglet.array4j.matrix.dense.FloatDenseVector;
import net.lunglet.array4j.matrix.util.FloatMatrixUtils;
import org.apache.commons.lang.NotImplementedException;

public final class GMMUtils {
    public static DiagCovGMM createDiagCovGMM(final int mixtures, final int dimension) {
        FloatVector weights = DenseFactory.floatVector(mixtures);
        FloatMatrixUtils.fill(weights, 1.0f);
        ArrayList<FloatVector> means = new ArrayList<FloatVector>();
        ArrayList<FloatVector> vars = new ArrayList<FloatVector>();
        for (int i = 0; i < mixtures; i++) {
            means.add(DenseFactory.floatVector(dimension));
            FloatVector var = DenseFactory.floatVector(dimension);
            FloatMatrixUtils.fill(var, 1.0f);
            vars.add(var);
        }
        return new DiagCovGMM(weights, means.toArray(new FloatVector[0]), vars.toArray(new FloatVector[0]));
    }

    public static FloatVector createSupervector(final DiagCovGMM gmm, final DiagCovGMM ubm) {
        int mixtures = gmm.getMixtureCount();
        int dimension = gmm.getDimension();
        FloatDenseVector sv = DenseFactory.floatVector(mixtures * dimension);
        FloatBuffer data = sv.data();
        FloatVector weights = gmm.getWeights();
        for (int i = 0; i < mixtures; i++) {
            float[] x = gmm.getMean(i).toArray();
            float[] x0 = ubm.getMean(i).toArray();
            float[] variances = ubm.getVariance(i).toArray();
            double sqrtw = Math.sqrt(weights.get(i));
            for (int j = 0; j < x.length; j++) {
                float y = x[j] - x0[j];
                y /= Math.sqrt(variances[j]);
                y *= sqrtw;
                x[j] = y;
            }
            data.put(x);
        }
        return sv;
    }

    public static boolean isGMMParametersFinite(final DiagCovGMM gmm) {
        for (int index = 0; index < gmm.getMixtureCount(); index++) {
            if (!FloatMatrixUtils.isAllFinite(gmm.getMean(index))) {
                return false;
            }
            if (!FloatMatrixUtils.isAllFinite(gmm.getVariance(index))) {
                return false;
            }
        }
        if (!FloatMatrixUtils.isAllFinite(gmm.getWeights())) {
            return false;
        }
        return true;
    }

    /**
     * Replace weak components (components not supported by enough data) by
     * splitting the strongest (heaviest) components.
     */
    public static DiagCovGMM replaceWeak(final DiagCovGMM gmm, final GMMMAPStats stats, final float thresh) {
        throw new NotImplementedException();
    }

    /**
     * Split all the components in the GMM.
     */
    public static DiagCovGMM splitAll(final DiagCovGMM gmm) {
        List<FloatVector> newMeans = new ArrayList<FloatVector>();
        List<FloatVector> newVars = new ArrayList<FloatVector>();
        for (int i = 0; i < gmm.getMixtureCount(); i++) {
            FloatVector mean = gmm.getMean(i);
            FloatVector var = gmm.getVariance(i);
            float[] varArr = var.toArray();
            float[] newMean0 = mean.toArray();
            float[] newMean1 = mean.toArray();
            for (int j = 0; j < varArr.length; j++) {
                double stddev = Math.sqrt(varArr[j]);
                newMean0[j] -= stddev;
                newMean1[j] += stddev;
            }
            newMeans.add(DenseFactory.floatVector(newMean0));
            newMeans.add(DenseFactory.floatVector(newMean1));
            // use same variance for both new components
            newVars.add(var);
            newVars.add(var);
        }
        FloatVector oldWeights = gmm.getWeights();
        FloatVector newWeights = DenseFactory.floatVector(2 * oldWeights.length());
        for (int i = 0; i < oldWeights.length(); i++) {
            float w = oldWeights.get(i) / 2.0f;
            newWeights.set(2 * i, w);
            newWeights.set(2 * i + 1, w);
        }
        return new DiagCovGMM(newWeights, newMeans, newVars);
    }

    /**
     * Split the heaviest component in the GMM into two components.
     */
    public static DiagCovGMM splitHeaviest(final DiagCovGMM gmm) {
        // TODO move this code into an argmax function
        FloatVector oldWeights = gmm.getWeights();
        float maxWeight = oldWeights.get(0);
        int maxIndex = 0;
        for (int i = 1; i < oldWeights.length(); i++) {
            if (oldWeights.get(i) > maxWeight) {
                maxWeight = oldWeights.get(i);
                maxIndex = i;
            }
        }

        List<FloatVector> newMeans = new ArrayList<FloatVector>();
        List<FloatVector> newVars = new ArrayList<FloatVector>();
        List<Float> newWeightsList = new ArrayList<Float>();
        for (int i = 0; i < gmm.getMixtureCount(); i++) {
            FloatVector mean = gmm.getMean(i);
            FloatVector var = gmm.getVariance(i);
            float weight = oldWeights.get(i);
            if (i == maxIndex) {
                float[] varArr = var.toArray();
                float[] newMean0 = mean.toArray();
                float[] newMean1 = mean.toArray();
                for (int j = 0; j < varArr.length; j++) {
                    double stddev = Math.sqrt(varArr[j]);
                    newMean0[j] -= stddev;
                    newMean1[j] += stddev;
                }
                newMeans.add(DenseFactory.floatVector(newMean0));
                newMeans.add(DenseFactory.floatVector(newMean1));
                // use same variance for both new components
                newVars.add(var);
                newVars.add(var);
                newWeightsList.add(weight / 2);
                newWeightsList.add(weight / 2);
            } else {
                newMeans.add(mean);
                newVars.add(var);
                newWeightsList.add(weight);
            }
        }
        FloatVector newWeights = DenseFactory.floatVector(newWeightsList);
        return new DiagCovGMM(newWeights, newMeans, newVars);
    }

    private GMMUtils() {
    }
}
