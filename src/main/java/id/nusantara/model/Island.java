package id.nusantara.model;

/**
 * Geographic partition of the dataset. Each island maps to one file in
 * {@code id.nusantara.data}, so adding a region means adding one enum
 * constant and one data class.
 */
public enum Island {
    JAWA("Jawa"),
    SUMATRA("Sumatra"),
    KALIMANTAN("Kalimantan"),
    SULAWESI("Sulawesi"),
    BALI_NUSA("Bali & Nusa Tenggara"),
    MALUKU("Maluku"),
    PAPUA("Papua"),
    SEMENANJUNG("Semenanjung Malaya");

    private final String displayName;

    Island(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
