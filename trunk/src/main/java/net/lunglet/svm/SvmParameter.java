package net.lunglet.svm;

final class SvmParameter implements Cloneable {
    /* svm_type */
    public static final int C_SVC = 1;

    public static final int NU_SVC = 2;

    public static final int ONE_CLASS = 3;

    public static final int EPSILON_SVR = 4;

    public static final int NU_SVR = 5;

    /* kernel_type */
    public static final int LINEAR = 1;

    public static final int POLY = 2;

    public static final int RBF = 3;

    public static final int SIGMOID = 4;

    public static final int PRECOMPUTED = 5;

    public int svm_type;

    public int kernel_type;

    // for poly
    public int degree;

    // for poly/rbf/sigmoid
    public double gamma;

    // for poly/sigmoid
    public double coef0;

    // these are for training only in MB
    public double cache_size;

    // stopping criteria
    public double eps;

    // for C_SVC, EPSILON_SVR and NU_SVR
    public double C;

    // for C_SVC
    public int nr_weight;

    // for C_SVC
    public int[] weight_label;

    // for C_SVC
    public double[] weight;

    // for NU_SVC, ONE_CLASS, and NU_SVR
    public double nu;

    // for EPSILON_SVR
    public double p;

    // use the shrinking heuristics
    public int shrinking;

    // do probability estimates
    public int probability;

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
