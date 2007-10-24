package com.googlecode.array4j.dense;

import com.googlecode.array4j.Constants;
import com.googlecode.array4j.Order;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.util.AssertUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

public final class FloatDenseUtils {
    public static FloatDenseMatrix arange(final int rows, final int columns, final Order orientation,
            final Storage storage) {
//        FloatDenseMatrix matrix = new FloatDenseMatrix(rows, columns, orientation, storage);
//        for (int i = 0; i < matrix.length(); i++) {
//            matrix.set(i, (float) i + 1);
//        }
//        return matrix;
        return null;
    }

    public static FloatDenseMatrix createMatrix(final float[]... values) {
        return createMatrix(Order.DEFAULT, Storage.DEFAULT_FOR_DENSE, values);
    }

    // TODO change createMatrix functions to valueOf methods of FloatDenseMatrix

    public static FloatDenseMatrix createMatrix(final Order orientation, final Storage storage,
            final float[]... values) {
        int rows = values.length;
        int columns = rows > 0 ? values[0].length : 0;
//        FloatDenseMatrix matrix = new FloatDenseMatrix(rows, columns, orientation, storage);
        FloatDenseMatrix matrix = null;
        for (int i = 0; i < rows; i++) {
            if (values[i].length != columns) {
                throw new IllegalArgumentException();
            }
            for (int j = 0; j < columns; j++) {
                matrix.set(i, j, values[i][j]);
            }
        }
        return matrix;
    }

    // note that mapped files can't reliably be deleted after having been
    // mapped, so that is why readHTK is also provided
    public static FloatDenseMatrix mapHTK(final File file) throws IOException {
        FileChannel channel = new FileInputStream(file).getChannel();
        ByteBuffer buf = channel.map(MapMode.READ_ONLY, 0, channel.size()).order(ByteOrder.BIG_ENDIAN);
        int nsamples = buf.getInt(0);
        int period = buf.getInt(4);
        int size = buf.getShort(8) & 0xffff;
        int kind = buf.getShort(10) & 0xffff;
        FloatBuffer data = ((ByteBuffer) buf.position(12)).asFloatBuffer();
        if (data.remaining() != nsamples * size / Constants.FLOAT_BYTES) {
            throw new IOException();
        }
        int columns = data.remaining() / nsamples;
//        return new FloatDenseMatrix(data, nsamples, columns, 0, 1, Order.ROW);
        return null;
    }

    public static FloatDenseMatrix mapHTK(final String filename) throws IOException {
        return mapHTK(new File(filename));
    }

    public static FloatDenseMatrix readHTK(final File file) throws IOException {
        FileChannel channel = new FileInputStream(file).getChannel();
        ByteBuffer buf = ByteBuffer.allocate((int) channel.size());
        channel.read(buf);
        channel.close();
        int nsamples = buf.getInt(0);
        int period = buf.getInt(4);
        int size = buf.getShort(8) & 0xffff;
        int kind = buf.getShort(10) & 0xffff;
        FloatBuffer data = ((ByteBuffer) buf.position(12)).asFloatBuffer();
        if (data.remaining() != nsamples * size / Constants.FLOAT_BYTES) {
            throw new IOException();
        }
        int columns = data.remaining() / nsamples;
//        return new FloatDenseMatrix(data, nsamples, columns, 0, 1, Order.ROW);
        return null;
    }

    /**
     * Get a submatrix spanning the column range [column0, column1).
     */
    public static FloatDenseMatrix subMatrixColumns(final FloatDenseMatrix x, final int column0, final int column1) {
        AssertUtils.checkArgument(column0 >= 0 && column0 <= x.columns(), String.format(
            "column0=%d not in range [0, %d]", column0, x.columns()));
        AssertUtils.checkArgument(column1 >= column0 && column1 <= x.columns(), String.format(
            "column1=%d not in range [%d, %d]", column1, column0, x.columns()));
        if (column0 == 0 && column1 == x.columns()) {
            return x;
        }
        int cols = column1 - column0;
        if (x.order().equals(Order.COLUMN)) {
//            return new FloatDenseMatrix(x, x.rows(), cols, x.columnOffset(column0), x.stride, x.order());
            return null;
        } else {
//            FloatDenseMatrix newMatrix = new FloatDenseMatrix(x.rows(), cols, x.order(), x.storage());
//            for (int i = column0, j = 0; i < column1; i++, j++) {
//                newMatrix.setColumn(j, x.column(i));
//            }
//            return newMatrix;
            return null;
        }
    }

    private FloatDenseUtils() {
    }
}
