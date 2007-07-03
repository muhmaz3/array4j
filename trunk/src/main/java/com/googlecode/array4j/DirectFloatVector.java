package com.googlecode.array4j;

import java.nio.FloatBuffer;

import com.googlecode.array4j.internal.ToArraysConverter;

public final class DirectFloatVector extends
        AbstractDenseVector<DirectFloatVector, DirectFloatSupport<DirectFloatVector, DirectFloatVector>, float[]>
        implements FloatVector<DirectFloatVector>, DenseVector<DirectFloatVector> {
    private final FloatBuffer data;

    DirectFloatVector(final FloatBuffer data, final int size, final int offset, final int stride,
            final Orientation orientation) {
        super(size, offset, stride, orientation);
        this.data = data;
        this.support = new DirectFloatSupport<DirectFloatVector, DirectFloatVector>(this, data);
        checkPostcondition(getData().remaining() >= size);
    }

    public DirectFloatVector(final int size) {
        this(size, 0, 1, Orientation.DEFAULT_FOR_VECTOR);
    }

    public DirectFloatVector(final int size, final int offset, final int stride, final Orientation orientation) {
        this(DirectFloatMatrix.createBuffer(size), size, offset, stride, orientation);
    }

    @Override
    protected ToArraysConverter<DirectFloatVector, float[]> createArraysConverter() {
        return new ToArraysConverter<DirectFloatVector, float[]>(this) {
            @Override
            protected float[] createArray(final int length) {
                return new float[length];
            }

            @Override
            protected float[][] createArrayArray(final int length) {
                return new float[length][];
            }

            @Override
            protected void set(final int srcPos, final float[] dest, final int destPos) {
                dest[destPos] = data.get(srcPos);
            }
        };
    }

    public DirectFloatVector createColumnVector() {
        // TODO Auto-generated method stub
        return null;
    }

    public DirectFloatVector createRowVector() {
        // TODO Auto-generated method stub
        return null;
    }

    public DirectFloatVector createVector(int size, int offset, int stride, Orientation orientation) {
        return new DirectFloatVector(getData(), size, offset, stride, orientation);
    }

    private FloatBuffer getData() {
        // TODO code duplicated from DirectFloatMatrix.getData
        return (FloatBuffer) ((FloatBuffer) data.rewind()).position(offset);
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

    public DirectFloatVector transpose() {
        return new DirectFloatVector(getData(), size, offset, stride, orientation.transpose());
    }
}
