package net.lunglet.primme;

import com.sun.jna.Structure;

/**
 * <CODE>
 * typedef struct primme_stats {<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;int numOuterIterations;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;int numRestarts;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;int numMatvecs;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;int numPreconds;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;double elapsedTime;<br>
 * } primme_stats;<br>
 * </CODE>
 */
public final class Stats extends Structure {
    public int numOuterIterations;

    public int numRestarts;

    public int numMatvecs;

    public int numPreconds;

    public double elapsedTime;
}
