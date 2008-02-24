package net.lunglet.array4j.matrix;

import net.lunglet.array4j.Direction;

public interface Vector extends Matrix {
    Direction direction();

    boolean isColumnVector();

    boolean isRowVector();

    int length();

    /** Change direction of vector. */
    Vector transpose();
}
