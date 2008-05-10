package net.lunglet.svm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;
import net.lunglet.array4j.matrix.FloatMatrix;
import net.lunglet.array4j.matrix.FloatVector;
import net.lunglet.array4j.matrix.dense.DenseFactory;
import net.lunglet.array4j.matrix.dense.FloatDenseMatrix;
import net.lunglet.array4j.matrix.math.FloatMatrixMath;
import net.lunglet.array4j.matrix.math.MatrixMath;
import net.lunglet.array4j.matrix.packed.FloatPackedMatrix;
import net.lunglet.array4j.matrix.util.FloatMatrixUtils;
import org.junit.Test;

// TODO test with heap and direct data oriented both ways

public final class SvmClassifierTest {
    private static List<Handle> createHandles(final List<? extends FloatVector> data, final int[] labels) {
        List<Handle> handles = new ArrayList<Handle>();
        int i = 0;
        for (final FloatVector x : data) {
            final int j = i++;
            handles.add(new Handle() {
                @Override
                public FloatVector getData() {
                    return x;
                }

                @Override
                public int getIndex() {
                    return j;
                }

                @Override
                public int getLabel() {
                    return labels[j];
                }
            });
        }
        return handles;
    }

    private svm_parameter createSvmParameter() {
        // TODO get these values from somewhere instead of hardcoding them here
        svm_parameter param = new svm_parameter();
        param.svm_type = svm_parameter.C_SVC;
        param.degree = 3;
        param.gamma = 0;
        param.coef0 = 0;
        param.nu = 0.5;
        param.cache_size = 100;
        param.eps = 1e-3;
        param.p = 0.1;
        param.shrinking = 1;
        param.probability = 0;
        param.nr_weight = 0;
        param.weight_label = new int[0];
        param.weight = new double[0];
        return param;
    }

    private svm_problem dataAsSvmProblem(final FloatMatrix data, final int[] labels) {
        svm_problem prob = new svm_problem();
        prob.l = data.columns();
        prob.x = new svm_node[prob.l][];
        prob.y = new double[prob.l];
        for (int i = 0; i < prob.l; i++) {
            prob.x[i] = new svm_node[data.rows()];
            for (int j = 0; j < prob.x[i].length; j++) {
                prob.x[i][j] = new svm_node();
                prob.x[i][j].index = j + 1;
                prob.x[i][j].value = data.get(j, i);
            }
            prob.y[i] = labels[i];
        }
        return prob;
    }

    private FloatDenseMatrix getLinearScores(final FloatDenseMatrix data, final int[] labels, final double cost) {
        if (data.columns() != labels.length) {
            throw new IllegalArgumentException();
        }

        // get number of classes
        Set<Integer> uniqueLabels = new HashSet<Integer>();
        for (int label : labels) {
            uniqueLabels.add(label);
        }
        int classes = uniqueLabels.size();

        // predict
        svm_problem prob = dataAsSvmProblem(data, labels);
        svm_parameter param = createSvmParameter();
        param.kernel_type = svm_parameter.LINEAR;
        param.C = cost;
        assertNull(svm.svm_check_parameter(prob, param));
        svm_model model = svm.svm_train(prob, param);

        // train
        double[] decvalues = new double[classes * (classes - 1) / 2];
        FloatDenseMatrix scores = DenseFactory.floatMatrix(decvalues.length, prob.l);
        for (int i = 0; i < prob.l; i++) {
            svm.svm_predict_values(model, prob.x[i], decvalues);
            for (int j = 0; j < decvalues.length; j++) {
                scores.set(j, i, (float) decvalues[j]);
            }
        }

        // XXX fix sign... might need something more complex here when dealing
        // with arbitrary labels. this assumes labels start at 0.
        MatrixMath.timesEquals(scores, labels[0] == 0 ? 1.0f : -1.0f);

        return scores;
    }

