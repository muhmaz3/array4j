package net.lunglet.primme;

import com.sun.jna.Structure;

/**
 * Restarting parameters.
 * <p>
 * <CODE>
 * typedef struct restarting_params {<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;primme_restartscheme scheme;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;int maxPrevRetain;<br>
 * } restarting_params;
 * </CODE>
 */
public final class RestartingParams extends Structure {
    /** Restart scheme. */
    int scheme;

    /**
     * Number of approximations from previous iteration to be retained after
     * restart.
     * <p>
     * This is recurrence based restarting (see GD+1, LOBPCG, etc). If
     * <CODE>maxPrevRetain > 0</CODE>, then the restart size will be
     * <CODE>minRestartSize + maxPrevRetain</CODE>.
     */
    int maxPrevRetain;
}
