package com.googlecode.array4j;

import java.util.Arrays;

import com.googlecode.array4j.internal.ToArraysConverter;

public final class DenseComplexFloatVector extends AbstractDenseVector<DenseComplexFloatVector, ComplexFloat[]>
        implements ComplexFloatVector<DenseComplexFloatVector>, DenseVector<DenseComplexFloatVector> {
    private final transient DenseComplexFloatSupport<DenseComplexFloatVector, DenseComplexFloatVector> complexSupport;

    private final float[] data;

    public DenseComplexFloatVector(final float[] data, final int size, final int offset, final int stride,
            final Orientation orientation) {
        super(size, offset, stride, orientation);
        checkArgument(data.length >= 2 * size * stride);
        this.data = data;
        this.complexSupport = new DenseComplexFloatSupport<DenseComplexFloatVector, DenseComplexFloatVector>(this, data);
    }

    public DenseComplexFloatVector(final int size, final Orientation orientation) {
        this(new float[2 * size], size, 0, 1, orientation);
    }

    void copyTo(final float[] target, final int targetOffset, final int targetStride) {
        for (int i = 0; i < size; i++) {
            int srcpos = targetOffset + i * targetStride;
            int targetpos = offset + i * stride;
            target[srcpos] = data[targetpos];
            target[srcpos + 1] = data[targetpos + 1];
        }
    }

    @Override
    protected ToArraysConverter<DenseComplexFloatVector, ComplexFloat[]> createArraysConverter() {
        // TODO Auto-generated method stub
        return null;
    }

    public DenseComplexFloatVector createColumnVector() {
        return new DenseComplexFloatVector(rows, Orientation.COLUMN);
    }

    public DenseComplexFloatVector createRowVector() {
        return new DenseComplexFloatVector(columns, Orientation.ROW);
    }

    public DenseComplexFloatVector createSharingVector(final int size, final int offset, final int stride,
            final Orientation orientation) {
        return new DenseComplexFloatVector(data, size, offset, stride, orientation);
    }

    public void setColumn(final int column, final ComplexFloatVector columnVector) {
        complexSupport.setColumn(column, columnVector);
    }

    public void setRow(final int row, final ComplexFloatVector rowVector) {
        complexSupport.setRow(row, rowVector);
    }

    public ComplexFloat[] toArray() {
        return complexSupport.toArray();
    }

    @Override
    public String toString() {
        return Arrays.toString(toArray());
    }

    public DenseComplexFloatVector transpose() {
        return new DenseComplexFloatVector(data, size, offset, stride, orientation.transpose());
    }
}
