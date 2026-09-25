package id.nusantara.data;

import id.nusantara.model.Island;
import id.nusantara.model.Kingdom;
import id.nusantara.model.YearRange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Polities of Maluku. Seeded with one entry; extend as detail grows. */
public final class MalukuData {

    public static final String TERNATE = "maluku.ternate";

    private MalukuData() {
    }

    public static List<Kingdom> load() {
        List<Kingdom> kingdoms = new ArrayList<Kingdom>();
        kingdoms.add(ternate());
        return Collections.unmodifiableList(kingdoms);
    }

    private static Kingdom ternate() {
        return Kingdom.builder(TERNATE, "Kesultanan Ternate", Island.MALUKU)
                .span(new YearRange(1257, 1914, true))
                .capital("Ternate")
                .religion("Islam")
                .summary("Pusat perdagangan cengkih; persaingannya dengan Tidore menarik masuk Portugis dan Belanda.")
                .build();
    }
}
