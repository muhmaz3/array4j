package net.lunglet.svm;

//
// Kernel evaluation
//
// the static method k_function is for doing single kernel evaluation
// the constructor of Kernel prepares to calculate the l*l kernel matrix
// the member function get_Q is for getting one column from the Q Matrix
//
abstract class QMatrix {
    abstract float[] getQ(int column, int len);

    abstract float[] getQD();

    abstract void swapIndex(int i, int j);
};
