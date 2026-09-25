package id.nusantara.model;

/**
 * A primary or secondary source backing a fact. Contested episodes (Bubat being
 * the clearest case) carry several sources that disagree, so narratives are
 * attributed rather than flattened into one account.
 */
public final class Source {

    public enum Kind {
        NASKAH("Naskah / kidung"),
        PRASASTI("Prasasti"),
        BABAD("Babad / kronik"),
        CATATAN_ASING("Catatan asing"),
        ARKEOLOGI("Temuan arkeologi"),
        MODERN("Kajian modern");

        private final String displayName;

        Kind(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    private final String title;
    private final Kind kind;
    private final String origin;
    private final String note;

    public Source(String title, Kind kind, String origin, String note) {
        this.title = title;
        this.kind = kind;
        this.origin = origin;
        this.note = note;
    }

    public static Source of(String title, Kind kind, String origin) {
        return new Source(title, kind, origin, null);
    }

    public String getTitle() {
        return title;
    }

    public Kind getKind() {
        return kind;
    }

    /** Where the source comes from, e.g. "Jawa Timur", "Sunda", "Tiongkok". */
    public String getOrigin() {
        return origin;
    }

    /** Optional caveat on reliability or bias. */
    public String getNote() {
        return note;
    }

    @Override
    public String toString() {
        return title + " (" + kind.getDisplayName() + (origin == null ? "" : ", " + origin) + ")";
    }
}
