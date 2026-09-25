package id.nusantara.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Aggregates every event file. Adding an event set means adding one line here.
 */
public final class EventRegistry {

    private final List<HistoricalEvent> events;

    private EventRegistry(List<HistoricalEvent> events) {
        this.events = Collections.unmodifiableList(events);
    }

    public static EventRegistry load() {
        List<HistoricalEvent> all = new ArrayList<HistoricalEvent>();
        all.addAll(JawaEvents.load());
        all.addAll(NusantaraEvents.load());
        // Tambahkan berkas peristiwa lain di sini: SumatraEvents, MalukuEvents, ...
        Collections.sort(all, byYear());
        return new EventRegistry(all);
    }

    public static Comparator<HistoricalEvent> byYear() {
        return new Comparator<HistoricalEvent>() {
            public int compare(HistoricalEvent a, HistoricalEvent b) {
                return Integer.valueOf(a.getYear()).compareTo(Integer.valueOf(b.getYear()));
            }
        };
    }

    /** All events, already sorted chronologically. */
    public List<HistoricalEvent> all() {
        return events;
    }

    public HistoricalEvent byId(String id) {
        for (HistoricalEvent event : events) {
            if (event.getId().equals(id)) {
                return event;
            }
        }
        return null;
    }

    public List<HistoricalEvent> involving(String kingdomId) {
        List<HistoricalEvent> result = new ArrayList<HistoricalEvent>();
        for (HistoricalEvent event : events) {
            if (event.involves(kingdomId)) {
                result.add(event);
            }
        }
        return result;
    }

    public List<HistoricalEvent> ofType(EventType type) {
        List<HistoricalEvent> result = new ArrayList<HistoricalEvent>();
        for (HistoricalEvent event : events) {
            if (event.getType() == type) {
                result.add(event);
            }
        }
        return result;
    }

    /** Events the sources disagree about -- worth rendering differently. */
    public List<HistoricalEvent> contested() {
        List<HistoricalEvent> result = new ArrayList<HistoricalEvent>();
        for (HistoricalEvent event : events) {
            if (event.isContested()) {
                result.add(event);
            }
        }
        return result;
    }
}
