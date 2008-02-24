package net.lunglet.io;

import static org.junit.Assert.assertEquals;
import com.googlecode.array4j.math.FloatMatrixUtils;
import com.googlecode.array4j.matrix.dense.DenseFactory;
import com.googlecode.array4j.matrix.dense.FloatDenseMatrix;
import com.googlecode.array4j.matrix.dense.FloatDenseVector;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.lunglet.io.MatrixInputStream;
import net.lunglet.io.MatrixOutputStream;
import org.junit.Test;

public final class MatrixIOStreamTest {
    @Test
    public void testWriteColumnsAsMatrix() throws IOException {
        FloatDenseMatrix matrix = DenseFactory.createFloatMatrix(3, 4);
        FloatMatrixUtils.fillRandom(matrix, new Random(0));
        List<FloatDenseVector> columns = new ArrayList<FloatDenseVector>(matrix.columns());
        for (FloatDenseVector column : matrix.columnsIterator()) {
            columns.add(column);
        }
        assertEquals(matrix.columns(), columns.size());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MatrixOutputStream out = new MatrixOutputStream(baos);
        out.writeColumnsAsMatrix(columns);
        out.close();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        MatrixInputStream in = new MatrixInputStream(bais);
        FloatDenseMatrix matrix2 = in.readMatrix();
        assertEquals(matrix.rows(), matrix2.rows());
        assertEquals(matrix.columns(), matrix2.columns());
        assertEquals(matrix, matrix2);
    }

    @Test
    public void testWriteMatrix() throws IOException {
        FloatDenseMatrix matrix = DenseFactory.createFloatMatrix(10, 20);
        FloatMatrixUtils.fillRandom(matrix, new Random(0));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MatrixOutputStream out = new MatrixOutputStream(baos);
        out.writeMatrix(matrix);
        out.close();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        MatrixInputStream in = new MatrixInputStream(bais);
        FloatDenseMatrix matrix2 = in.readMatrix();
        assertEquals(matrix.rows(), matrix2.rows());
        assertEquals(matrix.columns(), matrix2.columns());
        assertEquals(matrix, matrix2);
    }
}
