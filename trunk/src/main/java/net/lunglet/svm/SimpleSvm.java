package net.lunglet.svm;

import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.FloatVector;
import com.googlecode.array4j.dense.FloatDenseMatrix;
import com.googlecode.array4j.dense.FloatDenseVector;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// TODO rename to SvmClassifier

// TODO handle more than 2 classes everywhere

// TODO support model compaction by data being pushed to the model instead of pulling data

public final class SimpleSvm implements Serializable {
    // TODO make a SvmParameterBuilder so anything can be configured
    static SvmParameter createDefaultSvmParameter() {
        SvmParameter param = new SvmParameter();
        param.svm_type = SvmParameter.C_SVC;
        param.degree = 3;
        param.gamma = 0;
        param.coef0 = 0;
        param.nu = 0.5;
        // TODO tune cache size depending on whether a precomputed kernel is
        // being used or not
        param.cache_size = 32;
        param.eps = 1e-3;
        param.p = 0.1;
        param.shrinking = 1;
        param.probability = 0;
        param.nr_weight = 0;
        param.weight_label = new int[0];
        param.weight = new double[0];
        return param;
    }

    private static PrecomputedKernel createPrecomputedKernel(final List<Handle> data, final FloatMatrix<?, ?> kernel) {
        if (kernel == null) {
            throw new NullPointerException();
        }
        if (data.size() != kernel.rows() || !kernel.isSquare()) {
            throw new IllegalArgumentException();
        }
        return new PrecomputedKernel() {
            public float get(final int i, final int j) {
                return kernel.get(i, j);
            }
        };
    }

    private final transient List<Handle> data;

    private final transient PrecomputedKernel kernel;

    private SvmModel model;

    private final SvmProblem problem;

    public SimpleSvm(final List<Handle> data) {
        this(data, (PrecomputedKernel) null);
    }

    public SimpleSvm(final List<Handle> data, final FloatMatrix<?, ?> kernel) {
        this(data, createPrecomputedKernel(data, kernel));
    }

    public SimpleSvm(final List<Handle> data, final PrecomputedKernel kernel) {
        this.data = new ArrayList<Handle>(data);
        this.kernel = kernel;
        problem = new SvmProblem();
        problem.l = data.size();
        problem.y = new double[data.size()];
        problem.x = new SvmNode[problem.l];
        // XXX here indexes start from 0
        for (int i = 0; i < data.size(); i++) {
            problem.y[i] = data.get(i).getLabel();
            if (kernel != null) {
                problem.x[i] = new SvmNode(i);
            } else {
                problem.x[i] = new SvmNode(i, data.get(i));
            }
        }
        problem.kernel = kernel;
    }

    /**
     * Compact model so that it consists of a single support vector per class.
     */
    public void compact() {
        if (model == null) {
            throw new IllegalStateException();
        }
        if (model.nr_class != 2) {
            throw new UnsupportedOperationException();
        }
        FloatDenseVector sv = new FloatDenseVector(model.SV[0].getValue().length());
        for (int i = 0, k = 0; i < model.nr_class; i++) {
            for (int j = 0; j < model.nSV[i]; j++, k++) {
                final float alpha = (float) model.sv_coef[0][k];
                FloatVector<?> modelSV = model.SV[k].getValue();
                for (int m = 0; m < sv.length(); m++) {
                    sv.set(m, sv.get(m) + alpha * modelSV.get(m));
                }
            }
        }
        model.SV = new SvmNode[]{new SvmNode(0, sv)};
        model.sv_coef = new double[][]{{1.0, 0.0}};
        model.nSV = new int[]{1, 0};
        model.l = 1;
    }

    public SvmNode[] getSvmNodes() {
        return model.SV;
    }

    public float getRho() {
        if (model == null) {
            throw new IllegalStateException();
        }
        if (model.rho.length != 1) {
            throw new UnsupportedOperationException();
        }
        return (float) model.rho[0];
    }

    public FloatVector<?> getSupportVector() {
        if (model == null) {
            throw new IllegalStateException();
        }
        if (model.SV.length != 1) {
            throw new UnsupportedOperationException();
        }
        return model.SV[0].getValue();
    }

    public FloatDenseMatrix score(final FloatMatrix<?, ?> testData) {
        List<Handle> handles = new ArrayList<Handle>();
        for (final FloatVector<?> x : testData.columnsIterator()) {
            handles.add(new Handle() {
                @Override
                public FloatVector<?> getData() {
                    return x;
                }

                @Override
                public int getLabel() {
                    throw new UnsupportedOperationException();
                }
            });
        }
        return score(handles);
    }

    public FloatDenseMatrix score(final List<Handle> testData) {
        if (model == null) {
            throw new IllegalStateException();
        }
        int n = model.nr_class * (model.nr_class - 1) / 2;
        FloatDenseMatrix scores = new FloatDenseMatrix(n, testData.size());
        double[] decvalues = new double[n];
        for (int i = 0; i < testData.size(); i++) {
            Handle handle = testData.get(i);
            Svm.svm_predict_values(model, handle.getData(), decvalues);
            scores.setColumn(i, FloatDenseVector.valueOf(decvalues));
        }
        return scores;
    }

    public void train(final double cost) {
        SvmParameter param = createDefaultSvmParameter();
        param.svm_type = SvmParameter.C_SVC;
        param.C = cost;
        if (kernel != null) {
            param.kernel_type = SvmParameter.PRECOMPUTED;
        } else {
            param.kernel_type = SvmParameter.LINEAR;
        }
        Svm.svm_check_parameter(problem, param);
        model = new Svm().svm_train(problem, param);

        // make sign consistent regardless of data/label order
        double minLabel = problem.y[0];
        for (int i = 1; i < problem.y.length; i++) {
            minLabel = Math.min(minLabel, problem.y[i]);
        }
        if (problem.y[0] != minLabel) {
            for (int i = 0; i < model.sv_coef.length; i++) {
                for (int j = 0; j < model.sv_coef[i].length; j++) {
                    model.sv_coef[i][j] *= -1.0;
                }
            }
            for (int i = 0; i < model.rho.length; i++) {
                model.rho[i] *= -1.0;
            }
        }

        // fix support vectors after using precomputed kernel
        if (param.kernel_type == SvmParameter.PRECOMPUTED) {
            param.kernel_type = SvmParameter.LINEAR;
            for (int i = 0; i < model.SV.length; i++) {
                model.SV[i] = new SvmNode(i, data.get(model.SV[i].getIndex()));
            }
        }
        if (model.SV.length == 0) {
            throw new RuntimeException("SVM training failed");
        }
    }
}
