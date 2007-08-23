package com.googlecode.array4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.googlecode.array4j.dense.CFloatDenseMatrixFactory;

@RunWith(value = Parameterized.class)
public final class ComplexFloatMatrixTest<M extends ComplexFloatMatrix<M, V>, V extends ComplexFloatVector<V>> {
    @Parameters
    public static Collection<?> data() {
        return Arrays.asList(new Object[][]{{new CFloatDenseMatrixFactory(Storage.JAVA)},
                {new CFloatDenseMatrixFactory(Storage.DIRECT)}});
    }

    private final ComplexFloatMatrixFactory<M, V> factory;

    public ComplexFloatMatrixTest(final ComplexFloatMatrixFactory<M, V> factory) {
        this.factory = factory;
    }

    private void checkToArrays(final int rows, final int columns) {
        final float[] data = new float[2 * rows * columns];
        for (int i = 0; i < data.length; i++) {
            data[i] = i + 1;
        }

        final ComplexFloatMatrix<?, ?> rowMatrix = factory.createMatrix(data, rows, columns, Orientation.ROW);
        assertNotNull(rowMatrix);
        final ComplexFloat[][] rowArrays1 = rowMatrix.toRowArrays();
        assertEquals(rows, rowArrays1.length);
        final ComplexFloat[][] colArrays1 = rowMatrix.toColumnArrays();
        assertEquals(columns, colArrays1.length);

        final ComplexFloatMatrix<?, ?> colMatrix = factory.createMatrix(data, rows, columns, Orientation.COLUMN);
        assertNotNull(colMatrix);
        final ComplexFloat[][] rowArrays2 = colMatrix.toRowArrays();
        assertEquals(rows, rowArrays2.length);
        final ComplexFloat[][] colArrays2 = colMatrix.toColumnArrays();
        assertEquals(columns, colArrays2.length);

        for (int row = 0; row < rows; row++) {
            assertEquals(columns, rowArrays1[row].length);
            assertEquals(columns, rowArrays2[row].length);
            for (int column = 0; column < columns; column++) {
                assertEquals(rows, colArrays1[column].length);
                float real1 = 2 * (row * columns + column) + 1.0f;
                float imag1 = real1 + 1.0f;
                ComplexFloat value1 = ComplexFloat.valueOf(real1, imag1);
                assertEquals(value1, rowMatrix.get(row, column));
                assertEquals(value1, rowArrays1[row][column]);
                assertEquals(value1, colArrays1[column][row]);

                float real2 = 2 * (column * rows + row) + 1.0f;
                float imag2 = real2 + 1.0f;
                ComplexFloat value2 = ComplexFloat.valueOf(real2, imag2);
                assertEquals(rows, colArrays2[column].length);
                assertEquals(value2, colMatrix.get(row, column));
                assertEquals(value2, rowArrays2[row][column]);
                assertEquals(value2, colArrays2[column][row]);
            }
        }
    }

