package com.googlecode.array4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public final class DirectFloatMatrix
        extends
        AbstractDenseMatrix<DirectFloatMatrix, DirectFloatVector, DirectFloatSupport<DirectFloatMatrix, DirectFloatVector>, float[]>
        implements FloatMatrix<DirectFloatMatrix, DirectFloatVector>, DenseMatrix<DirectFloatMatrix, DirectFloatVector> {
    private static final int FLOAT_SIZE = Float.SIZE >>> 3;

    static FloatBuffer createBuffer(final float[] values) {
        final FloatBuffer buffer = createBuffer(values.length * FLOAT_SIZE);
        buffer.put(values);
        return buffer;
    }

    public static FloatBuffer createBuffer(final int size) {
        final ByteBuffer buffer = ByteBuffer.allocateDirect(size * FLOAT_SIZE);
        buffer.order(ByteOrder.nativeOrder());
        return buffer.asFloatBuffer();
    }

    private final FloatBuffer data;

    public DirectFloatMatrix(final float[] data, final int rows, final int columns, final int offset, final int stride,
            final Orientation orientation) {
        this(createBuffer(data), rows, columns, offset, stride, orientation);
    }

    public DirectFloatMatrix(final float[] values, final int rows, final int columns, final Orientation orientation) {
        this(values, rows, columns, 0, 1, orientation);
    }

    private DirectFloatMatrix(final FloatBuffer data, final int rows, final int columns, final int offset,
            final int stride, final Orientation orientation) {
        super(rows, columns, offset, stride, orientation);
        this.data = data;
        this.support = new DirectFloatSupport<DirectFloatMatrix, DirectFloatVector>(this, data);
        checkPostcondition(getData().remaining() >= size);
    }

    public DirectFloatMatrix(final int rows, final int columns) {
        this(rows, columns, Orientation.ROW);
    }

    public DirectFloatMatrix(final int rows, final int columns, final Orientation orientation) {
        this(createBuffer(rows * columns), rows, columns, 0, 1, orientation);
    }

    public DirectFloatVector createColumnVector() {
        return new DirectFloatVector(rows, Orientation.COLUMN);
    }

    public DirectFloatVector createColumnVector(final float... values) {
        checkArgument(values.length == rows);
        return new DirectFloatVector(Orientation.COLUMN, values);
    }

    public DirectFloatVector createRowVector() {
        return new DirectFloatVector(columns, Orientation.ROW);
    }

    public DirectFloatVector createRowVector(final float... values) {
        checkArgument(values.length == columns);
        return new DirectFloatVector(Orientation.ROW, values);
    }

    public DirectFloatVector createVector(final int size, final int offset, final int stride,
            final Orientation orientation) {
        return new DirectFloatVector(getData(), size, offset, stride, orientation);
    }

    private FloatBuffer getData() {
        return (FloatBuffer) data.rewind();
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.support = new DirectFloatSupport<DirectFloatMatrix, DirectFloatVector>(this, data);
    }

    public void setColumn(final int column, final FloatVector<?> columnVector) {
        support.setColumn(column, columnVector);
    }

    public void setRow(final int row, final FloatVector<?> rowVector) {
        support.setRow(row, rowVector);
    }

    public float[] toArray() {
        return support.toArray();
    }

    public DirectFloatMatrix transpose() {
        // interchange columns and rows and change orientation
        return new DirectFloatMatrix(getData(), columns, rows, offset, stride, orientation.transpose());
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }
}
