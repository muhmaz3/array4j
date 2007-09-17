package com.googlecode.array4j;

import java.io.Serializable;

// TODO length should only be a Vector method

public interface Array<A extends Array<A>> extends Serializable {
    int length();

    int[] shape();
}
