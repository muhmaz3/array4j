package com.googlecode.array4j;

public abstract class AbstractDenseObjectSupport<E> {
    private final int size;

    private final int rows;

    private final int columns;

    private final Orientation orientation;

    public AbstractDenseObjectSupport() {
        this.size = 0;
        this.rows = 0;
        this.columns = 0;
        this.orientation = Orientation.ROW;
    }

    public E[] toArray() {
        return null;
    }

    public E[][] toRowArrays() {
        E[][] rowsArr = createArrayArray(rows);
        for (int row = 0; row < rows; row++) {
            rowsArr[row] = createArray(columns);
        }
        for (int row = 0; row < rows; row++) {
            E[] rowArr = rowsArr[row];
            for (int column = 0; column < columns; column++) {
                if (orientation.equals(Orientation.ROW)) {
                    // position = offset + (row * columns + column) * stride
                } else {
                    // position = offset + (column * rows + row) * stride
                }
                rowArr[column] = null;
            }
        }
        return rowsArr;
    }

    public E[][] toColumnArrays() {
        E[][] columnsArr = createArrayArray(columns);
        for (int column = 0; column < columns; column++) {
            columnsArr[column] = createArray(rows);
        }
        for (int column = 0; column < columns; column++) {
            E[] columnArr = columnsArr[column];
            for (int row = 0; row < rows; row++) {
                if (orientation.equals(Orientation.COLUMN)) {
                    // position = offset + (column * rows + row) * stride
                } else {
                    // position = offset + (row * columns + column) * stride
                }
                columnArr[row] = null;
            }
        }
        return columnsArr;
    }

    protected abstract E[] createArray(int length);

    protected abstract E[][] createArrayArray(int length);
}
