package id.nusantara.registry;

import id.nusantara.data.BaliNusaData;
import id.nusantara.data.JawaData;
import id.nusantara.data.KalimantanData;
import id.nusantara.data.MalukuData;
import id.nusantara.data.PapuaData;
import id.nusantara.data.SulawesiData;
import id.nusantara.data.SumatraData;
import id.nusantara.events.EventRegistry;
import id.nusantara.events.HistoricalEvent;
import id.nusantara.model.Island;
import id.nusantara.model.Kingdom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Single entry point over every island file plus the event registry. Adding an
 * island is one data class and one line in {@link #load()}.
 */
public final class NusantaraRegistry {

    private final List<Kingdom> kingdoms;
    private final Map<String, Kingdom> byId;
    private final EventRegistry events;

    private NusantaraRegistry(List<Kingdom> kingdoms, EventRegistry events) {
        this.kingdoms = Collections.unmodifiableList(kingdoms);
        this.events = events;
        Map<String, Kingdom> index = new LinkedHashMap<String, Kingdom>();
        for (Kingdom kingdom : kingdoms) {
            index.put(kingdom.getId(), kingdom);
        }
        this.byId = Collections.unmodifiableMap(index);
    }

    public static NusantaraRegistry load() {
        List<Kingdom> all = new ArrayList<Kingdom>();
        all.addAll(JawaData.load());
        all.addAll(SumatraData.load());
        all.addAll(KalimantanData.load());
        all.addAll(SulawesiData.load());
        all.addAll(BaliNusaData.load());
        all.addAll(MalukuData.load());
        all.addAll(PapuaData.load());
        return new NusantaraRegistry(all, EventRegistry.load());
    }

    public List<Kingdom> allKingdoms() {
        return kingdoms;
    }

    public EventRegistry events() {
        return events;
    }

    /** Returns {@code null} when the id is unknown, e.g. a participant not yet modelled. */
    public Kingdom kingdom(String id) {
        return byId.get(id);
    }

    public List<Kingdom> byIsland(Island island) {
        List<Kingdom> result = new ArrayList<Kingdom>();
        for (Kingdom kingdom : kingdoms) {
            if (kingdom.getIsland() == island) {
                result.add(kingdom);
            }
        }
        return result;
    }

    /** Resolves an event's participant ids to kingdoms, skipping unknown ids. */
    public List<Kingdom> participantsOf(HistoricalEvent event) {
        List<Kingdom> result = new ArrayList<Kingdom>();
        for (String id : event.getParticipantIds()) {
            Kingdom kingdom = byId.get(id);
            if (kingdom != null) {
                result.add(kingdom);
            }
        }
        return result;
    }

    /** Participant ids with no kingdom behind them -- a to-do list for the data files. */
    public List<String> unresolvedParticipantIds() {
        List<String> missing = new ArrayList<String>();
        for (HistoricalEvent event : events.all()) {
            for (String id : event.getParticipantIds()) {
                if (!byId.containsKey(id) && !missing.contains(id)) {
                    missing.add(id);
                }
            }
        }
        return missing;
    }
}
