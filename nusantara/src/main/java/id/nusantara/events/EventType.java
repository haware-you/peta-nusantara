package id.nusantara.events;

/** Classification of a {@link HistoricalEvent}, used for filtering and icons. */
public enum EventType {
    PENDIRIAN("Pendirian"),
    PERANG("Perang / pertempuran"),
    PEMBERONTAKAN("Pemberontakan"),
    PERJANJIAN("Perjanjian"),
    PERNIKAHAN_POLITIK("Pernikahan politik"),
    EKSPEDISI("Ekspedisi"),
    PENAKLUKAN("Penaklukan"),
    KERUNTUHAN("Keruntuhan"),
    PENOBATAN("Penobatan"),
    KEAGAMAAN("Peristiwa keagamaan"),
    PEMBANGUNAN("Pembangunan"),
    DIPLOMASI("Diplomasi"),
    BENCANA("Bencana alam");

    private final String displayName;

    EventType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
