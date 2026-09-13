package id.nusantara.data;

import id.nusantara.model.Island;
import id.nusantara.model.Kingdom;
import id.nusantara.model.Ruler;
import id.nusantara.model.Source;
import id.nusantara.model.YearRange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Polities of Sumatra. */
public final class SumatraData {

    public static final String SRIWIJAYA = "sumatra.sriwijaya";
    public static final String ACEH = "sumatra.aceh";
    public static final String MELAYU_DHARMASRAYA = "sumatra.melayu";

    private SumatraData() {
    }

    public static List<Kingdom> load() {
        List<Kingdom> kingdoms = new ArrayList<Kingdom>();
        kingdoms.add(sriwijaya());
        kingdoms.add(melayuDharmasraya());
        kingdoms.add(aceh());
        return Collections.unmodifiableList(kingdoms);
    }

    private static Kingdom sriwijaya() {
        return Kingdom.builder(SRIWIJAYA, "Sriwijaya", Island.SUMATRA)
                .span(new YearRange(671, 1288, true))
                .capital("Palembang (kemudian mungkin Jambi)")
                .religion("Buddha Mahayana")
                .summary("Kekuatan maritim penguasa Selat Malaka dan pusat pembelajaran Buddha. "
                        + "Letak ibu kotanya masih diperdebatkan.")
                .source(Source.of("Prasasti Kedukan Bukit", Source.Kind.PRASASTI, "Palembang"))
                .source(new Source("Catatan I-Tsing", Source.Kind.CATATAN_ASING, "Tiongkok",
                        "Kesaksian langsung tentang Sriwijaya sebagai pusat studi Buddha."))
                .build();
    }

    private static Kingdom melayuDharmasraya() {
        return Kingdom.builder(MELAYU_DHARMASRAYA, "Melayu Dharmasraya", Island.SUMATRA)
                .span(new YearRange(1183, 1347, true))
                .capital("Dharmasraya, Batanghari")
                .religion("Buddha")
                .summary("Sasaran ekspedisi Pamalayu Singhasari; kemudian terkait erat "
                        + "dengan wangsa Majapahit.")
                .build();
    }

    private static Kingdom aceh() {
        return Kingdom.builder(ACEH, "Kesultanan Aceh Darussalam", Island.SUMATRA)
                .span(1496, 1903)
                .capital("Banda Aceh")
                .religion("Islam")
                .ruler(Ruler.of("Iskandar Muda", "Sultan", 1607, 1636))
                .summary("Kekuatan Islam terkuat di Sumatra; penantang utama Portugis di Malaka.")
                .build();
    }
}
