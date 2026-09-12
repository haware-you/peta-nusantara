package id.nusantara;

import id.nusantara.events.Account;
import id.nusantara.events.HistoricalEvent;
import id.nusantara.model.Island;
import id.nusantara.model.Kingdom;
import id.nusantara.registry.NusantaraRegistry;
import id.nusantara.timeline.Timeline;
import id.nusantara.timeline.TimelineWindow;

/** Smoke test / demo of the layout. Replace with the real UI entry point later. */
public final class Main {

    public static void main(String[] args) {
        NusantaraRegistry registry = NusantaraRegistry.load();
        Timeline timeline = Timeline.of(registry);

        System.out.println("== Kerajaan per pulau ==");
        for (Island island : Island.values()) {
            System.out.println(island.getDisplayName() + ": " + registry.byIsland(island).size());
        }

        System.out.println();
        System.out.println("== Rentang penuh ==");
        TimelineWindow window = timeline.fullWindow();
        System.out.println(window);

        System.out.println();
        System.out.println("== Jendela slider 1300-1400 ==");
        TimelineWindow zoomed = new TimelineWindow(1300, 1400, window.getMinBound(), window.getMaxBound());
        for (Kingdom kingdom : timeline.kingdomsIn(zoomed)) {
            System.out.println("  " + kingdom);
        }
        for (HistoricalEvent event : timeline.eventsIn(zoomed)) {
            System.out.println("  * " + event + "  (posisi " + String.format("%.2f", zoomed.positionOf(event.getYear())) + ")");
        }

        System.out.println();
        System.out.println("== Perang Bubat, versi sumber ==");
        HistoricalEvent bubat = registry.events().byId("event.bubat");
        System.out.println(bubat.getName() + " -- " + bubat.getWhen() + " @ " + bubat.getLocation());
        System.out.println("Terlibat: " + registry.participantsOf(bubat));
        for (Account account : bubat.getAccounts()) {
            System.out.println("  - " + account.getSource().getTitle() + " [" + account.getPerspective() + "]");
            System.out.println("      " + account.getNarrative());
        }

        System.out.println();
        System.out.println("== Peserta peristiwa yang belum dimodelkan ==");
        System.out.println(registry.unresolvedParticipantIds());
    }
}
