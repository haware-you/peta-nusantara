package id.nusantara.timeline;

/**
 * The visible year range of a timeline slider. Immutable: every pan or zoom
 * returns a new window, so UI state changes are easy to undo and to test
 * without a running widget.
 */
public final class TimelineWindow {

    /** Smallest span the slider may zoom into, in years. */
    public static final int MIN_SPAN = 5;

    private final int start;
    private final int end;
    private final int minBound;
    private final int maxBound;

    public TimelineWindow(int start, int end, int minBound, int maxBound) {
        if (minBound > maxBound) {
            throw new IllegalArgumentException("minBound must not exceed maxBound");
        }
        this.minBound = minBound;
        this.maxBound = maxBound;
        int clampedStart = clamp(Math.min(start, end));
        int clampedEnd = clamp(Math.max(start, end));
        if (clampedEnd - clampedStart < MIN_SPAN) {
            clampedEnd = clamp(clampedStart + MIN_SPAN);
            clampedStart = clamp(clampedEnd - MIN_SPAN);
        }
        this.start = clampedStart;
        this.end = clampedEnd;
    }

    /** A window covering the whole available range. */
    public static TimelineWindow full(int minBound, int maxBound) {
        return new TimelineWindow(minBound, maxBound, minBound, maxBound);
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getMinBound() {
        return minBound;
    }

    public int getMaxBound() {
        return maxBound;
    }

    public int getSpan() {
        return end - start;
    }

    /** Moves the left handle, keeping the right one fixed. */
    public TimelineWindow withStart(int newStart) {
        return new TimelineWindow(Math.min(newStart, end - MIN_SPAN), end, minBound, maxBound);
    }

    /** Moves the right handle, keeping the left one fixed. */
    public TimelineWindow withEnd(int newEnd) {
        return new TimelineWindow(start, Math.max(newEnd, start + MIN_SPAN), minBound, maxBound);
    }

    /** Slides the whole window, preserving its span even at the bounds. */
    public TimelineWindow pan(int deltaYears) {
        int span = getSpan();
        int newStart = clamp(start + deltaYears);
        int newEnd = newStart + span;
        if (newEnd > maxBound) {
            newEnd = maxBound;
            newStart = Math.max(minBound, newEnd - span);
        }
        return new TimelineWindow(newStart, newEnd, minBound, maxBound);
    }

    /**
     * Zooms around the window centre. A {@code factor} below 1 zooms in,
     * above 1 zooms out.
     */
    public TimelineWindow zoom(double factor) {
        int centre = (start + end) / 2;
        int half = Math.max(MIN_SPAN / 2, (int) Math.round(getSpan() * factor / 2.0));
        return new TimelineWindow(centre - half, centre + half, minBound, maxBound);
    }

    /** Recentres on a year, keeping the current span. */
    public TimelineWindow centredOn(int year) {
        return pan(year - (start + end) / 2);
    }

    public boolean contains(int year) {
        return year >= start && year <= end;
    }

    /**
     * Position of a year within the window as 0.0-1.0, for drawing. Values
     * outside the window fall outside that range; clamp at the call site if the
     * renderer needs it.
     */
    public double positionOf(int year) {
        int span = getSpan();
        return span == 0 ? 0.0 : (year - start) / (double) span;
    }

    /** Inverse of {@link #positionOf(int)}: turns a drag position into a year. */
    public int yearAt(double position) {
        return start + (int) Math.round(position * getSpan());
    }

    private int clamp(int year) {
        return Math.max(minBound, Math.min(maxBound, year));
    }

    @Override
    public String toString() {
        return start + " - " + end + " (" + getSpan() + " tahun)";
    }
}
