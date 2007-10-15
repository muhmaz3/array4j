package net.lunglet.primme;

/**
 * Restart scheme.
 */
public enum RestartScheme {
    /**
     * Thick restarting.
     * <p>
     * This is the most efficient and robust in the general case.
     */
    thick,
    /**
     * Dynamic thick restarting.
     * <p>
     * Helpful without preconditioning but it is expensive to implement.
     */
    dtr
}
