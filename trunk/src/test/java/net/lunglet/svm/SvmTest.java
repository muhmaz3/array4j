package net.lunglet.svm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.FloatMatrixMath;
import com.googlecode.array4j.FloatMatrixUtils;
import com.googlecode.array4j.Orientation;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.dense.FloatDenseMatrix;
import com.googlecode.array4j.dense.FloatDenseVector;
import com.googlecode.array4j.packed.FloatPackedMatrix;
import java.util.Random;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;
import org.junit.Test;

// TODO test with heap and direct data oriented both ways

public final class SvmTest {
    private svm_parameter createSvmParameter() {
        // TODO make sure these values match those in SimpleSvm#createDefaultSvmParameter()
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

    private svm_problem dataAsSvmProblem(final FloatMatrix<?, ?> data, final int[] labels) {
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

    private FloatDenseVector getLinearScores(final FloatDenseMatrix data, final int[] labels, final double cost) {
        if (data.columns() != labels.length) {
            throw new IllegalArgumentException();
        }

        // predict
        svm_problem prob = dataAsSvmProblem(data, labels);
        svm_parameter param = createSvmParameter();
        param.kernel_type = svm_parameter.LINEAR;
        param.C = cost;
        assertNull(svm.svm_check_parameter(prob, param));
        svm_model model = svm.svm_train(prob, param);

        // train
        FloatDenseVector scores = new FloatDenseVector(prob.l);
        double[] decvalues = new double[1];
        for (int i = 0; i < prob.l; i++) {
            svm.svm_predict_values(model, prob.x[i], decvalues);
            scores.set(i, (float) decvalues[0]);
        }
        scores.timesEquals(labels[0] > 0 ? 1.0f : -1.0f);

        return scores;
    }

    private FloatDenseVector getPrecomputedScores(final FloatMatrix<?, ?> data, final FloatMatrix<?, ?> gram,
            final int[] labels, final double cost) {
        if (data.columns() != labels.length || !gram.isSquare() || data.columns() != gram.rows()) {
            throw new IllegalArgumentException();
        }

        // train
        svm_problem prob = gramAsSvmProblem(gram, labels);
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
        FloatDenseVector scores = new FloatDenseVector(prob.l);
        double[] decvalues = new double[1];
        for (int i = 0; i < prob.l; i++) {
            svm.svm_predict_values(model, dataprob.x[i], decvalues);
            scores.set(i, (float) decvalues[0]);
        }
        // make sign consistent
        scores.timesEquals(labels[0] > 0 ? 1.0f : -1.0f);

        return scores;
    }

    private svm_problem gramAsSvmProblem(final FloatMatrix<?, ?> gram, final int[] labels) {
        svm_problem prob = new svm_problem();
        prob.l = gram.rows();
        prob.x = new svm_node[prob.l][];
        prob.y = new double[prob.l];
        for (int i = 0; i < prob.l; i++) {
            prob.x[i] = new svm_node[prob.l + 1];
            prob.x[i][0] = new svm_node();
            prob.x[i][0].value = i + 1;
            for (int j = 1; j < prob.x[i].length; j++) {
                prob.x[i][j] = new svm_node();
                prob.x[i][j].index = j;
                prob.x[i][j].value = gram.get(i, j - 1);
            }
            prob.y[i] = labels[i];
        }
        return prob;
    }

    @Test
    public void test() {
        final double cost = 100.0;
        final Random rng = new Random(1234);
        for (int i = 2; i < 100; i++) {
            for (int j = 4; j < 100; j++) {
                FloatDenseMatrix data = new FloatDenseMatrix(i, j, Orientation.ROW, Storage.HEAP);
                FloatMatrixUtils.fillRandom(data, rng);
                FloatPackedMatrix gram = FloatMatrixMath.timesTranspose(data.transpose());

                int[] labels = new int[data.columns()];
                // assume there are at least 2 data vectors and make sure we
                // have at least one vector with each label
                if (rng.nextBoolean()) {
                    labels[0] = 1;
                    labels[1] = -1;
                } else {
                    labels[0] = -1;
                    labels[1] = 1;
                }
                for (int k = 2; k < labels.length; k++) {
                    labels[k] = rng.nextBoolean() ? 1 : -1;
                }

                FloatDenseVector linearScores = getLinearScores(data, labels, cost);
//                FloatDenseVector precomputedScores = getPrecomputedScores(data, gram, labels, cost);

                // train SVM using linear kernel
                SimpleSvm svm1 = new SimpleSvm(data, labels);
                svm1.train(cost);
                FloatDenseVector scores1 = svm1.score(data);
//                svm1.compact();
//                FloatDenseVector scores2 = svm1.score(data);

                // train SVM using precomputed kernel
//                SimpleSvm svm2 = new SimpleSvm(data, gram, labels);
//                svm2.train(cost);
//                FloatDenseVector scores3 = svm2.score(data);
//                svm2.compact();
//                FloatDenseVector scores4 = svm2.score(data);

//                for (FloatDenseVector scores : new FloatDenseVector[]{scores1, scores2, scores3, scores4}) {
                for (FloatDenseVector scores : new FloatDenseVector[]{scores1}) {
//                    assertEquals(linearScores.length(), precomputedScores.length());
                    assertEquals(linearScores.length(), scores.length());
                    for (int k = 0; i < linearScores.length(); i++) {
                        assertEquals((int) Math.signum(linearScores.get(k)), (int) Math.signum(labels[k]));
//                        assertEquals(linearScores.get(k), precomputedScores.get(k), 1e-2);
                        assertEquals(linearScores.get(k), scores.get(k), 1e-2);
                    }
                }
            }
        }
    }
}
