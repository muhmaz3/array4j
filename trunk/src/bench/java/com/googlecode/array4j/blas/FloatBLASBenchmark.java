package com.googlecode.array4j.blas;

import java.util.Arrays;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.googlecode.array4j.DenseFloatMatrixFactory;
import com.googlecode.array4j.DirectFloatMatrixFactory;
import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.FloatMatrixFactory;
import com.googlecode.array4j.FloatVector;

@RunWith(value = Parameterized.class)
public final class FloatBLASBenchmark<M extends FloatMatrix<M, V>, V extends FloatVector<V>> {
    @Parameters
    public static Collection<?> data() {
        return Arrays.asList(new Object[][]{{DenseFloatBLAS.INSTANCE, new DenseFloatMatrixFactory()},
                {DirectFloatBLAS.INSTANCE, new DirectFloatMatrixFactory()}});
    }

    private final FloatBLAS<M, V> blas;

    private final FloatMatrixFactory<M, V> factory;

    public FloatBLASBenchmark(final FloatBLAS<M, V> blas, final FloatMatrixFactory<M, V> factory) {
        this.blas = blas;
        this.factory = factory;
    }

//    @Test
//    public void benchmarkSum() {
//        System.out.println(blas);
//        final V y = factory.createVector(167772160, Orientation.DEFAULT_FOR_VECTOR);
//        long[] deltas = new long[20];
//        for (int i = 0; i < deltas.length; i++) {
//            long start = System.nanoTime();
//            blas.sum(y);
//            long end = System.nanoTime();
//            deltas[i] = (end - start) / 1000L / 1000L;
//        }
//        System.out.println(Arrays.toString(deltas));
//    }
}
