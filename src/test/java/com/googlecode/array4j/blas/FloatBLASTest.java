package com.googlecode.array4j.blas;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.googlecode.array4j.DenseFloatMatrixFactory;
import com.googlecode.array4j.DirectFloatMatrixFactory;
import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.FloatMatrixFactory;
import com.googlecode.array4j.FloatVector;

@RunWith(value = Parameterized.class)
public final class FloatBLASTest<M extends FloatMatrix<M, V>, V extends FloatVector<V>> {
    @Parameters
    public static Collection<?> data() {
        return Arrays.asList(new Object[][]{{DenseFloatBLAS.INSTANCE, new DenseFloatMatrixFactory()},
                {DirectFloatBLAS.INSTANCE, new DirectFloatMatrixFactory()}});
    }

    private final FloatBLAS<M, V> blas;

    private final FloatMatrixFactory<M, V> factory;

    public FloatBLASTest(final FloatBLAS<M, V> blas, final FloatMatrixFactory<M, V> factory) {
        this.blas = blas;
        this.factory = factory;
    }

    @Test
    public void testDot() {
        final V x = factory.createRowVector(1.0f, 2.0f, 3.0f);
        final V y = factory.createRowVector(3.0f, 2.0f, 1.0f);
        assertEquals(10.0f, blas.dot(x, y));
    }

    @Test
    public void testIamax() {
        final V x = factory.createRowVector(1.0f, 3.0f, 2.0f);
        assertEquals(2, blas.iamax(x));
    }
}
