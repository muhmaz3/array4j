package net.lunglet.primme;

public enum PresetMethod {
    /** Switches to the best method dynamically. */
    DYNAMIC,
    /** Currently set as JDQMR_ETol. */
    DEFAULT_MIN_TIME,
    /** Currently set as GD_Olsen_plusK. */
    DEFAULT_MIN_MATVECS,
    /** Arnoldi implented a la Generalized Davidson. */
    Arnoldi,
    /** Generalized Davidson. */
    GD,
    /** GD+k with locally optimal restarting for k evals. */
    GD_plusK,
    /** GD+k, preconditioner applied to <CODE>(r+deltaeps*x)</CODE>. */
    GD_Olsen_plusK,
    /** As above, only deltaeps computed as in JD. */
    JD_Olsen_plusK,
    /** (accelerated) Rayleigh Quotient Iteration. */
    RQI,
    /** Jacobi-Davidson with const number of inner steps. */
    JDQR,
    /** JDQMR adaptive stopping criterion for inner QMR. */
    JDQMR,
    /** JDQMR + stops after resid reduces by a 0.1 factor. */
    JDQMR_ETol,
    /** Subspace iteration. */
    SUBSPACE_ITERATION,
    /** A LOBPCG implementation with orthogonal basis. */
    LOBPCG_OrthoBasis,
    /** As above, only finds evals a Window at a time. */
    LOBPCG_OrthoBasis_Window
}
