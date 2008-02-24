package com.googlecode.array4j.dense;

import static org.junit.Assert.assertEquals;
import com.googlecode.array4j.MatrixTestSupport;
import com.googlecode.array4j.Order;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.matrix.dense.DenseFactory;
import com.googlecode.array4j.matrix.dense.FloatDenseMatrix;
import com.googlecode.array4j.matrix.dense.FloatDenseUtils;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public final class FloatDenseUtilsTest {
    @Parameters
    public static Collection<?> data() {
        return Arrays.asList(new Object[][]{{Order.ROW}, {Order.COLUMN}});
    }

    private final Order order;

    public FloatDenseUtilsTest(final Order order) {
        this.order = order;
    }

    @Test
    public void testSubMatrixColumns() {
        for (int rows = 0; rows < 5; rows++) {
            for (int columns = 0; columns < 8; columns++) {
                FloatDenseMatrix x = DenseFactory.createFloatMatrix(rows, columns, order, Storage.HEAP);
                MatrixTestSupport.populateMatrix(x);
                for (int i = 0; i < columns; i++) {
                    for (int j = i; j <= columns; j++) {
                        FloatDenseMatrix y = FloatDenseUtils.subMatrixColumns(x, i, j);
                        assertEquals(x.rows(), y.rows());
                        assertEquals(j - i, y.columns());
                        for (int k = i; k < j; k++) {
                            int n = k - i;
                            for (int m = 0; m < x.rows(); m++) {
                                assertEquals(x.get(m, k), y.get(m, n), 0);
                            }
                            assertEquals(x.column(k), y.column(n));
                        }
                    }
                }
            }
        }
    }
}
