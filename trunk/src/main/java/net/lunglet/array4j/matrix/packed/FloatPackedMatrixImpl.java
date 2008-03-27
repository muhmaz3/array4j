package net.lunglet.array4j.matrix.packed;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.FloatBuffer;
import net.jcip.annotations.NotThreadSafe;
import net.lunglet.array4j.Direction;
import net.lunglet.array4j.Storage;
import net.lunglet.array4j.matrix.FloatVector;
import net.lunglet.array4j.matrix.dense.DenseFactory;
import net.lunglet.array4j.matrix.dense.FloatDenseVector;
import net.lunglet.array4j.matrix.util.FloatMatrixUtils;
import net.lunglet.util.BufferUtils;
import org.apache.commons.lang.NotImplementedException;

@NotThreadSafe
final class FloatPackedMatrixImpl extends AbstractPackedMatrix<FloatDenseVector> implements FloatPackedMatrix {
    private static final long serialVersionUID = 1L;

    private transient FloatBuffer data;

    FloatPackedMatrixImpl(final FloatPackedMatrixImpl other, final boolean transpose) {
        super(transpose ? other.columns : other.rows, transpose ? other.rows : other.columns,
                transpose ? other.packedType.transpose() : other.packedType, other.storage);
        this.data = other.data;
    }

    FloatPackedMatrixImpl(final int rows, final int columns, final PackedType packedType, final Storage storage) {
        super(rows, columns, packedType, storage);
        this.data = createData();
    }

    @Override
    public FloatDenseVector column(final int column) {
        FloatDenseVector v = DenseFactory.floatVector(rows(), Direction.COLUMN, storage());
        for (int row = 0; row < rows; row++) {
            v.set(row, get(row, column));
        }
        return v;
    }

    private FloatBuffer createData() {
        return BufferUtils.createFloatBuffer(getPackedLength(), storage);
    }

    /** {@inheritDoc} */
    public FloatBuffer data() {
        return ((FloatBuffer) data.position(0)).slice();
    }

    @Override
    public float get(final int row, final int column) {
        if (nonZeroElement(row, column)) {
            return data.get(elementOffset(row, column));
        } else {
            return 0.0f;
        }
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.data = createData();
        FloatBuffer buf = data();
        int len = getPackedLength();
        for (int i = 0; i < len; i++) {
            buf.put(in.readFloat());
        }
    }

    /** {@inheritDoc} */
    @Override
    public FloatDenseVector row(final int row) {
        FloatDenseVector v = DenseFactory.floatVector(columns(), Direction.ROW, storage());
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

    public float[] toArray() {
        // TODO figure out if this returns a dense array or a packed array
        // TODO a packed array will probably be more useful
        throw new NotImplementedException();
    }

    @Override
    public float[][] toColumnArrays() {
        throw new NotImplementedException();
    }

    @Override
    public float[][] toRowArrays() {
        throw new NotImplementedException();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return FloatMatrixUtils.toString(this);
    }

    /** {@inheritDoc} */
    @Override
    public FloatPackedMatrix transpose() {
        return new FloatPackedMatrixImpl(this, true);
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        FloatBuffer buf = data();
        int len = getPackedLength();
        for (int i = 0; i < len; i++) {
            out.writeFloat(buf.get(i));
        }
    }
}
