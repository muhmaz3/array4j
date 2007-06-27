package com.googlecode.array4j;

import java.util.Iterator;

public abstract class AbstractMatrix<M extends Matrix, V extends Vector> extends AbstractArray<M> implements
        Matrix<M, V> {
    final int rows;

    final int columns;

    public AbstractMatrix(final int rows, final int columns) {
        super(rows * columns);
        checkArgument(rows >= 0);
        checkArgument(columns >= 0);
        this.rows = rows;
        this.columns = columns;
    }

    public final int rows() {
        return rows;
    }

    public final int columns() {
        return columns;
    }

    public final Iterable<V> columnsIterator() {
        return new Iterable<V>() {
            public Iterator<V> iterator() {
                return new Iterator<V>() {
                    private int column = 0;

                    public boolean hasNext() {
                        return column < columns;
                    }

                    public V next() {
                        return column(column++);
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    public final Iterable<V> rowsIterator() {
        return new Iterable<V>() {
            public Iterator<V> iterator() {
                return new Iterator<V>() {
                    private int row = 0;

                    public boolean hasNext() {
                        return row < rows;
                    }

                    public V next() {
                        return row(row++);
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }
}
