package cz.vutbr.fit.speech.phnrec;

public final class Segment {
    final long startTime;

    final long endTime;

    public Segment(final long startTime, final long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public boolean contains(final long timestamp) {
        return timestamp >= startTime && timestamp <= endTime;
    }

    @Override
    public String toString() {
        return String.format("%d %d", startTime, endTime);
    }
}
