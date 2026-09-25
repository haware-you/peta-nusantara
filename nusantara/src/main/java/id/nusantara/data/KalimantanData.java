package id.nusantara.data;

import id.nusantara.model.Island;
import id.nusantara.model.Kingdom;
import id.nusantara.model.YearRange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Polities of Kalimantan. Seeded with one entry; extend as detail grows. */
public final class KalimantanData {

    public static final String KUTAI = "kalimantan.kutai";

    private KalimantanData() {
    }

    public static List<Kingdom> load() {
        List<Kingdom> kingdoms = new ArrayList<Kingdom>();
        kingdoms.add(kutai());
        return Collections.unmodifiableList(kingdoms);
    }

    private static Kingdom kutai() {
        return Kingdom.builder(KUTAI, "Kutai Martadipura", Island.KALIMANTAN)
                .span(new YearRange(350, 1605, true))
                .capital("Muara Kaman, Kalimantan Timur")
                .religion("Hindu")
                .summary("Kerajaan Hindu tertua yang diketahui di Nusantara, dikenal dari tujuh yupa berbahasa Sanskerta.")
                .build();
    }
}
