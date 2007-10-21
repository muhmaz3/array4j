package com.googlecode.array4j;

import java.io.Serializable;

// TODO length should only be a Vector method

// TODO allow rows, columns or entries to be annotated with arbitrary data like indexes or labels

public interface Array<A extends Array<A>> extends Serializable {
    int length();

    int[] shape();
}
