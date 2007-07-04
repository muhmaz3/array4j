package com.googlecode.array4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public final class FloatMatrixTest<M extends FloatMatrix<M, V>, V extends FloatVector<V>> {
    @Parameters
    public static Collection<?> data() {
        return Arrays.asList(new Object[][]{{new DenseFloatMatrixFactory()}, {new DenseFloatMatrixFactory()}});
    }

    private final FloatMatrixFactory<M, V> factory;

    public FloatMatrixTest(final FloatMatrixFactory<M, V> factory) {
        this.factory = factory;
    }

    private void checkToArrays(final int rows, final int columns) {
        final float[] data = new float[rows * columns];
        for (int i = 0; i < data.length; i++) {
            data[i] = i + 1;
        }

        final FloatMatrix<?, ?> rowMatrix = factory.createMatrix(data, rows, columns, Orientation.ROW);
        final float[][] rowArrays1 = rowMatrix.toRowArrays();
        assertEquals(rows, rowArrays1.length);
        final float[][] colArrays1 = rowMatrix.toColumnArrays();
        assertEquals(columns, colArrays1.length);

        final FloatMatrix<?, ?> colMatrix = factory.createMatrix(data, rows, columns, Orientation.COLUMN);
        final float[][] rowArrays2 = colMatrix.toRowArrays();
        assertEquals(rows, rowArrays2.length);
        final float[][] colArrays2 = colMatrix.toColumnArrays();
        assertEquals(columns, colArrays2.length);

        for (int row = 0; row < rows; row++) {
            assertEquals(columns, rowArrays1[row].length);
            assertEquals(columns, rowArrays2[row].length);
            for (int column = 0; column < columns; column++) {
                assertEquals(rows, colArrays1[column].length);
                final float value1 = row * columns + column + 1.0f;
                assertEquals(value1, rowArrays1[row][column]);
                assertEquals(value1, colArrays1[column][row]);

                assertEquals(rows, colArrays2[column].length);
                final float value2 = column * rows + row + 1.0f;
                assertEquals(value2, rowArrays2[row][column]);
                assertEquals(value2, colArrays2[column][row]);
            }
        }
    }

    private M createColumnMatrixRange(final int rows, final int columns) {
        final float[] data = new float[rows * columns];
        for (int i = 0, k = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                data[k++] = 1.0f + i + j * columns;
            }
        }
        return factory.createMatrix(data, rows, columns, Orientation.COLUMN);
    }

    private void createMatrices(final float[] data, final int offset, final int stride, final int maxSize) {
        for (int rows = 0; rows <= maxSize; rows++) {
            for (int columns = 0; columns <= maxSize; columns++) {
                if (rows * columns > maxSize) {
                    continue;
                }
                for (final Orientation orientation : Orientation.values()) {
                    factory.createMatrix(data, rows, columns, offset, stride, orientation);
                }
            }
        }
    }

    private M createRowMatrixRange(final int rows, final int columns) {
        final float[] data = new float[rows * columns];
        for (int i = 0; i < data.length; i++) {
            data[i] = 1.0f + i;
        }
        return factory.createMatrix(data, rows, columns, Orientation.ROW);
    }

    private void printMatrix(final PrintStream stream, final FloatMatrix<?, ?> matrix) {
        final float[][] rows = matrix.toRowArrays();
        stream.println(String.format("%d x %d", matrix.rows(), matrix.columns()));
        for (final float[] row : rows) {
            stream.println(Arrays.toString(row));
        }
    }

    @Test
    public void testColumn() {
        final int rows = 5;
        final int columns = 7;
        FloatMatrix<?, ?> matrix;
        float[][] matrixColumns;

        matrix = createRowMatrixRange(rows, columns);
        matrixColumns = matrix.toColumnArrays();
        for (int column = 0; column < columns; column++) {
            assertTrue(Arrays.equals(matrixColumns[column], matrix.column(column).toArray()));
        }

        matrix = createColumnMatrixRange(rows, columns);
        matrixColumns = matrix.toColumnArrays();
        for (int column = 0; column < columns; column++) {
            assertTrue(Arrays.equals(matrixColumns[column], matrix.column(column).toArray()));
        }
    }

    @Test
    public void testConstructor() {
        factory.createMatrix(0, 0);
        factory.createMatrix(0, 1);
        factory.createMatrix(1, 0);
        factory.createMatrix(10, 0);
        factory.createMatrix(0, 10);
        factory.createMatrix(1, 1);
        factory.createMatrix(2, 1);
        factory.createMatrix(1, 2);
        factory.createMatrix(2, 10);
        factory.createMatrix(10, 2);
        factory.createMatrix(10, 10);
        factory.createMatrix(5, 5, Orientation.ROW);
        factory.createMatrix(5, 5, Orientation.COLUMN);
    }

    @Test
    public void testConstructors2() {
        final float[] data = new float[12];
        for (int i = 0; i < data.length; i++) {
            data[i] = i + 1;
        }

        for (int offset = 0; offset < data.length; offset++) {
            final int firstStride = -data.length + offset - 1;
            final int lastStride = data.length - offset + 1;
            for (int stride = firstStride; stride <= lastStride; stride++) {
                int maxSize;
                if (stride == 0) {
                    // any size is valid
                    maxSize = 3;
                } else {
                    maxSize = 0;
                    int position = offset;
                    while (position >= 0 && position < data.length - stride + 1) {
                        maxSize++;
                        position += stride;
                    }
                    // size can always be at least 1, regardless of the stride
                    if (maxSize == 0) {
                        maxSize = 1;
                    }
                }
                createMatrices(data, offset, stride, maxSize);
            }
        }
    }

    @Test
    public void testIterators() {
        final int rows = 4;
        final int columns = 3;
        final FloatMatrix<?, ?> matrix = createRowMatrixRange(rows, columns);
        for (final FloatVector<?> rowVector : matrix.rowsIterator()) {
            assertEquals(columns, rowVector.size());
            assertEquals(1, rowVector.rows());
            assertEquals(columns, rowVector.columns());
        }

        for (final FloatVector<?> columnVector : matrix.columnsIterator()) {
            assertEquals(rows, columnVector.size());
            assertEquals(rows, columnVector.rows());
            assertEquals(1, columnVector.columns());
        }
    }

    @Test
    public void testRow() {
        final int rows = 5;
        final int columns = 7;
        FloatMatrix<?, ?> matrix;
        float[][] matrixRows;

        matrix = createRowMatrixRange(rows, columns);
        matrixRows = matrix.toRowArrays();
        for (int row = 0; row < rows; row++) {
            assertTrue(Arrays.equals(matrixRows[row], matrix.row(row).toArray()));
        }

        matrix = createColumnMatrixRange(rows, columns);
        matrixRows = matrix.toRowArrays();
        for (int row = 0; row < rows; row++) {
            assertTrue(Arrays.equals(matrixRows[row], matrix.row(row).toArray()));
        }
    }

    @Test
    public void testSetColumn() {
        final int rows = 3;
        final int columns = 4;
        final float[][] values = {{10.0f, 20.0f, 30.0f}, {40.0f, 50.0f, 60.0f}, {70.0f, 80.0f, 90.0f},
                {100.0f, 110.0f, 120.0f}};
        assertEquals(columns, values.length);
        final FloatMatrix<?, ?> rowMatrix = createRowMatrixRange(rows, columns);
        final FloatMatrix<?, ?> colMatrix = createColumnMatrixRange(rows, columns);

        for (int column = 0; column < columns; column++) {
            rowMatrix.setColumn(column, rowMatrix.createColumnVector(values[column]));
            assertTrue("Columns must be equal", Arrays.equals(values[column], rowMatrix.column(column).toArray()));

            colMatrix.setColumn(column, colMatrix.createColumnVector(values[column]));
            assertTrue("Columns must be equal", Arrays.equals(values[column], colMatrix.column(column).toArray()));
        }
    }

    @Test
    public void testSetRow() {
        final int rows = 4;
        final int columns = 3;
        final float[][] values = {{10.0f, 20.0f, 30.0f}, {40.0f, 50.0f, 60.0f}, {70.0f, 80.0f, 90.0f},
                {100.0f, 110.0f, 120.0f}};
        assertEquals(rows, values.length);
        final M rowMatrix = createRowMatrixRange(rows, columns);
        final M colMatrix = createColumnMatrixRange(rows, columns);
        for (int row = 0; row < rows; row++) {
            rowMatrix.setRow(row, rowMatrix.createRowVector(values[row]));
            assertTrue("Rows must be equal", Arrays.equals(values[row], rowMatrix.row(row).toArray()));

            colMatrix.setRow(row, colMatrix.createRowVector(values[row]));
            assertTrue("Rows must be equal", Arrays.equals(values[row], colMatrix.row(row).toArray()));
        }
    }

    @Test
    public void testToArray() {
        FloatMatrix<?, ?> matrix;
        float[] arr;

        // test stride = 0
        matrix = factory.createMatrix(new float[]{1.0f, 2.0f}, 1, 2, 1, 0, Orientation.ROW);
        arr = matrix.toArray();
        assertEquals(2.0f, arr[0]);
        assertEquals(2.0f, arr[1]);

        // test size = 1 with a large stride
        matrix = factory.createMatrix(new float[]{1.0f}, 1, 1, 0, Integer.MAX_VALUE, Orientation.ROW);
        arr = matrix.toArray();
        assertEquals(1.0f, arr[0]);

        // test size = 0
        matrix = factory.createMatrix(new float[]{}, 0, 0, 0, 0, Orientation.ROW);
        assertEquals(0, matrix.toArray().length);

        // test stride = -1
        matrix = factory.createMatrix(new float[]{1.0f, 2.0f}, 1, 2, 1, -1, Orientation.ROW);
        arr = matrix.toArray();
        assertEquals(2.0f, arr[0]);
        assertEquals(1.0f, arr[1]);
    }

    @Test
    public void testToArrays() {
        for (int rows = 0; rows <= 3; rows++) {
            for (int columns = 0; columns <= 4; columns++) {
                checkToArrays(rows, columns);
            }
        }
    }

    @Test
    public void testTranspose() {
        final int rows = 3;
        final int columns = 4;
        final FloatMatrix<?, ?> original = createRowMatrixRange(rows, columns);
        final FloatMatrix<?, ?> transpose = original.transpose();
        final FloatMatrix<?, ?> original2 = transpose.transpose();

        assertEquals(rows, original.rows());
        assertEquals(rows, transpose.columns());
        assertEquals(rows, original2.rows());

        assertEquals(columns, original.columns());
        assertEquals(columns, transpose.rows());
        assertEquals(columns, original2.columns());
    }
}
