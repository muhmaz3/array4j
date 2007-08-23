package net.lunglet.svm;

final class SvmModel {
    // parameter
    SvmParameter param;

    // number of classes, = 2 in regression/one class svm
    int nr_class;

    // total #SV
    int l;

    // SVs (SV[l])
    public SvmNode[] SV;

    // coefficients for SVs in decision functions (sv_coef[k-1][l])
    double[][] sv_coef;

    // constants in decision functions (rho[k*(k-1)/2])
    double[] rho;

    // pariwise probability information
    double[] probA;

    double[] probB;

    // for classification only

    // label of each class (label[k])
    int[] label;

    // number of SVs for each class (nSV[k])
    // nSV[0] + nSV[1] + ... + nSV[k-1] = l
    int[] nSV;
};
