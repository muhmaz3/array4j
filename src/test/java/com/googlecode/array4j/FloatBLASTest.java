package com.googlecode.array4j;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.googlecode.array4j.dense.FloatDenseMatrixFactory;
import com.googlecode.array4j.dense.FloatDenseVector;

@RunWith(value = Parameterized.class)
public final class FloatBLASTest<M extends FloatMatrix<M, V>, V extends FloatVector<V>> {
    @Parameters
    public static Collection<?> data() {
        return Arrays.asList(new Object[][]{{new FloatDenseMatrixFactory(Storage.JAVA)},
                {new FloatDenseMatrixFactory(Storage.DIRECT)}});
    }

    private final FloatDenseMatrixFactory factory;

    public FloatBLASTest(final FloatDenseMatrixFactory factory) {
        this.factory = factory;
    }

    @Test
    public void testDot() {
        final FloatDenseVector x = factory.createRowVector(1.0f, 2.0f, 3.0f);
        final FloatDenseVector y = factory.createRowVector(3.0f, 2.0f, 1.0f);
        assertEquals(10.0f, FloatBLAS.dot(x, y));
    }

//    @Test
//    public void testIamax() {
//        final FloatDenseVector x = factory.createRowVector(1.0f, 3.0f, 2.0f);
//        assertEquals(2, FloatBLAS.iamax(x));
//    }
}
