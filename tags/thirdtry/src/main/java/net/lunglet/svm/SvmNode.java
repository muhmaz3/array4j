package net.lunglet.svm;

import com.googlecode.array4j.FloatVector;

final class SvmNode {
    int index;

    FloatVector<?> value;

    public SvmNode(int index, FloatVector<?> value) {
        this.index = index;
        this.value = value;
    }
}
