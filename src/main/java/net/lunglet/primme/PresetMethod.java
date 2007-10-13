package net.lunglet.primme;

public enum PresetMethod {
    DYNAMIC,
    DEFAULT_MIN_TIME,
    DEFAULT_MIN_MATVECS,
    Arnoldi,
    GD,
    GD_plusK,
    GD_Olsen_plusK,
    JD_Olsen_plusK,
    RQI,
    JDQR,
    JDQMR,
    JDQMR_ETol,
    SUBSPACE_ITERATION,
    LOBPCG_OrthoBasis,
    LOBPCG_OrthoBasis_Window
}
