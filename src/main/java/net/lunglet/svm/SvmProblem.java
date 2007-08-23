package net.lunglet.svm;

import com.googlecode.array4j.FloatMatrix;

class SvmProblem {
    public FloatMatrix<?, ?> gram;

    public int l;

    public SvmNode[] x;

    public double[] y;
}
