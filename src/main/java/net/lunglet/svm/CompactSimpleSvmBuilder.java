package net.lunglet.svm;

import com.googlecode.array4j.blas.FloatDenseBLAS;
import com.googlecode.array4j.matrix.FloatVector;
import com.googlecode.array4j.matrix.dense.FloatDenseVector;
import java.util.HashMap;
import java.util.Map;
import net.lunglet.util.AssertUtils;

// TODO rename this class

public final class CompactSimpleSvmBuilder {
    private final SvmModel compactModel;

    private final FloatDenseVector sv;

    private final Map<Integer, Float> weights;

    public CompactSimpleSvmBuilder(final SvmModel model) {
        if (model.nr_class != 2) {
            throw new UnsupportedOperationException();
        }
        this.compactModel = new SvmModel();
        // TODO might want to make deep copies here
        compactModel.param = model.param;
        compactModel.nr_class = model.nr_class;
        compactModel.rho = model.rho;
        this.weights = new HashMap<Integer, Float>();
        for (int i = 0, k = 0; i < model.nr_class; i++) {
            for (int j = 0; j < model.nSV[i]; j++, k++) {
                int index = model.SV[k].getHandleIndex();
                AssertUtils.assertFalse(weights.containsKey(index));
                weights.put(index, (float) model.sv_coef[0][k]);
            }
        }
//        this.sv = new FloatDenseVector(model.SV[0].getValue().length(), Order.COLUMN, Storage.DIRECT);
        this.sv = null;
    }

    public SvmClassifier build() {
        if (weights.size() != 0) {
            throw new IllegalStateException("required training data was not presented");
        }
        compactModel.SV = new SvmNode[]{new SvmNode(0, sv)};
        compactModel.sv_coef = new double[][]{{1.0, 0.0}};
        compactModel.nSV = new int[]{1, 0};
        compactModel.l = 1;
        return new SvmClassifier(compactModel);
    }

    public void present(final FloatVector x, final int index) {
        if (weights.size() == 0 || !weights.containsKey(index)) {
            return;
        }
        float alpha = weights.get(index);
        FloatDenseBLAS.DEFAULT.axpy(alpha, (FloatDenseVector) x, sv);
        weights.remove(index);
    }
}
