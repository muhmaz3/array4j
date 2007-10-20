package net.lunglet.primme;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public final class PRIMMETest {
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
}
