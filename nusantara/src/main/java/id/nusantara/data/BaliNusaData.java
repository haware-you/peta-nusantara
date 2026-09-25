package id.nusantara.data;

import id.nusantara.model.Island;
import id.nusantara.model.Kingdom;
import id.nusantara.model.YearRange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Polities of Bali & Nusa Tenggara. Seeded with one entry; extend as detail grows. */
public final class BaliNusaData {

    public static final String GELGEL = "balinusa.gelgel";

    private BaliNusaData() {
    }

    public static List<Kingdom> load() {
        List<Kingdom> kingdoms = new ArrayList<Kingdom>();
        kingdoms.add(gelgel());
        return Collections.unmodifiableList(kingdoms);
    }

    private static Kingdom gelgel() {
        return Kingdom.builder(GELGEL, "Kerajaan Gelgel", Island.BALI_NUSA)
                .span(new YearRange(1380, 1686, true))
                .capital("Gelgel, Klungkung")
                .religion("Hindu Bali")
                .summary("Pewaris tatanan Majapahit di Bali setelah keruntuhannya di Jawa.")
                .build();
    }
}
