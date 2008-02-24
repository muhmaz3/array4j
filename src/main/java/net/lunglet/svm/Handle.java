package net.lunglet.svm;

import com.googlecode.array4j.matrix.FloatVector;

public interface Handle {
    FloatVector getData();

    int getIndex();

    int getLabel();
}
