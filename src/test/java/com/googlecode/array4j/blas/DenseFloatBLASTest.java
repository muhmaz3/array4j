package com.googlecode.array4j.blas;

import junit.framework.TestCase;

import com.googlecode.array4j.DenseFloatVector;

public final class DenseFloatBLASTest extends TestCase {
    public void test() {
        DenseFloatVector x = new DenseFloatVector(1.0f, 2.0f, 3.0f);
        DenseFloatVector y = new DenseFloatVector(3.0f, 2.0f, 1.0f);
        System.out.println(DenseFloatBLAS.dot(x, y));
    }
}