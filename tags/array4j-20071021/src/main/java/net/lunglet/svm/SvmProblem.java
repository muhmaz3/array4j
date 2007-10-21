package net.lunglet.svm;

import java.io.Serializable;

class SvmProblem implements Serializable {
    private static final long serialVersionUID = 1L;

    transient PrecomputedKernel kernel;

    int l;

    SvmNode[] x;

    double[] y;
}
