package id.nusantara.timeline;

import id.nusantara.events.EventRegistry;
import id.nusantara.events.HistoricalEvent;
import id.nusantara.model.Era;
import id.nusantara.model.Island;
import id.nusantara.model.Kingdom;
import id.nusantara.registry.NusantaraRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * Chronological view over the geographically-partitioned data. Files are split
 * by island; this class supplies the orthogonal time axis, so neither view has
 * to compromise the other.
 *
 * <p>The slider talks to this class through
 * {@link #eventsIn(TimelineWindow)} and {@link #kingdomsIn(TimelineWindow)}.
 */
public final class Timeline {

    private final NusantaraRegistry registry;

    public Timeline(NusantaraRegistry registry) {
        this.registry = registry;
    }

    public static Timeline of(NusantaraRegistry registry) {
        return new Timeline(registry);
    }

    /** A window spanning everything in the dataset -- the slider's initial state. */
    public TimelineWindow fullWindow() {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (Kingdom kingdom : registry.allKingdoms()) {
            if (kingdom.getSpan() != null && kingdom.getSpan().hasKnownStart()) {
                min = Math.min(min, kingdom.getSpan().getStart());
            }
            if (kingdom.getSpan() != null && kingdom.getSpan().hasKnownEnd()) {
                max = Math.max(max, kingdom.getSpan().getEnd());
            }
        }
        for (HistoricalEvent event : registry.events().all()) {
            if (event.getWhen() != null && event.getWhen().hasKnownStart()) {
                min = Math.min(min, event.getWhen().getStart());
                max = Math.max(max, event.getWhen().getEnd());
            }
        }
        if (min > max) {
            return TimelineWindow.full(0, 2000);
        }
        return TimelineWindow.full(min, max);
    }

    /** Events overlapping the slider window, in chronological order. */
    public List<HistoricalEvent> eventsIn(TimelineWindow window) {
        return eventsBetween(window.getStart(), window.getEnd());
    }

    public List<HistoricalEvent> eventsBetween(int fromYear, int toYear) {
        List<HistoricalEvent> result = new ArrayList<HistoricalEvent>();
        for (HistoricalEvent event : registry.events().all()) {
            if (event.occurredBetween(fromYear, toYear)) {
                result.add(event);
            }
        }
        return result;
    }

    /** Kingdoms alive at any point in the slider window. */
    public List<Kingdom> kingdomsIn(TimelineWindow window) {
        return kingdomsBetween(window.getStart(), window.getEnd());
    }

    public List<Kingdom> kingdomsBetween(int fromYear, int toYear) {
        List<Kingdom> result = new ArrayList<Kingdom>();
        for (Kingdom kingdom : registry.allKingdoms()) {
            if (kingdom.existedBetween(fromYear, toYear)) {
                result.add(kingdom);
            }
        }
        return result;
    }

    /** Kingdoms alive in a single year -- the "snapshot" a scrubbing slider shows. */
    public List<Kingdom> snapshot(int year) {
        List<Kingdom> result = new ArrayList<Kingdom>();
        for (Kingdom kingdom : registry.allKingdoms()) {
            if (kingdom.existedIn(year)) {
                result.add(kingdom);
            }
        }
        return result;
    }

    /** One island's lane in the timeline -- the row a stacked slider view draws. */
    public List<Kingdom> lane(Island island, TimelineWindow window) {
        List<Kingdom> result = new ArrayList<Kingdom>();
        for (Kingdom kingdom : registry.byIsland(island)) {
            if (kingdom.existedBetween(window.getStart(), window.getEnd())) {
                result.add(kingdom);
            }
        }
        return result;
    }

    public List<HistoricalEvent> eventsOf(Era era) {
        return eventsBetween(era.getStartYear(), era.getEndYear());
    }

    /** Every event a kingdom took part in, chronologically. */
    public List<HistoricalEvent> storyOf(String kingdomId) {
        return registry.events().involving(kingdomId);
    }

    public EventRegistry events() {
        return registry.events();
    }

    public NusantaraRegistry registry() {
        return registry;
    }
}
