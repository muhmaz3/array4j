package com.googlecode.array4j;

import java.util.Arrays;

public final class DenseComplexFloatSupport<M extends DenseMatrix<M, V> & ComplexFloatMatrix<M, V>, V extends DenseVector<V> & ComplexFloatVector<V>>
        extends AbstractDenseObjectSupport<ComplexFloat> {
    private final int columns;

    private final float[] data;

    private final M matrix;

    private DenseMatrixSupport<M, V> matrixSupport;

    private final int offset;

    private final Orientation orientation;

    private final int rows;

    private final int size;

    private final int stride;

    public DenseComplexFloatSupport(final M matrix, final float[] data) {
        this.matrix = matrix;
        // TODO might be able to get rid of this...
        this.matrixSupport = matrix.getMatrixSupport();
        this.data = data;
        this.size = matrix.size();
        this.rows = matrix.rows();
        this.columns = matrix.columns();
        this.offset = matrix.offset();
        this.stride = matrix.stride();
        this.orientation = matrix.orientation();
    }

    @Override
    protected ComplexFloat[] createArray(final int length) {
        return new ComplexFloat[length];
    }

    @Override
    protected ComplexFloat[][] createArrayArray(final int length) {
        return new ComplexFloat[length][];
    }

    @Override
    public ComplexFloat[][] toRowArrays() {
        ComplexFloat[][] rowsArr = createArrayArray(rows);
        for (int row = 0; row < rows; row++) {
            rowsArr[row] = createArray(columns);
        }
        for (int row = 0; row < rows; row++) {
            ComplexFloat[] rowArr = rowsArr[row];
            for (int column = 0; column < columns; column++) {
                int position = offset;
                if (orientation.equals(Orientation.ROW)) {
                    position += (row * columns + column) * stride;
                } else {
                    position += offset + (column * rows + row) * stride;
                }
                rowArr[column] = new ComplexFloat(position, position + 1);
            }
        }
        return rowsArr;
    }

    @Override
    public ComplexFloat[][] toColumnArrays() {
        ComplexFloat[][] columnsArr = createArrayArray(columns);
        for (int column = 0; column < columns; column++) {
            columnsArr[column] = createArray(rows);
        }
        for (int column = 0; column < columns; column++) {
            ComplexFloat[] columnArr = columnsArr[column];
            for (int row = 0; row < rows; row++) {
                int position = offset;
                if (orientation.equals(Orientation.COLUMN)) {
                    position += (column * rows + row) * stride;
                } else {
                    position += (row * columns + column) * stride;
                }
                columnArr[row] = new ComplexFloat(position, position + 1);
            }
        }
        return columnsArr;
    }

    public void setColumn(final int column, final ComplexFloatVector columnVector) {
        matrixSupport.checkColumnIndex(column);
        if (!columnVector.isColumnVector() || rows != columnVector.size()) {
            throw new IllegalArgumentException();
        }

        if (!(columnVector instanceof DenseComplexFloatVector)) {
            // TODO support any FloatVector instance here, maybe with a special
            // case for the DirectFloatVector
            throw new UnsupportedOperationException();
        }

        DenseComplexFloatVector denseColVec = (DenseComplexFloatVector) columnVector;
        denseColVec.copyTo(data, matrixSupport.columnOffset(column), matrixSupport.rowStride);
    }

    public void setRow(final int row, final ComplexFloatVector rowVector) {
        matrixSupport.checkRowIndex(row);
        if (!rowVector.isRowVector() || columns != rowVector.size()) {
            throw new IllegalArgumentException();
        }

        if (!(rowVector instanceof DenseComplexFloatVector)) {
            // TODO support any FloatVector instance here, maybe with a special
            // case for the DirectFloatVector
            throw new UnsupportedOperationException();
        }

        DenseComplexFloatVector denseRowVec = (DenseComplexFloatVector) rowVector;
        denseRowVec.copyTo(data, matrixSupport.rowOffset(row), matrixSupport.columnStride);
    }

    @Override
    public ComplexFloat[] toArray() {
        ComplexFloat[] arr = new ComplexFloat[size];
        if (size == 0) {
            return arr;
        }
        if (stride == 0) {
            ComplexFloat value = new ComplexFloat(data[offset], data[offset + 1]);
            Arrays.fill(arr, value);
            return arr;
        }
        for (int i = offset, j = 0; j < size; i += stride, j++) {
            arr[j] = new ComplexFloat(data[i], data[i + 1]);
        }
        return arr;
    }
}
