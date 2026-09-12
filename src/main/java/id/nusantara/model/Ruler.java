package id.nusantara.model;

/** A monarch or regent of a {@link Kingdom}. */
public final class Ruler {

    private final String name;
    private final String title;
    private final YearRange reign;
    private final String note;

    public Ruler(String name, String title, YearRange reign, String note) {
        this.name = name;
        this.title = title;
        this.reign = reign;
        this.note = note;
    }

    public static Ruler of(String name, String title, int from, int to) {
        return new Ruler(name, title, new YearRange(from, to), null);
    }

    public String getName() {
        return name;
    }

    /** Regnal title, e.g. "Sri Maharaja", "Prabu", "Sultan". */
    public String getTitle() {
        return title;
    }

    public YearRange getReign() {
        return reign;
    }

    public String getNote() {
        return note;
    }

    public String getFullName() {
        return (title == null ? "" : title + " ") + name;
    }

    @Override
    public String toString() {
        return getFullName() + " (" + reign + ")";
    }
}
