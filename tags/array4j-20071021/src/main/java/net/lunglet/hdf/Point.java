package net.lunglet.hdf;

import java.util.Arrays;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public final class Point {
    private final long[] coords;

    public Point(final long... coords) {
        this.coords = new long[coords.length];
        System.arraycopy(coords, 0, this.coords, 0, coords.length);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof Point)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        Point other = (Point) obj;
        return new EqualsBuilder().append(coords, other.coords).isEquals();
    }

    public long[] getCoordinates() {
        return Arrays.copyOf(coords, coords.length);
    }

    public int getNDims() {
        return coords.length;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(coords).toHashCode();
    }

    @Override
    public String toString() {
        return Arrays.toString(coords);
    }
}
