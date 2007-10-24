package net.lunglet.svm;

import com.googlecode.array4j.FloatVector;

public interface Handle {
    FloatVector getData();

    int getIndex();

    int getLabel();
}
