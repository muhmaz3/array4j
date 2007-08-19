package cz.vutbr.fit.speech.phnrec;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

final class MasterLabel {
    final String label;

    final long startTime;

    final long endTime;

    final float score;

    public MasterLabel(final String label, final long startTime, final long endTime, final float score) {
        this.label = label;
        this.startTime = startTime;
        this.endTime = endTime;
        this.score = score;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof MasterLabel)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        MasterLabel other = (MasterLabel) obj;
        return new EqualsBuilder().append(label, other.label).append(startTime, other.startTime).append(endTime,
                other.endTime).append(score, other.score).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(label).append(startTime).append(endTime).append(score).toHashCode();
    }

    @Override
    public String toString() {
        return String.format("%d %d %s %.6f", startTime, endTime, label, score);
    }
}
