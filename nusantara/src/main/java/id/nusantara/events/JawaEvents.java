package id.nusantara.events;

import id.nusantara.data.JawaData;
import id.nusantara.model.Island;
import id.nusantara.model.Source;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Events whose participants are polities of Jawa. */
public final class JawaEvents {

    public static final String BUBAT = "event.bubat";
    public static final String PENDIRIAN_MAJAPAHIT = "event.pendirian-majapahit";
    public static final String SUMPAH_PALAPA = "event.sumpah-palapa";
    public static final String PERANG_PAREGREG = "event.paregreg";

    private JawaEvents() {
    }

    public static List<HistoricalEvent> load() {
        List<HistoricalEvent> events = new ArrayList<HistoricalEvent>();
        events.add(pendirianMajapahit());
        events.add(sumpahPalapa());
        events.add(perangBubat());
        events.add(perangParegreg());
        return Collections.unmodifiableList(events);
    }

    private static HistoricalEvent pendirianMajapahit() {
        return HistoricalEvent.builder(PENDIRIAN_MAJAPAHIT, "Pendirian Majapahit", EventType.PENDIRIAN)
                .year(1293)
                .participants(JawaData.MAJAPAHIT, JawaData.SINGHASARI)
                .islands(Island.JAWA)
                .location("Tarik, tepi Sungai Brantas")
                .outcome("Raden Wijaya dinobatkan sebagai Kertarajasa Jayawardhana.")
                .summary("Setelah memanfaatkan pasukan Mongol untuk menjatuhkan Jayakatwang "
                        + "lalu memukul mundur pasukan itu sendiri, Raden Wijaya mendirikan "
                        + "Majapahit di atas reruntuhan Singhasari.")
                .account(Account.of(
                        Source.of("Pararaton", Source.Kind.NASKAH, "Jawa Timur"),
                        "Menuturkan tipu muslihat Raden Wijaya terhadap pasukan Mongol."))
                .build();
    }

    private static HistoricalEvent sumpahPalapa() {
        return HistoricalEvent.builder(SUMPAH_PALAPA, "Sumpah Palapa", EventType.DIPLOMASI)
                .year(1336)
                .participants(JawaData.MAJAPAHIT)
                .islands(Island.JAWA)
                .location("Balai istana Majapahit")
                .outcome("Menjadi dasar program ekspansi Majapahit di bawah Gajah Mada.")
                .summary("Gajah Mada bersumpah tidak akan menikmati palapa sebelum menyatukan "
                        + "Nusantara. Sumpah ini hanya dikenal dari Pararaton; bentuk dan "
                        + "cakupan aslinya masih diperdebatkan para sejarawan.")
                .account(Account.of(
                        Source.of("Pararaton", Source.Kind.NASKAH, "Jawa Timur"),
                        "Satu-satunya sumber yang memuat naskah sumpah tersebut."))
                .build();
    }

    /**
     * Pasunda Bubat, 1357. The reference case for this package: two polities,
     * three source traditions, and no agreement between them on motive.
     */
    private static HistoricalEvent perangBubat() {
        Source pararaton = new Source("Pararaton", Source.Kind.NASKAH, "Jawa Timur",
                "Menyinggung peristiwa secara singkat, tanpa uraian panjang.");
        Source kidungSunda = new Source("Kidung Sunda / Kidung Sundayana", Source.Kind.NASKAH, "Bali",
                "Uraian paling rinci, tetapi disusun jauh sesudah peristiwa "
                        + "dan bersifat sastra, bukan catatan sezaman.");
        Source caritaParahyangan = new Source("Carita Parahyangan", Source.Kind.NASKAH, "Sunda",
                "Sumber Sunda; menyebut wafatnya sang prabu di Majapahit.");

        return HistoricalEvent.builder(BUBAT, "Perang Bubat (Pasunda Bubat)", EventType.PERANG)
                .year(1357)
                .participants(JawaData.MAJAPAHIT, JawaData.SUNDA)
                .islands(Island.JAWA)
                .location("Lapangan Bubat, dekat ibu kota Majapahit")
                .outcome("Rombongan Sunda tewas, termasuk Prabu Maharaja Linggabuana; "
                        + "Dyah Pitaloka mengakhiri hidupnya. Hubungan Sunda-Majapahit "
                        + "rusak untuk waktu yang lama.")
                .summary("Rombongan Sunda datang ke Majapahit untuk pernikahan Dyah Pitaloka "
                        + "Citraresmi dengan Hayam Wuruk. Gajah Mada menuntut sang putri "
                        + "diserahkan sebagai tanda takluk, bukan sebagai mempelai setara. "
                        + "Penolakan pihak Sunda berakhir dengan pertempuran yang timpang di "
                        + "Lapangan Bubat.")
                .contested(true)
                .account(new Account(pararaton,
                        "Mencatat peristiwa Pasunda Bubat pada masa Hayam Wuruk tanpa "
                                + "menjelaskan panjang lebar sebab maupun tanggung jawabnya.",
                        "Jawa Timur"))
                .account(new Account(kidungSunda,
                        "Menggambarkan tuntutan Gajah Mada atas penyerahan putri sebagai "
                                + "upeti, penolakan rombongan Sunda, pertempuran di Bubat, "
                                + "dan bela pati Dyah Pitaloka.",
                        "Bali / Jawa"))
                .account(new Account(caritaParahyangan,
                        "Dari sisi Sunda: sang prabu berangkat ke Majapahit dan wafat di "
                                + "sana; penekanannya pada kehormatan dan kehilangan, bukan "
                                + "pada ambisi Majapahit.",
                        "Sunda"))
                .build();
    }

    private static HistoricalEvent perangParegreg() {
        return HistoricalEvent.builder(PERANG_PAREGREG, "Perang Paregreg", EventType.PEMBERONTAKAN)
                .years(1404, 1406)
                .participants(JawaData.MAJAPAHIT)
                .islands(Island.JAWA)
                .location("Istana Barat dan Istana Timur Majapahit")
                .outcome("Majapahit menang secara formal tetapi melemah secara permanen.")
                .summary("Perang saudara antara Wikramawardhana dan Bhre Wirabhumi yang "
                        + "menguras kekuatan Majapahit dan mempercepat lepasnya daerah taklukan.")
                .build();
    }
}
