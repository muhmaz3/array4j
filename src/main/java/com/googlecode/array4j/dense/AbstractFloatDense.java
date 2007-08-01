package com.googlecode.array4j.dense;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.Random;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.googlecode.array4j.Constants;
import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.FloatVector;
import com.googlecode.array4j.Orientation;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.VectorSupport;
import com.googlecode.array4j.blas.FloatBLAS;
import com.googlecode.array4j.blas.JavaFloatBLAS;
import com.googlecode.array4j.blas.MKLFloatBLAS;

public abstract class AbstractFloatDense<M extends FloatMatrix<M, FloatDenseVector>> extends
        AbstractDenseMatrix<M, FloatDenseVector, float[]> {
    private static final int DEFAULT_OFFSET = 0;

    private static final int DEFAULT_STRIDE = 1;

    private static final int ELEMENT_SIZE = 1;

    public static FloatBuffer createFloatBuffer(final int size, final Storage storage) {
        if (storage.equals(Storage.DIRECT)) {
            final ByteBuffer buffer = ByteBuffer.allocateDirect(size * ELEMENT_SIZE * Constants.FLOAT_BYTES);
            buffer.order(ByteOrder.nativeOrder());
            return buffer.asFloatBuffer();
        } else {
            return FloatBuffer.allocate(size * ELEMENT_SIZE);
        }
    }

    protected transient FloatBuffer data;

    protected transient FloatBLAS blas;

    /**
     * Constructor for matrix with existing data.
     */
    public AbstractFloatDense(final FloatBuffer data, final int rows, final int columns, final int offset,
            final int stride, final Orientation orientation) {
        super(ELEMENT_SIZE, rows, columns, offset, stride, orientation);
        this.data = data;
        setBlas();
    }

    /**
     * Constructor for vector with existing data.
     */
    public AbstractFloatDense(final FloatBuffer data, final int size, final int offset, final int stride,
            final Orientation orientation) {
        this(data, VectorSupport.rows(size, orientation), VectorSupport.columns(size, orientation), offset, stride,
                orientation);
    }

    /**
     * Constructor for new matrix.
     */
    public AbstractFloatDense(final int rows, final int columns, final Orientation orientation, final Storage storage) {
        super(ELEMENT_SIZE, rows, columns, DEFAULT_OFFSET, DEFAULT_STRIDE, orientation);
        this.data = createFloatBuffer(size, storage);
        setBlas();
    }

    /**
     * Constructor for new vector.
     */
    public AbstractFloatDense(final int size, final Orientation orientation, final Storage storage) {
        this(VectorSupport.rows(size, orientation), VectorSupport.columns(size, orientation), orientation, storage);
    }

    @Override
    public final FloatDenseVector column(final int column) {
        checkColumnIndex(column);
        return new FloatDenseVector(data, rows, columnOffset(column), rowStride, Orientation.COLUMN);
    }

    @Override
    protected final float[] createArray(final int length) {
        return new float[length];
    }

    @Override
    protected final float[][] createArrayArray(final int length) {
        return new float[length][];
    }

    @Override
    public final FloatDenseVector createColumnVector() {
        return new FloatDenseVector(rows, Orientation.COLUMN, storage());
    }

    @Override
    public final FloatDenseVector createRowVector() {
        return new FloatDenseVector(columns, Orientation.ROW, storage());
    }

    public final FloatBuffer data() {
        return data;
    }

    public final float[] dataArray() {
        return data.array();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof AbstractFloatDense)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (new EqualsBuilder().appendSuper(super.equals(obj)).isEquals()) {
            return false;
        }
        // TODO optimize this
        AbstractFloatDense<?> other = (AbstractFloatDense<?>) obj;
        for (int i = 0; i < size; i++) {
            if (get(i) != other.get(i)) {
                return false;
            }
        }
        return true;
    }

    public final void fill(final float value) {
        FloatBuffer xdata = createFloatBuffer(1, storage());
        FloatDenseVector x = new FloatDenseVector(xdata, size, 0, 0, Orientation.DEFAULT_FOR_VECTOR);
        xdata.put(0, value);
        blas.copy(x, asVector());
    }

    @Override
    protected final void fillFrom(final float[] dest, final int srcPos) {
        Arrays.fill(dest, data.get(srcPos));
    }

    public final float get(final int index) {
        return data.get(elementOffset(index));
    }

    public final float get(final int row, final int column) {
        return data.get(elementOffset(row, column));
    }

    public final boolean hasArray() {
        return data.hasArray();
    }

    public final boolean isDirect() {
        return data.isDirect();
    }

    public final int offset() {
        return offset;
    }

    private void setBlas() {
        // TODO might want to using ACML or other versions of BLAS too,
        // depending on system properties
        if (data.isDirect()) {
            blas = MKLFloatBLAS.getInstance();
        } else {
            blas = JavaFloatBLAS.getInstance();
        }
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        Storage storage = (Storage) in.readObject();
        this.data = createFloatBuffer(size, storage);
        setBlas();
        // TODO this stuff can fail when there are offsets and strides involved
        for (int i = 0; i < size; i++) {
            data.put(i, in.readFloat());
        }
    }

    @Override
    public final FloatDenseVector row(final int row) {
        return new FloatDenseVector(data, columns, rowOffset(row), columnStride, Orientation.ROW);
    }

    public final void set(final int index, final float value) {
        data.put(elementOffset(index), value);
    }

    public final void set(final int row, final int column, final float value) {
        data.put(elementOffset(row, column), value);
    }

    public final void setColumn(final int column, final FloatVector<?> columnVector) {
        // TODO this code is almost identical to setRow
        checkArgument(rows == columnVector.size());
        int targetOffset = columnOffset(column);
        int targetStride = rowStride;
        // TODO this could be optimized
        for (int i = 0; i < rows; i++) {
            data.put(targetOffset + i * targetStride, columnVector.get(i));
        }
    }

    @Override
    protected final void setFrom(final float[] dest, final int destPos, final int srcPos) {
        dest[destPos] = data.get(srcPos);
    }

    public final void setRow(final int row, final FloatVector<?> rowVector) {
        // TODO this code is almost identical to setColumn
        checkArgument(columns == rowVector.size());
        int targetOffset = rowOffset(row);
        int targetStride = columnStride;
        // TODO this could be optimized
        for (int i = 0; i < columns; i++) {
            data.put(targetOffset + i * targetStride, rowVector.get(i));
        }
    }

    public final Storage storage() {
        return data.isDirect() ? Storage.DIRECT : Storage.JAVA;
    }

    public final int stride() {
        return stride;
    }

    public final void timesEquals(final float value) {
        for (int i = 0; i < size; i++) {
            set(i, get(i) * value);
        }
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(storage());
        // TODO ensure data is written so that when it is read, trans can be set
        // to Transpose.NORMAL
        // TODO optimize this
        for (int i = 0; i < size; i++) {
            out.writeFloat(get(i));
        }
    }

    @Override
    public String toString() {
        // TODO do something better here
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < size; i++) {
            builder.append(String.format("%.16f ", get(i)));
        }
        builder.append("]");
        return builder.toString();
    }

    public final FloatMatrix<?, ?> times(final FloatMatrix<?, ?> matrix) {
        if (!(matrix instanceof FloatDenseMatrix)) {
            throw new UnsupportedOperationException();
        }
        FloatDenseMatrix a = (FloatDenseMatrix) this;
        FloatDenseMatrix b = (FloatDenseMatrix) matrix;
        FloatDenseMatrix c = new FloatDenseMatrix(rows, b.columns, orientation, storage());
        blas.gemm(1.0f, a, b, 0.0f, c);
        // TODO return a vector if possible
        return c;
    }

    // TODO this method should be static outside the matrix class
    public final void fillRandom(final Random rng) {
        for (int i = 0; i < size; i++) {
            set(i, rng.nextFloat());
        }
    }

    public final float dot(final FloatVector<?> other) {
        if (!(other instanceof FloatDenseVector)) {
            throw new UnsupportedOperationException();
        }
        return blas.dot((FloatDenseVector) this, (FloatDenseVector) other);
    }
}