    private M createColumnMatrixRange(final int rows, final int columns) {
        final float[] data = new float[2 * rows * columns];
        for (int i = 0, k = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                data[k++] = 1.0f + i + j * columns;
                data[k++] = -1.0f + i + j * columns;
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
                    M matrix = factory.createMatrix(data, rows, columns, offset, stride, orientation);
                    assertNotNull(matrix);
                }
            }
        }
    }

    private M createRowMatrixRange(final int rows, final int columns) {
        final float[] data = new float[2 * rows * columns];
        for (int i = 0; i < data.length; i++) {
            data[i] = 1.0f + i;
        }
        M matrix = factory.createMatrix(data, rows, columns, Orientation.ROW);
        assertNotNull(matrix);
        return matrix;
    }

    @Test
    public void testColumn() {
        final int rows = 5;
        final int columns = 7;
        ComplexFloatMatrix<?, ?> matrix;
        ComplexFloat[][] matrixColumns;

        matrix = createRowMatrixRange(rows, columns);
        matrixColumns = matrix.toColumnArrays();
        for (int column = 0; column < columns; column++) {
            assertTrue(matrix.column(column).isColumnVector());
            assertTrue(Arrays.equals(matrixColumns[column], matrix.column(column).toArray()));
        }

        matrix = createColumnMatrixRange(rows, columns);
        matrixColumns = matrix.toColumnArrays();
        for (int column = 0; column < columns; column++) {
            assertTrue(matrix.column(column).isColumnVector());
            assertTrue(Arrays.equals(matrixColumns[column], matrix.column(column).toArray()));
        }
    }

    @Test
    public void testConstructor() {
        assertNotNull(factory.createMatrix(0, 0));
        assertNotNull(factory.createMatrix(0, 1));
        assertNotNull(factory.createMatrix(1, 0));
        assertNotNull(factory.createMatrix(10, 0));
        assertNotNull(factory.createMatrix(0, 10));
        assertNotNull(factory.createMatrix(1, 1));
        assertNotNull(factory.createMatrix(2, 1));
        assertNotNull(factory.createMatrix(1, 2));
        assertNotNull(factory.createMatrix(2, 10));
        assertNotNull(factory.createMatrix(10, 2));
        assertNotNull(factory.createMatrix(10, 10));
        assertNotNull(factory.createMatrix(5, 5, Orientation.ROW));
        assertNotNull(factory.createMatrix(5, 5, Orientation.COLUMN));
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

//    @Test
//    public void testFill() {
//        int k = 0;
//        for (int rows = 0; rows <= 3; rows++) {
//            for (int columns = 0; columns <= 4; columns++) {
//                FloatMatrix<?, ?> matrix = factory.createMatrix(rows, columns);
//                matrix.fill(++k);
//                for (int i = 0; i < rows; i++) {
//                    for (int j = 0; j < columns; j++) {
//                        assertEquals(k, matrix.get(i, j));
//                        assertEquals(k, matrix.row(i).get(j));
//                        assertEquals(k, matrix.column(j).get(i));
//                    }
//                }
//            }
//        }
//    }

    @Test
    public void testIterators() {
        final int rows = 4;
        final int columns = 3;
        final ComplexFloatMatrix<?, ?> matrix = createRowMatrixRange(rows, columns);
        for (final ComplexFloatVector<?> rowVector : matrix.rowsIterator()) {
            assertTrue(rowVector.isRowVector());
            assertEquals(columns, rowVector.size());
            assertEquals(1, rowVector.rows());
            assertEquals(columns, rowVector.columns());
        }

        for (final ComplexFloatVector<?> columnVector : matrix.columnsIterator()) {
            assertTrue(columnVector.isColumnVector());
            assertEquals(rows, columnVector.size());
            assertEquals(rows, columnVector.rows());
            assertEquals(1, columnVector.columns());
        }
    }

    @Test
    public void testRow() {
        final int rows = 5;
        final int columns = 7;
        ComplexFloatMatrix<?, ?> matrix;
        ComplexFloat[][] matrixRows;

        matrix = createRowMatrixRange(rows, columns);
        matrixRows = matrix.toRowArrays();
        for (int row = 0; row < rows; row++) {
            assertTrue(matrix.row(row).isRowVector());
            ComplexFloatVector<?> rowVector = matrix.row(row);
            assertTrue("Rows must be equal", Arrays.equals(matrixRows[row], matrix.row(row).toArray()));
        }

        matrix = createColumnMatrixRange(rows, columns);
        matrixRows = matrix.toRowArrays();
        for (int row = 0; row < rows; row++) {
            assertTrue(matrix.row(row).isRowVector());
            assertTrue("Rows must be equal", Arrays.equals(matrixRows[row], matrix.row(row).toArray()));
        }
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        for (int rows = 0; rows < 4; rows++) {
            for (int columns = 0; columns < 4; columns++) {
                ComplexFloatMatrix<?, ?> input = createRowMatrixRange(rows, columns);
                assertNotNull(input);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(input);
                oos.close();
                ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                ObjectInputStream ois = new ObjectInputStream(bais);
                ComplexFloatMatrix<?, ?> output = (ComplexFloatMatrix<?, ?>) ois.readObject();
                assertEquals(input, output);
                ois.close();
            }
        }
    }

    @Test
    public void testSetColumn() {
        final int rows = 3;
        final int columns = 4;
        ComplexFloat[][] values = new ComplexFloat[columns][];
        for (int j = 0, k = 10; j < columns; j++) {
            values[j] = new ComplexFloat[rows];
            for (int i = 0; i < rows; i++, k += 20) {
                values[j][i] = ComplexFloat.valueOf(k, k + 10);
            }
        }
        assertEquals(columns, values.length);

        final ComplexFloatMatrix<?, ?> rowMatrix = createRowMatrixRange(rows, columns);
        final ComplexFloatMatrix<?, ?> colMatrix = createColumnMatrixRange(rows, columns);
        for (int column = 0; column < columns; column++) {
            ComplexFloatVector<?> newColumn = rowMatrix.createColumnVector();
            for (int index = 0; index < values[column].length; index++) {
                newColumn.set(index, values[column][index]);
            }
            rowMatrix.setColumn(column, newColumn);
            assertTrue("Columns must be equal", Arrays.equals(values[column], rowMatrix.column(column).toArray()));

            newColumn = colMatrix.createColumnVector();
            for (int index = 0; index < values[column].length; index++) {
                newColumn.set(index, values[column][index]);
            }
            colMatrix.setColumn(column, newColumn);
            assertTrue("Columns must be equal", Arrays.equals(values[column], colMatrix.column(column).toArray()));
        }
    }

    @Test
    public void testSetRow() {
        final int rows = 4;
        final int columns = 3;
        ComplexFloat[][] values = new ComplexFloat[rows][];
        for (int i = 0, k = 10; i < rows; i++) {
            values[i] = new ComplexFloat[columns];
            for (int j = 0; j < columns; j++, k += 20) {
                values[i][j] = ComplexFloat.valueOf(k, k + 10);
            }
        }
        assertEquals(rows, values.length);

        final M rowMatrix = createRowMatrixRange(rows, columns);
        final M colMatrix = createColumnMatrixRange(rows, columns);
        for (int row = 0; row < rows; row++) {
            ComplexFloatVector<?> newRow = rowMatrix.createRowVector();
            for (int index = 0; index < values[row].length; index++) {
                newRow.set(index, values[row][index]);
            }
            rowMatrix.setRow(row, newRow);
            assertTrue("Rows must be equal", Arrays.equals(values[row], rowMatrix.row(row).toArray()));

            newRow = colMatrix.createRowVector();
            for (int index = 0; index < values[row].length; index++) {
                newRow.set(index, values[row][index]);
            }
            colMatrix.setRow(row, newRow);
            assertTrue("Rows must be equal", Arrays.equals(values[row], colMatrix.row(row).toArray()));
        }
    }

    @Test
    public void testToArray() {
        ComplexFloatMatrix<?, ?> matrix;
        ComplexFloat[] arr;

        // test stride = 0
        matrix = factory.createMatrix(new float[]{1.0f, 2.0f, 3.0f}, 1, 2, 1, 0, Orientation.ROW);
        assertNotNull(matrix);
        arr = matrix.toArray();
        assertEquals(ComplexFloat.valueOf(2.0f, 3.0f), arr[0]);
        assertEquals(ComplexFloat.valueOf(2.0f, 3.0f), arr[1]);

        // test size = 1 with a large stride
        matrix = factory.createMatrix(new float[]{1.0f, 2.0f}, 1, 1, 0, Integer.MAX_VALUE, Orientation.ROW);
        arr = matrix.toArray();
        assertEquals(ComplexFloat.valueOf(1.0f, 2.0f), arr[0]);

        // test size = 0
        matrix = factory.createMatrix(new float[]{}, 0, 0, 0, 0, Orientation.ROW);
        assertEquals(0, matrix.toArray().length);

        // test stride = -1
        matrix = factory.createMatrix(new float[]{1.0f, 2.0f}, 1, 2, 2, -1, Orientation.ROW);
        arr = matrix.toArray();
        // TODO this doesn't work yet
//        assertEquals(ComplexFloat.valueOf(2.0f, 1.0f), arr[0]);
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
        final ComplexFloatMatrix<?, ?> original = createRowMatrixRange(rows, columns);
        final ComplexFloatMatrix<?, ?> transpose = original.transpose();
        final ComplexFloatMatrix<?, ?> original2 = transpose.transpose();

        assertEquals(rows, original.rows());
        assertEquals(rows, transpose.columns());
        assertEquals(rows, original2.rows());

        assertEquals(columns, original.columns());
        assertEquals(columns, transpose.rows());
        assertEquals(columns, original2.columns());
    }
}
