package net.lunglet.primme;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import com.googlecode.array4j.Constants;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.blas.FloatDenseBLAS;
import com.googlecode.array4j.matrix.dense.DenseFactory;
import com.googlecode.array4j.matrix.dense.FloatDenseMatrix;
import com.googlecode.array4j.matrix.dense.FloatDenseVector;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.DoubleBuffer;
import net.lunglet.primme.PRIMMELibrary.MatrixMatvecCallback;
import net.lunglet.util.BufferUtils;
import org.junit.Test;

// XXX anorm scales the convergence tolerance

public final class PRIMMETest {
    private FloatDenseMatrix readLUNDA() throws IOException {
        InputStream in = this.getClass().getResourceAsStream("LUNDA.mtx");
        assertNotNull(in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = reader.readLine();
        assertNotNull(line);
        String[] parts = line.trim().split("\\s+");
        int rows = Integer.valueOf(parts[0]);
        assertEquals(147, rows);
        int columns = Integer.valueOf(parts[1]);
        assertEquals(147, columns);
        int elements = Integer.valueOf(parts[2]);
        assertEquals(2449, elements);
        FloatDenseMatrix matrix = DenseFactory.createFloatMatrix(rows, columns);
        for (int i = 0; i < elements; i++) {
            line = reader.readLine();
            assertNotNull(line);
            parts = line.trim().split("\\s+");
            int row = Integer.valueOf(parts[0]) - 1;
            int column = Integer.valueOf(parts[1]) - 1;
            float value = Float.valueOf(parts[2]);
            matrix.set(row, column, value);
        }
        return matrix;
    }

    @Test
    public void testEverything() throws IOException {
        final FloatDenseMatrix matrix = readLUNDA();
//        final FloatDenseMatrix matrix = DenseFactory.createFloatMatrix(7, 7);
//        for (int i = 0; i < matrix.rows(); i++) {
//            matrix.set(i, i, 1.0f + i);
//        }

        PRIMMEParams params = new PRIMMEParams();
        PRIMMELibrary.INSTANCE.primme_initialize(params);
        params.n = matrix.rows();
        params.numEvals = 3;
        params.preconditioner = null;
        params.target = Target.largest.ordinal();
        params.printLevel = 5;
        params.aNorm = 0.0;
        params.eps = 1.0e-6;

        int ret = PRIMMELibrary.INSTANCE.dprimme(null, null, null, params);
        assertEquals(1, ret);
//        assertEquals(2192, params.realWorkSize.intValue());
//        assertEquals(104, params.intWorkSize);
        params.matrixMatvec = new MatrixMatvecCallback() {
            @Override
            public void callback(final Pointer x, final Pointer y, final IntByReference blockSize,
                    final PRIMMEParams params) {
                DoubleBuffer xbuf = x.getByteBuffer(0, Constants.DOUBLE_BYTES * params.n).asDoubleBuffer();
                DoubleBuffer ybuf = y.getByteBuffer(0, Constants.DOUBLE_BYTES * params.n).asDoubleBuffer();
                FloatDenseVector xx = DenseFactory.createFloatVector(params.n);
                for (int i = 0; i < xx.length(); i++) {
                    xx.set(i, (float) xbuf.get(i));
                }
                FloatDenseVector yy = DenseFactory.createFloatVector(params.n);
                FloatDenseBLAS.DEFAULT.gemv(1.0f, matrix, xx, 0.0f, yy);
                for (int i = 0; i < yy.length(); i++) {
                    ybuf.put(i, (double) yy.get(i));
                }
            }
        };
        ret = PRIMMELibrary.INSTANCE.primme_set_method(PresetMethod.DYNAMIC, params);
        assertEquals(0, ret);
//        PRIMMELibrary.INSTANCE.primme_display_params(params);
//        assertEquals(7, params.maxBasisSize);
//        assertEquals(3, params.minRestartSize);
//        assertEquals(1, params.maxBlockSize);
        assertEquals(Integer.MAX_VALUE, params.maxOuterIterations);
        assertEquals(Integer.MAX_VALUE, params.maxMatvecs);
        assertEquals(RestartScheme.thick.ordinal(), params.restartingParams.scheme);
        assertEquals(1, params.restartingParams.maxPrevRetain);
        assertEquals(ConvergenceTest.adaptive_ETolerance.ordinal(), params.correctionParams.convTest);

        DoubleBuffer evals = BufferUtils.createDoubleBuffer(params.numEvals, Storage.DIRECT);
        int evecsSize = params.n * (params.numEvals + params.maxBlockSize);
        DoubleBuffer evecs = BufferUtils.createDoubleBuffer(evecsSize, Storage.DIRECT);
        DoubleBuffer resNorms = BufferUtils.createDoubleBuffer(params.numEvals, Storage.DIRECT);
        ret = PRIMMELibrary.INSTANCE.dprimme(evals, evecs, resNorms, params);
        assertEquals(0, ret);
        PRIMMELibrary.INSTANCE.primme_Free(params);
//        for (int i = 0; i < evals.capacity(); i++) {
//            System.out.println("eigenvalue = " + evals.get(i));
//        }
    }

    @Test
    public void testConvergeTestOrdinals() {
        assertEquals(ConvergenceTest.full_LTolerance.ordinal(), 0);
        assertEquals(ConvergenceTest.decreasing_LTolerance.ordinal(), 1);
        assertEquals(ConvergenceTest.adaptive_ETolerance.ordinal(), 2);
        assertEquals(ConvergenceTest.adaptive.ordinal(), 3);
    }

    @Test
    public void testCorrectionParams() {
        assertEquals(48, new CorrectionParams().size());
        Field[] fields = CorrectionParams.class.getDeclaredFields();
        String[] names = {"precondition", "robustShifts", "maxInnerIterations", "projectors", "convTest", "relTolBase"};
        assertEquals(names.length, fields.length);
        for (int i = 0; i < names.length; i++) {
            assertEquals(names[i], fields[i].getName());
        }
    }

    @Test
    public void testJDProjectors() {
        assertEquals(24, new JDProjectors().size());
        Field[] fields = JDProjectors.class.getDeclaredFields();
        String[] names = {"leftQ", "leftX", "rightQ", "rightX", "skewQ", "skewX"};
        assertEquals(names.length, fields.length);
        for (int i = 0; i < names.length; i++) {
            assertEquals(names[i], fields[i].getName());
        }
    }

    @Test
    public void testPresetMethodOrdinals() {
        assertEquals(PresetMethod.DYNAMIC.ordinal(), 0);
        assertEquals(PresetMethod.DEFAULT_MIN_TIME.ordinal(), 1);
        assertEquals(PresetMethod.DEFAULT_MIN_MATVECS.ordinal(), 2);
        assertEquals(PresetMethod.Arnoldi.ordinal(), 3);
        assertEquals(PresetMethod.GD.ordinal(), 4);
        assertEquals(PresetMethod.GD_plusK.ordinal(), 5);
        assertEquals(PresetMethod.GD_Olsen_plusK.ordinal(), 6);
        assertEquals(PresetMethod.JD_Olsen_plusK.ordinal(), 7);
        assertEquals(PresetMethod.RQI.ordinal(), 8);
        assertEquals(PresetMethod.JDQR.ordinal(), 9);
        assertEquals(PresetMethod.JDQMR.ordinal(), 10);
        assertEquals(PresetMethod.JDQMR_ETol.ordinal(), 11);
        assertEquals(PresetMethod.SUBSPACE_ITERATION.ordinal(), 12);
        assertEquals(PresetMethod.LOBPCG_OrthoBasis.ordinal(), 13);
        assertEquals(PresetMethod.LOBPCG_OrthoBasis_Window.ordinal(), 14);
    }

    @Test
    public void testPRIMMEParams() {
        // TODO this value might depend on the compiler
        assertEquals(248, new PRIMMEParams().size());
        Field[] fields = PRIMMEParams.class.getDeclaredFields();
        String[] names = {"n", "matrixMatvec", "applyPreconditioner", "massMatrixMatvec", "numProcs", "procID",
                "nLocal", "commInfo", "globalSumDouble", "numEvals", "target", "numTargetShifts", "targetShifts",
                "dynamicMethodSwitch", "locking", "initSize", "numOrthoConst", "maxBasisSize", "minRestartSize",
                "maxBlockSize", "maxMatvecs", "maxOuterIterations", "intWorkSize", "realWorkSize", "iseed", "intWork",
                "realWork", "aNorm", "eps", "printLevel", "outputFile", "matrix", "preconditioner",
                "shiftsForPreconditioner", "restartingParams", "correctionParams", "stats", "stackTrace"};
        assertEquals(names.length, fields.length);
        for (int i = 0; i < names.length; i++) {
            assertEquals(names[i], fields[i].getName());
        }
    }

    @Test
    public void testRestartingParams() {
        assertEquals(8, new RestartingParams().size());
        Field[] fields = RestartingParams.class.getDeclaredFields();
        String[] names = {"scheme", "maxPrevRetain"};
        assertEquals(names.length, fields.length);
        for (int i = 0; i < names.length; i++) {
            assertEquals(names[i], fields[i].getName());
        }
    }

    @Test
    public void testRestartSchemeOrdinals() {
        assertEquals(RestartScheme.thick.ordinal(), 0);
        assertEquals(RestartScheme.dtr.ordinal(), 1);
    }

    @Test
    public void testStats() {
        assertEquals(24, new Stats().size());
        Field[] fields = Stats.class.getDeclaredFields();
        String[] names = {"numOuterIterations", "numRestarts", "numMatvecs", "numPreconds", "elapsedTime"};
        assertEquals(names.length, fields.length);
        for (int i = 0; i < names.length; i++) {
            assertEquals(names[i], fields[i].getName());
        }
    }

    @Test
    public void testTargetOrdinals() {
        assertEquals(Target.smallest.ordinal(), 0);
        assertEquals(Target.largest.ordinal(), 1);
        assertEquals(Target.closest_geq.ordinal(), 2);
        assertEquals(Target.closest_leq.ordinal(), 3);
        assertEquals(Target.closest_abs.ordinal(), 4);
    }
}
