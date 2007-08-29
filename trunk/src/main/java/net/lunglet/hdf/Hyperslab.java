package net.lunglet.hdf;

public final class Hyperslab {
    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof Hyperslab)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        Hyperslab other = (Hyperslab) obj;
//        return new EqualsBuilder().append(coords, other.coords).isEquals();
        return false;
    }

    @Override
    public int hashCode() {
//        return new HashCodeBuilder().append(coords).toHashCode();
        return 0;
    }
}
