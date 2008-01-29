package net.lunglet.primme;

import static org.junit.Assert.assertEquals;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.util.BufferUtils;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import java.lang.reflect.Field;
import java.nio.DoubleBuffer;
import net.lunglet.primme.PRIMMELibrary.MatrixMatvecCallback;
import org.junit.Test;

public final class PRIMMETest {
    @Test
    public void test() {
        int numEvals = 1;
        Target target = Target.largest;
        int n = 3;
        MatrixMatvecCallback callback = new MatrixMatvecCallback() {
            @Override
            public void callback(final Pointer x, final Pointer y, final IntByReference blockSize,
                    final PRIMMEParams params) {
                System.out.println("callback called");
            }
        };
        PRIMMEParams params = new PRIMMEParams(numEvals, target, n, callback);
        PRIMMELibrary.INSTANCE.primme_initialize(params);
        int ret = PRIMMELibrary.INSTANCE.primme_set_method(PresetMethod.DYNAMIC, params);
        assertEquals(0, ret);
        PRIMMELibrary.INSTANCE.primme_display_params(params);
        int evalsSize = params.numEvals;
        DoubleBuffer evals = BufferUtils.createDoubleBuffer(evalsSize, Storage.DIRECT);
        int evecsSize = params.nLocal * params.n;
        DoubleBuffer evecs = BufferUtils.createDoubleBuffer(evecsSize, Storage.DIRECT);
        int resNormsSize = params.numEvals;
        DoubleBuffer resNorms = BufferUtils.createDoubleBuffer(resNormsSize, Storage.DIRECT);
//        int ret = PRIMMELibrary.INSTANCE.dprimme(evals, evecs, resNorms, params);
    }

    @Test
    public void testConvergeTestOrdinals() {
        assertEquals(ConvergenceTest.full_LTolerance.ordinal(), 0);
        assertEquals(ConvergenceTest.decreasing_LTolerance.ordinal(), 1);
        assertEquals(ConvergenceTest.adaptive_ETolerance.ordinal(), 2);
        assertEquals(ConvergenceTest.adaptive.ordinal(), 3);
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
    public void testRestartSchemeOrdinals() {
        assertEquals(RestartScheme.thick.ordinal(), 0);
        assertEquals(RestartScheme.dtr.ordinal(), 1);
    }

    @Test
    public void testTargetOrdinals() {
        assertEquals(Target.smallest.ordinal(), 0);
        assertEquals(Target.largest.ordinal(), 1);
        assertEquals(Target.closest_geq.ordinal(), 2);
        assertEquals(Target.closest_leq.ordinal(), 3);
        assertEquals(Target.closest_abs.ordinal(), 4);
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
}
