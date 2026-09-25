package id.nusantara.model;

/**
 * An inclusive span of years. Supports open-ended and approximate spans, both
 * common in Nusantara sources where a founding or fall date is contested.
 */
public final class YearRange {

    /** Sentinel for a bound that is unknown or still open. */
    public static final int UNKNOWN = Integer.MIN_VALUE;

    private final int start;
    private final int end;
    private final boolean approximate;

    public YearRange(int start, int end) {
        this(start, end, false);
    }

    public YearRange(int start, int end, boolean approximate) {
        this.start = start;
        this.end = end;
        this.approximate = approximate;
    }

    /** A span covering a single year. */
    public static YearRange of(int year) {
        return new YearRange(year, year);
    }

    /** A span whose end is unknown or ongoing. */
    public static YearRange from(int start) {
        return new YearRange(start, UNKNOWN);
    }

    /** A span flagged as approximate ("circa"). */
    public static YearRange circa(int start, int end) {
        return new YearRange(start, end, true);
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public boolean isApproximate() {
        return approximate;
    }

    public boolean hasKnownStart() {
        return start != UNKNOWN;
    }

    public boolean hasKnownEnd() {
        return end != UNKNOWN;
    }

    public boolean contains(int year) {
        boolean afterStart = !hasKnownStart() || year >= start;
        boolean beforeEnd = !hasKnownEnd() || year <= end;
        return afterStart && beforeEnd;
    }

    /**
     * True when this span shares at least one year with {@code other}. This is
     * the predicate a timeline slider uses: given the slider window, keep every
     * entity that overlaps it.
     */
    public boolean overlaps(YearRange other) {
        if (other == null) {
            return false;
        }
        boolean thisBeforeOther = hasKnownEnd() && other.hasKnownStart() && end < other.start;
        boolean otherBeforeThis = other.hasKnownEnd() && hasKnownStart() && other.end < start;
        return !thisBeforeOther && !otherBeforeThis;
    }

    /** Length in years, or -1 when either bound is unknown. */
    public int lengthInYears() {
        if (!hasKnownStart() || !hasKnownEnd()) {
            return -1;
        }
        return end - start + 1;
    }

    @Override
    public String toString() {
        String prefix = approximate ? "c. " : "";
        String startText = hasKnownStart() ? String.valueOf(start) : "?";
        String endText = hasKnownEnd() ? String.valueOf(end) : "sekarang";
        if (hasKnownStart() && hasKnownEnd() && start == end) {
            return prefix + startText;
        }
        return prefix + startText + "-" + endText;
    }
}
