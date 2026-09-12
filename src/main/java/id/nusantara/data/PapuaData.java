package id.nusantara.data;

import id.nusantara.model.Island;
import id.nusantara.model.Kingdom;
import id.nusantara.model.YearRange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Polities of Papua. Seeded with one entry; extend as detail grows. */
public final class PapuaData {

    public static final String WAIGEO = "papua.waigeo";

    private PapuaData() {
    }

    public static List<Kingdom> load() {
        List<Kingdom> kingdoms = new ArrayList<Kingdom>();
        kingdoms.add(waigeo());
        return Collections.unmodifiableList(kingdoms);
    }

    private static Kingdom waigeo() {
        return Kingdom.builder(WAIGEO, "Kerajaan Waigeo", Island.PAPUA)
                .span(new YearRange(1500, 1900, true))
                .capital("Waigeo, Raja Ampat")
                .religion("Islam dan kepercayaan setempat")
                .summary("Salah satu dari kerajaan Raja Ampat, lama berada dalam orbit pengaruh Tidore.")
                .build();
    }
}
