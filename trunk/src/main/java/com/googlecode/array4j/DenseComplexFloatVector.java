package com.googlecode.array4j;

public final class DenseComplexFloatVector
        extends
        AbstractDenseVector<DenseComplexFloatVector, DenseComplexFloatSupport<DenseComplexFloatVector, DenseComplexFloatVector>, ComplexFloat[]>
        implements ComplexFloatVector<DenseComplexFloatVector>, DenseVector<DenseComplexFloatVector> {
    private final float[] data;

    public DenseComplexFloatVector(final float[] data, final int size, final int offset, final int stride,
            final Orientation orientation) {
        super(size, offset, stride, orientation);
        checkArgument(data.length >= 2 * size * stride);
        this.data = data;
        this.support = new DenseComplexFloatSupport<DenseComplexFloatVector, DenseComplexFloatVector>(this, data);
    }

    public DenseComplexFloatVector(final int size, final Orientation orientation) {
        this(new float[2 * size], size, 0, 1, orientation);
    }

    public DenseComplexFloatVector createColumnVector() {
        // TODO Auto-generated method stub
        return null;
    }

    public DenseComplexFloatVector createRowVector() {
        // TODO Auto-generated method stub
        return null;
    }

    public DenseComplexFloatVector createVector(final int size, final int offset, final int stride,
            final Orientation orientation) {
        return new DenseComplexFloatVector(data, size, offset, stride, orientation);
    }

    public void setColumn(final int column, final ComplexFloatVector<?> columnVector) {
        // TODO Auto-generated method stub

    }

    public void setRow(final int row, final ComplexFloatVector<?> rowVector) {
        // TODO Auto-generated method stub

    }

    public ComplexFloat[] toArray() {
        // TODO Auto-generated method stub
        return null;
    }

    public DenseComplexFloatVector transpose() {
        // TODO Auto-generated method stub
        return null;
    }
}
