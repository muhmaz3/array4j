package cz.vutbr.fit.speech.phnrec;

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
    public String toString() {
        return String.format("%d %d %s %.6f", startTime, endTime, label, score);
    }
}
