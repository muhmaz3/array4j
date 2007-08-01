package net.lunglet.svm;

import java.util.Arrays;

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

    public SimpleSvm(final FloatMatrix<?, ?> data, final FloatMatrix<?, ?> gram, final int[] labels) {
        this.data = data;
        this.gram = gram;
        problem = new SvmProblem();
        problem.l = data.columns();
        problem.y = new double[labels.length];
        problem.x = new FloatVector<?>[problem.l];
        for (int i = 0; i < labels.length; i++) {
            problem.y[i] = labels[i];
            problem.x[i] = data.column(i);
        }
    }

    public void train(final double cost) {
        SvmParameter param = new SvmParameter();
        param.C = cost;
        param.svm_type = SvmParameter.C_SVC;
        param.kernel_type = SvmParameter.PRECOMPUTED;
        Svm.svm_check_parameter(problem, param);
        model = Svm.svm_train(problem, param);
    }

    public FloatDenseVector score(final FloatMatrix<?, ?> testData) {
        if (model == null) {
            throw new IllegalStateException();
        }
        double[] decvalues = new double[model.nr_class * (model.nr_class - 1) / 2];
        for (FloatVector<?> x : testData.columnsIterator()) {
            Svm.svm_predict_values(model, x, decvalues);
            System.out.println(Arrays.toString(decvalues));
        }
        return null;
    }
}
