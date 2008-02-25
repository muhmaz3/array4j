package net.lunglet.array4j.matrix.packed;

import java.nio.FloatBuffer;
import net.jcip.annotations.NotThreadSafe;
import net.lunglet.array4j.Direction;
import net.lunglet.array4j.Storage;
import net.lunglet.array4j.matrix.FloatMatrix;
import net.lunglet.array4j.matrix.FloatVector;
import net.lunglet.array4j.matrix.dense.DenseFactory;
import net.lunglet.array4j.matrix.dense.FloatDenseVector;
import net.lunglet.array4j.matrix.util.FloatMatrixUtils;
import net.lunglet.util.BufferUtils;

@NotThreadSafe
final class FloatPackedMatrixImpl extends AbstractPackedMatrix<FloatDenseVector> implements FloatPackedMatrix {
    private static final long serialVersionUID = 1L;

    private transient FloatBuffer data;

    FloatPackedMatrixImpl(final FloatBuffer data, final int rows, final int columns, final PackedType packedType) {
        super(rows, columns, packedType);
        this.data = data;
    }

    FloatPackedMatrixImpl(final int rows, final int columns, final PackedType packedType, final Storage storage) {
        super(rows, columns, packedType);
        this.data = BufferUtils.createFloatBuffer(getBufferSize(), storage);
    }

    @Override
    public FloatDenseVector column(final int column) {
        FloatDenseVector v = DenseFactory.createFloatVector(rows(), Direction.COLUMN, storage());
        for (int row = 0; row < rows; row++) {
            v.set(row, get(row, column));
        }
        return v;
    }

    /** {@inheritDoc} */
    public FloatBuffer data() {
        return ((FloatBuffer) data.position(0)).slice();
    }

    /** {@inheritDoc} */
    @Override
    public void divideEquals(final float value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float get(final int row, final int column) {
        if (nonZeroElement(row, column)) {
            return data.get(elementOffset(row, column));
        } else {
            return 0.0f;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void minusEquals(final float value) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    @Override
    public void plusEquals(final float value) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    @Override
    public FloatDenseVector row(final int row) {
        FloatDenseVector v = DenseFactory.createFloatVector(columns(), Direction.ROW, storage());
        for (int column = 0; column < columns; column++) {
            v.set(column, get(row, column));
        }
        return v;
    }

    @Override
    public void set(final int row, final int column, final float value) {
        checkCanSet(row, column);
        data.put(elementOffset(row, column), value);
    }

    /** {@inheritDoc} */
    @Override
    public void setColumn(final int column, final FloatVector columnVector) {
        checkColumnIndex(column);
        checkColumnVector(columnVector);
        for (int row = 0; row < rows; row++) {
            if (nonZeroElement(row, column)) {
                set(row, column, columnVector.get(row));
            } else {
                if (columnVector.get(row) != 0.0f) {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setRow(final int row, final FloatVector rowVector) {
        checkRowIndex(row);
        checkRowVector(rowVector);
        for (int column = 0; column < columns; column++) {
            if (nonZeroElement(row, column)) {
                set(row, column, rowVector.get(column));
            } else {
                if (rowVector.get(column) != 0.0f) {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public Storage storage() {
        return data.isDirect() ? Storage.DIRECT : Storage.HEAP;
    }

    /** {@inheritDoc} */
    @Override
    public void timesEquals(final float value) {
        throw new UnsupportedOperationException();
    }

    public float[] toArray() {
        // TODO figure out if this returns a dense array or a packed array
        // TODO a packed array will probably be more useful
        throw new UnsupportedOperationException();
    }

    @Override
    public float[][] toColumnArrays() {
        throw new UnsupportedOperationException();
    }

    @Override
    public float[][] toRowArrays() {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return FloatMatrixUtils.toString(this);
    }

    /** {@inheritDoc} */
    @Override
    public FloatPackedMatrix transpose() {
        return new FloatPackedMatrixImpl(data, rows, columns, packedType.transpose());
    }

    /** {@inheritDoc} */
    @Override
    public void plusEquals(final FloatMatrix other) {
        if (!(other instanceof FloatPackedMatrix)) {
            throw new IllegalArgumentException();
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public FloatDenseVector asVector() {
        throw new UnsupportedOperationException();
    }
}
