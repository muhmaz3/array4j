package net.lunglet.primme;

import com.sun.jna.Structure;

/**
 * <CODE>
 * typedef struct JD_projectors {<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;int LeftQ;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;int LeftX;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;int RightQ;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;int RightX;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;int SkewQ;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;int SkewX;<br>
 * } JD_projectors;
 * </CODE>
 */
public final class JDProjectors extends Structure {
    /** Set if a projector with Q must be applied on the left. */
    public int leftQ;

    /** Set if a projector with X must be applied on the left. */
    public int leftX;

    /** Set if a projector with Q must be applied on the right. */
    public int rightQ;

    /** Set if a projector with X must be applied on the right. */
    public int rightX;

    /** Set if the Q right projector must be skew. */
    public int skewQ;

    /** Set if the X right projector must be skew. */
    public int skewX;
}
