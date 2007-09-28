package net.lunglet.svm;

import java.io.Serializable;

final class SvmParameter implements Cloneable, Serializable {
    private static final long serialVersionUID = 1L;

    /* svm_type */
    public static final int C_SVC = 1;

    public static final int EPSILON_SVR = 4;

    /* kernel_type */
    public static final int LINEAR = 1;

    public static final int NU_SVC = 2;

    public static final int NU_SVR = 5;

    public static final int ONE_CLASS = 3;

    public static final int POLY = 2;

    public static final int PRECOMPUTED = 5;

    public static final int RBF = 3;

    public static final int SIGMOID = 4;

    // for C_SVC, EPSILON_SVR and NU_SVR
    public double C;

    // these are for training only in MB
    public double cache_size;

    // for poly/sigmoid
    public double coef0;

    // for poly
    public int degree;

    // stopping criteria
    public double eps;

    // for poly/rbf/sigmoid
    public double gamma;

    public int kernel_type;

    // for C_SVC
    public int nr_weight;

    // for NU_SVC, ONE_CLASS, and NU_SVR
    public double nu;

    // for EPSILON_SVR
    public double p;

    // do probability estimates
    public int probability;

    // use the shrinking heuristics
    public int shrinking;

    public int svm_type;

    // for C_SVC
    public double[] weight;

    // for C_SVC
    public int[] weight_label;

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
