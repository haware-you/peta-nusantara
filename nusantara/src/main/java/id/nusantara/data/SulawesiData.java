package id.nusantara.data;

import id.nusantara.model.Island;
import id.nusantara.model.Kingdom;
import id.nusantara.model.YearRange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Polities of Sulawesi. Seeded with one entry; extend as detail grows. */
public final class SulawesiData {

    public static final String GOWA = "sulawesi.gowa";

    private SulawesiData() {
    }

    public static List<Kingdom> load() {
        List<Kingdom> kingdoms = new ArrayList<Kingdom>();
        kingdoms.add(gowa());
        return Collections.unmodifiableList(kingdoms);
    }

    private static Kingdom gowa() {
        return Kingdom.builder(GOWA, "Gowa-Tallo", Island.SULAWESI)
                .span(new YearRange(1300, 1667, true))
                .capital("Somba Opu, Makassar")
                .religion("Islam (sejak 1605)")
                .summary("Kekuatan maritim Makassar; kekuasaannya dipatahkan Perjanjian Bongaya.")
                .build();
    }
}
