package com.googlecode.array4j;

import junit.framework.TestCase;

public final class DenseComplexFloatMatrixTest extends TestCase {
    public void xtest() {
        DenseComplexFloatMatrix matrix = new DenseComplexFloatMatrix(3, 2);

        for (DenseComplexFloatVector row : matrix.rowsIterator()) {
//            System.out.println(row);
        }
    }
}
