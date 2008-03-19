package net.lunglet.array4j.matrix.dense;

import static net.lunglet.array4j.Order.COLUMN;
import static net.lunglet.array4j.Order.ROW;
import static net.lunglet.array4j.Storage.DIRECT;
import static net.lunglet.array4j.Storage.HEAP;
import static org.junit.Assert.assertEquals;
import net.lunglet.array4j.Order;
import net.lunglet.array4j.Storage;
import org.junit.Test;

public final class DenseFactoryTest {
    private static void checkValueOf(final int rows, final int columns, final Storage storage, final Order order) {
        int m = order.equals(ROW) ? rows : columns;
        int n = order.equals(ROW) ? columns : rows;
        float[][] values = new float[m][];
        for (int i = 0; i < values.length; i++) {
            values[i] = new float[n];
        }
        for (int i = 0, k = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                values[i][j] = k++;
            }
        }
        FloatDenseMatrix matrix = DenseFactory.floatMatrix(values, order, storage);
        assertEquals(order, matrix.order());
        assertEquals(storage, matrix.storage());
        if (order.equals(ROW)) {
            assertEquals(rows, matrix.rows());
            if (rows == 0) {
                // for ROW storage, if there are no rows, there is no way to
                // infer the number of columns in the matrix
                assertEquals(0, matrix.columns());
            } else {
                assertEquals(columns, matrix.columns());
            }
        } else {
            assertEquals(columns, matrix.columns());
            if (columns == 0) {
                // for COLUMN storage, if there are no columns, there is no way
                // to infer the number of rows in the matrix
                assertEquals(0, matrix.rows());
            } else {
                assertEquals(rows, matrix.rows());
            }
        }
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                int ii = order.equals(ROW) ? i : j;
                int jj = order.equals(ROW) ? j : i;
                assertEquals(values[i][j], matrix.get(ii, jj), 0);
            }
        }
    }

    @Test
    public void testValueOf() {
        for (int rows = 0; rows < 5; rows++) {
            for (int columns = 0; columns < 5; columns++) {
                for (Storage storage : new Storage[]{DIRECT, HEAP}) {
                    for (Order order : new Order[]{ROW, COLUMN}) {
                        checkValueOf(rows, columns, storage, order);
                    }
                }
            }
        }
    }
}
