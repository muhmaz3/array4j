package net.lunglet.svm;

import net.lunglet.array4j.matrix.FloatVector;

public interface Handle {
    FloatVector getData();

    int getIndex();

    int getLabel();
}
