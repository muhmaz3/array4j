package net.lunglet.array4j.matrix.dense;

import java.nio.Buffer;
import java.util.List;
import net.lunglet.array4j.Order;
import net.lunglet.array4j.Storage;
import net.lunglet.array4j.matrix.Matrix;

public interface DenseMatrix extends Matrix {
    /** {@inheritDoc} */
    DenseVector asVector();

    /** {@inheritDoc} */
    DenseVector column(int column);

    /** {@inheritDoc} */
    Iterable<? extends DenseVector> columnsIterator();

    /** {@inheritDoc} */
    List<? extends DenseVector> columnsList();

    /** Get data buffer. */
    Buffer data();

    int leadingDimension();

    /** Get the offset into the data buffer of the first element. */
    int offset();

    /** Get the storage order (row-major or column-major). */
    Order order();

    /** {@inheritDoc} */
    DenseVector row(int row);

    Iterable<? extends DenseVector> rowsIterator();

    /** Get the storage type (heap or direct). */
    Storage storage();

    /** Get the stride between elements. */
    int stride();

    /** {@inheritDoc} */
    DenseMatrix transpose();
}
