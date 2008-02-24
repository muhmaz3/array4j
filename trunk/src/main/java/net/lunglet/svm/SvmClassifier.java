package net.lunglet.svm;

import com.googlecode.array4j.matrix.FloatMatrix;
import com.googlecode.array4j.matrix.FloatVector;
import com.googlecode.array4j.matrix.dense.FloatDenseMatrix;
import com.googlecode.array4j.matrix.dense.FloatDenseVector;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// TODO handle more than 2 classes everywhere (major benefit: kernel cache is probably reused)

// TODO factor out compact-only methods into their own class, an instance of which is returned by compact

public final class SvmClassifier implements Serializable {
    private static final long serialVersionUID = 1L;

    // TODO make a SvmParameterBuilder so anything can be configured
    static SvmParameter createDefaultSvmParameter() {
        SvmParameter param = new SvmParameter();
        param.svm_type = SvmParameter.C_SVC;
        param.degree = 3;
        param.gamma = 0;
        param.coef0 = 0;
        param.nu = 0.5;
        // TODO do some benchmarks with different cache sizes
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

    private static PrecomputedKernel createPrecomputedKernel(final List<Handle> data, final FloatMatrix kernel) {
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

    public SvmClassifier(final List<Handle> data) {
        this(data, (PrecomputedKernel) null);
    }

    public SvmClassifier(final List<Handle> data, final FloatMatrix kernel) {
        this(data, createPrecomputedKernel(data, kernel));
    }

    public SvmClassifier(final List<Handle> data, final PrecomputedKernel kernel) {
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

    SvmClassifier(final SvmModel model) {
        this.data = null;
        this.kernel = null;
        this.model = model;
        this.problem = null;
    }

    /**
     * Compact model so that it consists of a single support vector per class.
     */
    public SvmClassifier compact() {
        if (model == null) {
            throw new IllegalStateException();
        }
        CompactSimpleSvmBuilder builder = getCompactBuilder();
        for (int i = 0, k = 0; i < model.nr_class; i++) {
            for (int j = 0; j < model.nSV[i]; j++, k++) {
                builder.present(model.SV[k].getValue(), model.SV[k].getIndex());
            }
        }
        return builder.build();
    }

    public CompactSimpleSvmBuilder getCompactBuilder() {
        return new CompactSimpleSvmBuilder(model);
    }

    public FloatDenseVector getModel() {
        FloatVector sv = getSupportVector();
//        FloatDenseVector modelvec = new FloatDenseVector(sv.length() + 1, Order.COLUMN, Storage.DIRECT);
        FloatDenseVector modelvec = null;
        for (int i = 0; i < sv.length(); i++) {
            modelvec.set(i, sv.get(i));
        }
        modelvec.set(modelvec.length() - 1, getRho());
        return modelvec;
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

    public FloatVector getSupportVector() {
        if (model == null) {
            throw new IllegalStateException();
        }
        if (model.SV.length != 1) {
            throw new UnsupportedOperationException();
        }
        return model.SV[0].getValue();
    }

    public SvmNode[] getSvmNodes() {
        return model.SV;
    }

    public FloatDenseMatrix score(final FloatMatrix testData) {
        List<Handle> handles = new ArrayList<Handle>();
        for (final FloatVector x : testData.columnsIterator()) {
            handles.add(new Handle() {
                @Override
                public FloatVector getData() {
                    return x;
                }

                @Override
                public int getIndex() {
                    throw new UnsupportedOperationException();
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
//        FloatDenseMatrix scores = new FloatDenseMatrix(n, testData.size());
        FloatDenseMatrix scores = null;
        double[] decvalues = new double[n];
        for (int i = 0; i < testData.size(); i++) {
            Handle handle = testData.get(i);
            Svm.svm_predict_values(model, handle.getData(), decvalues);
//            scores.setColumn(i, FloatDenseVector.valueOf(decvalues));
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
                int index = model.SV[i].getIndex();
                model.SV[i] = new SvmNode(index, data.get(index));
            }
        }
        if (model.SV.length == 0) {
            throw new RuntimeException("SVM training failed");
        }
    }
}
