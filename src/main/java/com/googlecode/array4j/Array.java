package com.googlecode.array4j;

import java.io.Serializable;

public interface Array<A extends Array<A>> extends Serializable {
    // TODO rename to length
    int size();

    int[] shape();
}
