package net.lunglet.array4j.matrix;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.lunglet.array4j.Direction;
import net.lunglet.util.AssertUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Abstract base class for matrices.
 */
public abstract class AbstractMatrix<V extends Vector> implements Matrix {
    private static final long serialVersionUID = 1L;

    protected static int vectorColumns(final int size, final Direction direction) {
        AssertUtils.checkArgument(size >= 0);
        if (direction.equals(Direction.ROW)) {
            return size;
        } else {
            if (size == 0) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    protected static int vectorRows(final int size, final Direction direction) {
        AssertUtils.checkArgument(size >= 0);
        if (direction.equals(Direction.COLUMN)) {
            return size;
        } else {
            if (size == 0) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    protected final AbstractMatrix<V> base;

    protected final int columns;

    protected final int length;

    protected final int rows;

    public AbstractMatrix(final AbstractMatrix<V> base, final int rows, final int columns) {
        AssertUtils.checkArgument(rows >= 0);
        AssertUtils.checkArgument(columns >= 0);
        this.rows = rows;
        this.columns = columns;
        if ((1L * rows * columns) > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("dimensions too large");
        }
        this.length = rows * columns;
        this.base = base;
    }

    protected final void checkArithmeticOperand(final Matrix other) {
        if (rows != other.rows()) {
            throw new IllegalArgumentException();
        }
        if (columns != other.columns()) {
            throw new IllegalArgumentException();
        }
    }

    protected final void checkColumnIndex(final int column) {
        if (column < 0 || column >= columns) {
            throw new IndexOutOfBoundsException(String.format("Column index %d out of bounds [0,%d)", column, columns));
        }
    }

    protected final void checkColumnVector(final Vector vector) {
        if (vector.length() != rows) {
            throw new IllegalArgumentException();
        }
    }

    protected final void checkRowIndex(final int row) {
        if (row < 0 || row >= rows) {
            throw new IndexOutOfBoundsException(String.format("Row index %d out of bounds [0,%d)", row, rows));
        }
    }

    protected final void checkRowVector(final Vector vector) {
        if (vector.length() != columns) {
            throw new IllegalArgumentException("vector with length " + columns + " required (length is "
                    + vector.length() + ")");
        }
    }

    public abstract V column(int column);

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

    public final List<V> columnsList() {
        List<V> columnsList = new ArrayList<V>();
        for (V column : columnsIterator()) {
            columnsList.add(column);
        }
        return columnsList;
    }

    public final Direction direction() {
        if (rows <= 1 && rows == columns) {
            return Direction.BOTH;
        } else if (isRowVector()) {
            return Direction.ROW;
        } else if (isColumnVector()) {
            return Direction.COLUMN;
        }
        throw new AssertionError();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof AbstractMatrix)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        AbstractMatrix<?> other = (AbstractMatrix<?>) obj;
        EqualsBuilder equalsBuilder = new EqualsBuilder();
        equalsBuilder.append(rows, other.rows);
        equalsBuilder.append(columns, other.columns);
        return equalsBuilder.isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(rows).append(columns).toHashCode();
    }

    public final boolean isColumnVector() {
        return columns <= 1;
    }

    public final boolean isRowVector() {
        return rows <= 1;
    }

    public final boolean isSquare() {
        return rows == columns;
    }

    public abstract V row(int row);

    public final int rows() {
        return rows;
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
