package com.googlecode.array4j.blas;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.FloatVector;
import com.googlecode.array4j.Orientation;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.dense.FloatDenseMatrix;
import com.googlecode.array4j.dense.FloatDenseMatrixFactory;
import com.googlecode.array4j.dense.FloatDenseVector;

@RunWith(value = Parameterized.class)
public final class FloatBLASTest<M extends FloatMatrix<M, V>, V extends FloatVector<V>> {
    @Parameters
    public static Collection<?> data() {
        return Arrays.asList(new Object[][]{
                {new FloatDenseMatrixFactory(Storage.HEAP), new Orientation[]{Orientation.COLUMN}},
                {new FloatDenseMatrixFactory(Storage.DIRECT), new Orientation[]{Orientation.ROW, Orientation.COLUMN}}});
    }

    private final FloatDenseMatrixFactory factory;

    private final Orientation[] blasOrientations;

    public FloatBLASTest(final FloatDenseMatrixFactory factory, final Orientation[] blasOrientations) {
        this.factory = factory;
        this.blasOrientations = blasOrientations;
    }

    @Ignore
    public void testDot() {
        final FloatDenseVector x = factory.createRowVector(1.0f, 2.0f, 3.0f);
        final FloatDenseVector y = factory.createRowVector(3.0f, 2.0f, 1.0f);
        assertEquals(10.0f, FloatDenseBLAS.dot(x, y), 0.0);
    }

    @Ignore
    public void testIamax() {
        final FloatDenseVector x = factory.createRowVector(1.0f, 3.0f, 2.0f);
//         assertEquals(2, blas.iamax(x));
    }

    private static void checkMatrix(final float[][] ref, final FloatDenseMatrix mat) {
        assertEquals(ref.length, mat.rows());
        for (int i = 0; i < mat.rows(); i++) {
            assertEquals(ref[i].length, mat.columns());
            for (int j = 0; j < mat.columns(); j++) {
                // compare relative to the reference value because the absolute
                // values can become large
                assertEquals(ref[i][j], mat.get(i, j), ref[i][j] * 1.0e-6);
            }
        }
    }

    private static void populateMatrix(final FloatDenseMatrix x) {
        for (int i = 0, k = 1; i < x.rows(); i++) {
            for (int j = 0; j < x.columns(); j++, k++) {
                x.set(i, j, (float) k);
            }
        }
    }

    private void checkGemm(final Orientation blasOrientation, final FloatDenseMatrix a, final FloatDenseMatrix b) {
        final float alpha = 1.0f;
        FloatDenseMatrix c = factory.createMatrix(a.rows(), b.columns(), blasOrientation);
        FloatDenseBLAS.gemm(alpha, a, b, 0.0f, c);
        checkMatrix(referenceGemm(alpha, a, b), c);
    }

    @Test
    public void testGemm() {
        Orientation[] aborients = new Orientation[]{Orientation.ROW, Orientation.COLUMN};
        for (Orientation blasOrientation : blasOrientations) {
            for (int rows = 0; rows <= 5; rows++) {
                for (int columns = 0; columns <= 5; columns++) {
                    for (Orientation aorient : aborients) {
                        for (Orientation borient : aborients) {
                            final FloatDenseMatrix a = factory.createMatrix(rows, columns, aorient);
                            final FloatDenseMatrix b = factory.createMatrix(columns, rows, borient);
                            populateMatrix(a);
                            populateMatrix(b);
                            checkGemm(blasOrientation, a, b);
                            checkGemm(blasOrientation, b, a);
                            checkGemm(blasOrientation, a.transpose(), b.transpose());
                            checkGemm(blasOrientation, b.transpose(), a.transpose());
                            checkGemm(blasOrientation, a, a.transpose());
                            checkGemm(blasOrientation, a.transpose(), a);
                            checkGemm(blasOrientation, b, b.transpose());
                            checkGemm(blasOrientation, b.transpose(), b);
                        }
                    }
                }
            }
        }
    }

    private static float[][] referenceGemm(final float alpha, final FloatDenseMatrix a, final FloatDenseMatrix b) {
        float[][] aRowArrays = a.toRowArrays();
        float[][] bColumnArrays = b.toColumnArrays();
        assertEquals(aRowArrays.length, bColumnArrays.length);
        float[][] c = new float[a.rows()][];
        for (int i = 0; i < a.rows(); i++) {
            c[i] = new float[b.columns()];
            for (int j = 0; j < b.columns(); j++) {
                assertEquals(aRowArrays[i].length, bColumnArrays[j].length);
                for (int k = 0; k < aRowArrays[i].length; k++) {
                    c[i][j] += alpha * aRowArrays[i][k] * bColumnArrays[j][k];
                }
            }
        }
        return c;
    }
}
