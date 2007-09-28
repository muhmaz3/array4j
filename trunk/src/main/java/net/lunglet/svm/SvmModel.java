package net.lunglet.svm;

import java.io.Serializable;

final class SvmModel implements Serializable {
    private static final long serialVersionUID = 1L;

    // total #SV
    int l;

    // label of each class (label[k])
    int[] label;

    // number of classes, = 2 in regression/one class svm
    int nr_class;

    // number of SVs for each class (nSV[k])
    // nSV[0] + nSV[1] + ... + nSV[k-1] = l
    int[] nSV;

    // parameter
    SvmParameter param;

    // pariwise probability information
    double[] probA;

    double[] probB;

    // constants in decision functions (rho[k*(k-1)/2])
    double[] rho;

    // for classification only

    // SVs (SV[l])
    public SvmNode[] SV;

    // coefficients for SVs in decision functions (sv_coef[k-1][l])
    double[][] sv_coef;
};
