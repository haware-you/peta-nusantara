package id.nusantara.events;

import id.nusantara.data.MalukuData;
import id.nusantara.data.SumatraData;
import id.nusantara.model.Island;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Events that cross island boundaries and therefore belong to no single
 * island file. This is the file that justifies a separate events package.
 */
public final class NusantaraEvents {

    public static final String PAMALAYU = "event.pamalayu";
    public static final String PORTUGIS_MALAKA = "event.portugis-malaka";

    private NusantaraEvents() {
    }

    public static List<HistoricalEvent> load() {
        List<HistoricalEvent> events = new ArrayList<HistoricalEvent>();
        events.add(ekspedisiPamalayu());
        events.add(jatuhnyaMalaka());
        return Collections.unmodifiableList(events);
    }

    private static HistoricalEvent ekspedisiPamalayu() {
        return HistoricalEvent.builder(PAMALAYU, "Ekspedisi Pamalayu", EventType.EKSPEDISI)
                .years(1275, 1293)
                .participants("jawa.singhasari", SumatraData.MELAYU_DHARMASRAYA)
                .islands(Island.JAWA, Island.SUMATRA)
                .location("Jawa Timur ke Batanghari, Sumatra")
                .outcome("Melayu masuk ke dalam orbit pengaruh Jawa; ditandai arca Amoghapasa.")
                .summary("Ekspedisi Kertanegara ke Sumatra, ditafsirkan beragam sebagai "
                        + "penaklukan atau sebagai persekutuan melawan ancaman Mongol.")
                .build();
    }

    private static HistoricalEvent jatuhnyaMalaka() {
        return HistoricalEvent.builder(PORTUGIS_MALAKA, "Jatuhnya Malaka ke tangan Portugis", EventType.PENAKLUKAN)
                .year(1511)
                .participants("semenanjung.malaka", MalukuData.TERNATE, SumatraData.ACEH)
                .islands(Island.SEMENANJUNG, Island.SUMATRA, Island.MALUKU)
                .location("Malaka")
                .outcome("Jalur rempah beralih; Aceh dan Ternate naik sebagai penantang.")
                .summary("Penaklukan Malaka oleh Afonso de Albuquerque mengubah peta "
                        + "perdagangan Nusantara dan memicu perlawanan di beberapa kawasan sekaligus.")
                .build();
    }
}
