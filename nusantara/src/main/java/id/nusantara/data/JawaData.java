package id.nusantara.data;

import id.nusantara.model.Island;
import id.nusantara.model.Kingdom;
import id.nusantara.model.Ruler;
import id.nusantara.model.Source;
import id.nusantara.model.YearRange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Polities of Jawa. One island, one file. */
public final class JawaData {

    public static final String MAJAPAHIT = "jawa.majapahit";
    public static final String SUNDA = "jawa.sunda";
    public static final String SINGHASARI = "jawa.singhasari";
    public static final String KADIRI = "jawa.kadiri";
    public static final String DEMAK = "jawa.demak";

    private JawaData() {
    }

    public static List<Kingdom> load() {
        List<Kingdom> kingdoms = new ArrayList<Kingdom>();
        kingdoms.add(majapahit());
        kingdoms.add(sunda());
        kingdoms.add(singhasari());
        kingdoms.add(kadiri());
        kingdoms.add(demak());
        return Collections.unmodifiableList(kingdoms);
    }

    private static Kingdom majapahit() {
        return Kingdom.builder(MAJAPAHIT, "Majapahit", Island.JAWA)
                .span(1293, 1527)
                .capital("Trowulan, Jawa Timur")
                .religion("Hindu-Buddha (Siwa-Buddha)")
                .summary("Kemaharajaan Jawa Timur yang pada puncaknya di bawah Hayam Wuruk "
                        + "dan Gajah Mada menuntut pengakuan dari sebagian besar Nusantara. "
                        + "Batas kekuasaan sesungguhnya masih diperdebatkan: Nagarakretagama "
                        + "mendaftar wilayah taklukan, tetapi sebagian mungkin sekadar mitra dagang.")
                .ruler(Ruler.of("Raden Wijaya", "Kertarajasa Jayawardhana", 1293, 1309))
                .ruler(Ruler.of("Jayanegara", "Sri Maharaja", 1309, 1328))
                .ruler(Ruler.of("Tribhuwana Wijayatunggadewi", "Sri Maharani", 1328, 1350))
                .ruler(Ruler.of("Hayam Wuruk", "Sri Rajasanagara", 1350, 1389))
                .ruler(Ruler.of("Wikramawardhana", "Sri Maharaja", 1389, 1429))
                .source(Source.of("Nagarakretagama (Desawarnana)", Source.Kind.NASKAH, "Jawa Timur"))
                .source(Source.of("Pararaton", Source.Kind.NASKAH, "Jawa Timur"))
                .source(new Source("Prasasti Kudadu", Source.Kind.PRASASTI, "Jawa Timur",
                        "Menyebut pelarian Raden Wijaya sebelum pendirian Majapahit."))
                .build();
    }

    private static Kingdom sunda() {
        return Kingdom.builder(SUNDA, "Sunda (Sunda-Galuh)", Island.JAWA)
                .span(new YearRange(669, 1579, true))
                .capital("Pakuan Pajajaran (kemudian), sebelumnya Kawali")
                .religion("Hindu-Buddha, kemudian Sunda Wiwitan")
                .summary("Kerajaan Jawa Barat yang bertahan berdampingan dengan Majapahit. "
                        + "Sumber Sunda dan sumber Jawa Timur memberi gambaran yang berbeda "
                        + "tentang hubungan keduanya, terutama sesudah Bubat.")
                .ruler(Ruler.of("Prabu Maharaja Linggabuana", "Prabu", 1350, 1357))
                .ruler(Ruler.of("Mangkubumi Suradipati (Hyang Bunisora)", "Prabu", 1357, 1371))
                .ruler(Ruler.of("Niskala Wastu Kancana", "Prabu", 1371, 1475))
                .ruler(Ruler.of("Sri Baduga Maharaja (Prabu Siliwangi)", "Sri Baduga", 1482, 1521))
                .source(Source.of("Carita Parahyangan", Source.Kind.NASKAH, "Sunda"))
                .source(Source.of("Prasasti Batutulis", Source.Kind.PRASASTI, "Bogor"))
                .build();
    }

    private static Kingdom singhasari() {
        return Kingdom.builder(SINGHASARI, "Singhasari", Island.JAWA)
                .span(1222, 1292)
                .capital("Kutaraja / Singhasari, Jawa Timur")
                .religion("Hindu-Buddha")
                .summary("Pendahulu langsung Majapahit; keruntuhannya di tangan Jayakatwang "
                        + "membuka jalan bagi Raden Wijaya.")
                .ruler(Ruler.of("Ken Arok", "Sri Rajasa", 1222, 1227))
                .ruler(Ruler.of("Kertanegara", "Sri Maharaja", 1268, 1292))
                .source(Source.of("Pararaton", Source.Kind.NASKAH, "Jawa Timur"))
                .build();
    }

    private static Kingdom kadiri() {
        return Kingdom.builder(KADIRI, "Kadiri (Kediri)", Island.JAWA)
                .span(1042, 1222)
                .capital("Daha, Jawa Timur")
                .religion("Hindu")
                .summary("Pusat kesusastraan Jawa Kuno; berakhir setelah kalah dari Ken Arok.")
                .ruler(Ruler.of("Jayabaya", "Sri Maharaja", 1135, 1157))
                .build();
    }

    private static Kingdom demak() {
        return Kingdom.builder(DEMAK, "Kesultanan Demak", Island.JAWA)
                .span(1475, 1554)
                .capital("Demak, Jawa Tengah")
                .religion("Islam")
                .summary("Kesultanan Islam pertama di Jawa; menandai peralihan dari tatanan "
                        + "Hindu-Buddha pesisir ke kesultanan.")
                .ruler(Ruler.of("Raden Patah", "Sultan", 1475, 1518))
                .ruler(Ruler.of("Trenggana", "Sultan", 1521, 1546))
                .build();
    }
}
