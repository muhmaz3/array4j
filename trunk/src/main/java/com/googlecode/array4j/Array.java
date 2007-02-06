package com.googlecode.array4j;

import java.nio.DoubleBuffer;

public interface Array<E extends Array> {
    int[] getShape();

    int[] shape();

    double get(int... indexes);

    void set(double value, int... indexes);

    void fill(double value);

    E reshape(int... shape);

    E logEquals();

    E log();

    double sum();

    double sum(int axis);

    DoubleBuffer getBuffer();
}
