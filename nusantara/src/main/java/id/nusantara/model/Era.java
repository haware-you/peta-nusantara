package id.nusantara.model;

/**
 * Periodisation used for labelling and filtering only. Deliberately NOT the
 * unit of file organisation: kingdoms such as Majapahit (1293-1527) straddle
 * era boundaries, so era is derived from a year rather than owning entities.
 */
public enum Era {
    PRA_SEJARAH("Pra-sejarah", Integer.MIN_VALUE, 0),
    HINDU_BUDDHA_AWAL("Hindu-Buddha Awal", 1, 900),
    HINDU_BUDDHA_AKHIR("Hindu-Buddha Akhir", 901, 1300),
    MAJAPAHIT_DAN_SEMASA("Majapahit dan Semasa", 1301, 1500),
    KESULTANAN_ISLAM("Kesultanan Islam", 1501, 1600),
    KOLONIAL_AWAL("Kolonial Awal (VOC)", 1601, 1800),
    KOLONIAL_AKHIR("Kolonial Akhir", 1801, 1942),
    PENDUDUKAN_JEPANG("Pendudukan Jepang", 1942, 1945),
    MODERN("Modern", 1945, Integer.MAX_VALUE);

    private final String displayName;
    private final int startYear;
    private final int endYear;

    Era(String displayName, int startYear, int endYear) {
        this.displayName = displayName;
        this.startYear = startYear;
        this.endYear = endYear;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getStartYear() {
        return startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public boolean contains(int year) {
        return year >= startYear && year <= endYear;
    }

    /** Returns the era a given year falls in, or {@code null} if none matches. */
    public static Era of(int year) {
        for (Era era : values()) {
            if (era.contains(year)) {
                return era;
            }
        }
        return null;
    }
}