    private FloatDenseMatrix getPrecomputedScores(final FloatMatrix data, final FloatMatrix kernel, final int[] labels,
            final double cost) {
        if (data.columns() != labels.length || !kernel.isSquare() || data.columns() != kernel.rows()) {
            throw new IllegalArgumentException();
        }

        // get number of classes
        Set<Integer> uniqueLabels = new HashSet<Integer>();
        for (int label : labels) {
            uniqueLabels.add(label);
        }
        int classes = uniqueLabels.size();

        // train
        svm_problem prob = precomputedKernelAsSvmProblem(kernel, labels);
        svm_problem dataprob = dataAsSvmProblem(data, labels);
        svm_parameter param = createSvmParameter();
        param.kernel_type = svm_parameter.PRECOMPUTED;
        param.C = cost;
        assertNull(svm.svm_check_parameter(prob, param));
        svm_model model = svm.svm_train(prob, param);
        // fix model so that prediction works
        param.kernel_type = svm_parameter.LINEAR;
        for (int i = 0; i < model.SV.length; i++) {
            model.SV[i] = dataprob.x[(int) model.SV[i][0].value - 1];
        }

        // predict
        double[] decvalues = new double[classes * (classes - 1) / 2];
        FloatDenseMatrix scores = DenseFactory.floatMatrix(decvalues.length, prob.l);
        for (int i = 0; i < prob.l; i++) {
            svm.svm_predict_values(model, dataprob.x[i], decvalues);
            for (int j = 0; j < decvalues.length; j++) {
                scores.set(j, i, (float) decvalues[j]);
            }
        }

        // XXX fix sign... might need something more complex heren when dealing
        // with arbitrary labels. this assumes labels start at 0.
        MatrixMath.timesEquals(scores, labels[0] == 0 ? 1.0f : -1.0f);

        return scores;
    }

    private svm_problem precomputedKernelAsSvmProblem(final FloatMatrix kernel, final int[] labels) {
        svm_problem prob = new svm_problem();
        prob.l = kernel.rows();
        prob.x = new svm_node[prob.l][];
        prob.y = new double[prob.l];
        for (int i = 0; i < prob.l; i++) {
            prob.x[i] = new svm_node[prob.l + 1];
            prob.x[i][0] = new svm_node();
            prob.x[i][0].value = i + 1;
            for (int j = 1; j < prob.x[i].length; j++) {
                prob.x[i][j] = new svm_node();
                prob.x[i][j].index = j;
                prob.x[i][j].value = kernel.get(i, j - 1);
            }
            prob.y[i] = labels[i];
        }
        return prob;
    }

    @Test
    public void test() {
        final double cost = 100.0;
        final Random rng = new Random(1234);
        for (int i = 2; i < 100; i += 10) {
            for (int j = 4; j < 100; j += 10) {
                FloatDenseMatrix data = DenseFactory.floatRowHeap(i, j);
                FloatMatrixUtils.fillRandom(data, rng);
                FloatPackedMatrix kernel = FloatMatrixMath.timesTranspose(data.transpose());
                int[] labels = new int[data.columns()];
                // assume there are at least 2 data vectors and make sure we
                // have at least one vector with each label
                if (rng.nextBoolean()) {
                    labels[0] = 0;
                    labels[1] = 1;
                } else {
                    labels[0] = 1;
                    labels[1] = 0;
                }
                for (int k = 2; k < labels.length; k++) {
                    // TODO increase this number to test with more than 2 classes
                    labels[k] = rng.nextInt(2);
                }

                FloatDenseMatrix linearScores = getLinearScores(data, labels, cost);
                FloatDenseMatrix precomputedScores = getPrecomputedScores(data, kernel, labels, cost);

                // train SVM using linear kernel
                SvmClassifier svm1 = new SvmClassifier(createHandles(data.columnsList(), labels));
                svm1.train(cost);
                FloatDenseMatrix scores1 = svm1.score(data);
                SvmClassifier svm1compact = svm1.compact();
                FloatDenseMatrix scores2 = svm1compact.score(data);

                // train SVM using precomputed kernel
                SvmClassifier svm2 = new SvmClassifier(createHandles(data.columnsList(), labels), kernel);
                svm2.train(cost);
                FloatDenseMatrix scores3 = svm2.score(data);
                SvmClassifier svm2compact = svm2.compact();
                FloatDenseMatrix scores4 = svm2compact.score(data);

                for (FloatDenseMatrix scores : new FloatDenseMatrix[]{scores1, scores2, scores3, scores4}) {
                    assertEquals(linearScores.rows(), precomputedScores.rows());
                    assertEquals(linearScores.columns(), precomputedScores.columns());
                    assertEquals(linearScores.rows(), scores.rows());
                    assertEquals(linearScores.columns(), scores.columns());
                    for (int m = 0; m < linearScores.rows(); m++) {
                        for (int n = 0; n < linearScores.columns(); n++) {
                            float linearScore = linearScores.get(m, n);
                            float precomputedScore = precomputedScores.get(m, n);
                            float score = scores.get(m, n);
                            assertTrue(Math.abs(linearScore - precomputedScore) < 0.2f);
                            assertTrue(Math.abs(linearScore - score) < 0.2f);
                        }
                    }
                }
            }
        }
    }
}
