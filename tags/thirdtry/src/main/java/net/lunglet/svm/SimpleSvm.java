package net.lunglet.svm;

import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.FloatVector;
import com.googlecode.array4j.dense.FloatDenseVector;

// TODO make data an array of T or an Iterable<FloatVector> or something
// TODO support arbitrary labels

public final class SimpleSvm {
    private final FloatMatrix<?, ?> data;

    private final FloatMatrix<?, ?> gram;

    private final SvmProblem problem;

    private SvmModel model;

    public SimpleSvm(final FloatMatrix<?, ?> data, final int[] labels) {
        this(data, null, labels);
    }

    public SimpleSvm(final FloatMatrix<?, ?> data, final FloatMatrix<?, ?> gram, final int[] labels) {
        if (data.columns() != labels.length) {
            throw new IllegalArgumentException();
        }
        if (gram != null && (data.columns() != gram.rows() || !gram.isSquare())) {
            throw new IllegalArgumentException();
        }
        this.data = data;
        this.gram = gram;
        problem = new SvmProblem();
        problem.l = data.columns();
        problem.y = new double[labels.length];
        problem.x = new SvmNode[problem.l];
        for (int i = 0; i < labels.length; i++) {
            problem.y[i] = labels[i];
            if (gram != null) {
                problem.x[i] = new SvmNode(i, gram.column(i));
            } else {
                problem.x[i] = new SvmNode(i, data.column(i));
            }
        }
    }

    public void train(final double cost) {
        SvmParameter param = createDefaultSvmParameter();
        param.svm_type = SvmParameter.C_SVC;
        param.C = cost;
        if (gram != null) {
            param.kernel_type = SvmParameter.PRECOMPUTED;
        } else {
            param.kernel_type = SvmParameter.LINEAR;
        }
        Svm.svm_check_parameter(problem, param);
        model = new Svm().svm_train(problem, param);
        if (param.kernel_type == SvmParameter.PRECOMPUTED) {
            param.kernel_type = SvmParameter.LINEAR;
            // obtain actual support vectors
            for (int i = 0; i < model.SV.length; i++) {
                model.SV[i] =  new SvmNode(i, data.column(model.SV[i].index));
            }
        }
    }

    public FloatDenseVector score(final FloatMatrix<?, ?> testData) {
        if (model == null) {
            throw new IllegalStateException();
        }
        double[] decvalues = new double[model.nr_class * (model.nr_class - 1) / 2];
        FloatDenseVector scores = new FloatDenseVector(testData.columns());
        int i = 0;
        for (FloatVector<?> x : testData.columnsIterator()) {
            Svm.svm_predict_values(model, x, decvalues);
            scores.set(i++, (float) decvalues[0]);
        }
        // make sign consistent
        scores.timesEquals(problem.y[0] > 0 ? 1.0f : -1.0f);
        return scores;
    }

    static SvmParameter createDefaultSvmParameter() {
        SvmParameter param = new SvmParameter();
        param.svm_type = SvmParameter.C_SVC;
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
}
