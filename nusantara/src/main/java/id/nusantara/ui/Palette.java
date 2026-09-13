package id.nusantara.ui;

import id.nusantara.events.EventType;
import id.nusantara.model.Island;
import javafx.scene.paint.Color;

import java.util.EnumMap;
import java.util.Map;

/**
 * Warna yang dipasang dari kode, bukan dari CSS.
 *
 * <p>Warna pulau dan jenis peristiwa bergantung pada data, sehingga tidak bisa
 * ditulis sebagai aturan CSS statis. Token netral (latar, garis, teks) tetap
 * hidup di {@code nusantara.css}.
 */
public final class Palette {

    private static final Map<Island, Color> ISLAND_LIGHT = new EnumMap<>(Island.class);
    private static final Map<Island, Color> ISLAND_DARK = new EnumMap<>(Island.class);
    private static final Map<EventType, Color> TYPE_LIGHT = new EnumMap<>(EventType.class);
    private static final Map<EventType, Color> TYPE_DARK = new EnumMap<>(EventType.class);

    private static boolean dark = false;

    static {
        ISLAND_LIGHT.put(Island.JAWA, Color.web("#A8761F"));
        ISLAND_LIGHT.put(Island.SUMATRA, Color.web("#2C7263"));
        ISLAND_LIGHT.put(Island.KALIMANTAN, Color.web("#6E609B"));
        ISLAND_LIGHT.put(Island.SULAWESI, Color.web("#9C4A38"));
        ISLAND_LIGHT.put(Island.BALI_NUSA, Color.web("#3F6E96"));
        ISLAND_LIGHT.put(Island.MALUKU, Color.web("#777C28"));
        ISLAND_LIGHT.put(Island.PAPUA, Color.web("#8D5279"));
        ISLAND_LIGHT.put(Island.SEMENANJUNG, Color.web("#516B51"));

        ISLAND_DARK.put(Island.JAWA, Color.web("#C4933F"));
        ISLAND_DARK.put(Island.SUMATRA, Color.web("#459885"));
        ISLAND_DARK.put(Island.KALIMANTAN, Color.web("#9A8CCB"));
        ISLAND_DARK.put(Island.SULAWESI, Color.web("#C4705A"));
        ISLAND_DARK.put(Island.BALI_NUSA, Color.web("#6CA0CC"));
        ISLAND_DARK.put(Island.MALUKU, Color.web("#9BA146"));
        ISLAND_DARK.put(Island.PAPUA, Color.web("#B87BA3"));
        ISLAND_DARK.put(Island.SEMENANJUNG, Color.web("#7EA07E"));

        TYPE_LIGHT.put(EventType.PENDIRIAN, Color.web("#2C7263"));
        TYPE_LIGHT.put(EventType.PERANG, Color.web("#A33223"));
        TYPE_LIGHT.put(EventType.PEMBERONTAKAN, Color.web("#A65A22"));
        TYPE_LIGHT.put(EventType.PERJANJIAN, Color.web("#3F6E96"));
        TYPE_LIGHT.put(EventType.PERNIKAHAN_POLITIK, Color.web("#8D5279"));
        TYPE_LIGHT.put(EventType.EKSPEDISI, Color.web("#6E609B"));
        TYPE_LIGHT.put(EventType.PENAKLUKAN, Color.web("#8D3055"));
        TYPE_LIGHT.put(EventType.KERUNTUHAN, Color.web("#5A5F63"));
        TYPE_LIGHT.put(EventType.PENOBATAN, Color.web("#8A6212"));
        TYPE_LIGHT.put(EventType.KEAGAMAAN, Color.web("#3F7A5E"));
        TYPE_LIGHT.put(EventType.PEMBANGUNAN, Color.web("#777C28"));
        TYPE_LIGHT.put(EventType.DIPLOMASI, Color.web("#8A6212"));
        TYPE_LIGHT.put(EventType.BENCANA, Color.web("#9C4A38"));

        TYPE_DARK.put(EventType.PENDIRIAN, Color.web("#4FA893"));
        TYPE_DARK.put(EventType.PERANG, Color.web("#E07A63"));
        TYPE_DARK.put(EventType.PEMBERONTAKAN, Color.web("#DC9153"));
        TYPE_DARK.put(EventType.PERJANJIAN, Color.web("#6CA0CC"));
        TYPE_DARK.put(EventType.PERNIKAHAN_POLITIK, Color.web("#B87BA3"));
        TYPE_DARK.put(EventType.EKSPEDISI, Color.web("#9A8CCB"));
        TYPE_DARK.put(EventType.PENAKLUKAN, Color.web("#CB6D96"));
        TYPE_DARK.put(EventType.KERUNTUHAN, Color.web("#8B9096"));
        TYPE_DARK.put(EventType.PENOBATAN, Color.web("#D6AB4E"));
        TYPE_DARK.put(EventType.KEAGAMAAN, Color.web("#5FA87F"));
        TYPE_DARK.put(EventType.PEMBANGUNAN, Color.web("#9BA146"));
        TYPE_DARK.put(EventType.DIPLOMASI, Color.web("#D6AB4E"));
        TYPE_DARK.put(EventType.BENCANA, Color.web("#C4705A"));
    }

    private Palette() {
    }

    public static boolean isDark() {
        return dark;
    }

    public static void setDark(boolean value) {
        dark = value;
    }

    public static Color of(Island island) {
        Color color = (dark ? ISLAND_DARK : ISLAND_LIGHT).get(island);
        return color == null ? Color.web("#7C7E77") : color;
    }

    public static Color of(EventType type) {
        Color color = (dark ? TYPE_DARK : TYPE_LIGHT).get(type);
        return color == null ? Color.web("#7C7E77") : color;
    }

    /** Warna teks netral, dipakai untuk simpul yang digambar langsung. */
    public static Color ink() {
        return dark ? Color.web("#E6E4DA") : Color.web("#22241F");
    }

    public static Color surface() {
        return dark ? Color.web("#1C1F23") : Color.web("#F3F1E7");
    }

    public static Color contested() {
        return dark ? Color.web("#E07A63") : Color.web("#A33223");
    }

    public static Color scrub() {
        return dark ? Color.web("#8FBEE4") : Color.web("#2E4A63");
    }

    /**
     * Warna untuk gaya inline CSS dengan tingkat kepekatan.
     *
     * <p>CSS JavaFX tidak menerima heksadesimal delapan digit ({@code #rrggbbaa});
     * deklarasi seperti itu dianggap tidak sah dan dibuang tanpa peringatan.
     */
    public static String rgba(Color color, double alpha) {
        return String.format("rgba(%d,%d,%d,%.2f)",
                Math.round(color.getRed() * 255),
                Math.round(color.getGreen() * 255),
                Math.round(color.getBlue() * 255),
                alpha);
    }

    public static String hex(Color color) {
        return String.format("#%02X%02X%02X",
                Math.round(color.getRed() * 255),
                Math.round(color.getGreen() * 255),
                Math.round(color.getBlue() * 255));
    }
}
