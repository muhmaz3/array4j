package net.lunglet.primme;

/**
 * Convergence test.
 */
public enum ConvergenceTest {
    /** Stop by iterations only. */
    full_LTolerance,

    /** Stop when <CODE>LinSysResidual < relTolBase^(-outerIterations)</CODE>. */
    decreasing_LTolerance,

    /** JDQMR adaptive (like Notay's JDCG). */
    adaptive_ETolerance,

    /**
     * As in JDQMR adaptive but stop also when
     * <CODE>Eres_(innerIts) < Eres_(0) * 0.1</CODE>.
     */
    adaptive
}
