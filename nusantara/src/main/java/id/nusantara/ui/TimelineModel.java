package id.nusantara.ui;

import id.nusantara.events.HistoricalEvent;
import id.nusantara.model.Kingdom;
import id.nusantara.registry.NusantaraRegistry;
import id.nusantara.timeline.Timeline;
import id.nusantara.timeline.TimelineWindow;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Keadaan yang dibagi seluruh panel: rentang yang tampak, tahun yang ditelusuri,
 * dan apa yang sedang dipilih.
 *
 * <p>Rentang disimpan sebagai {@link TimelineWindow} yang tidak berubah -- setiap
 * geseran menghasilkan objek baru, sehingga pengamat cukup mendengarkan satu
 * properti untuk mengetahui seluruh perubahan.
 */
public final class TimelineModel {

    /** Apa yang sedang dipilih di panel rincian. */
    public enum Kind { KINGDOM, EVENT }

    /** Pilihan aktif; {@code id} merujuk ke id polity atau id peristiwa. */
    public record Selection(Kind kind, String id) {
    }

    private final NusantaraRegistry registry;
    private final Timeline timeline;

    private final ObjectProperty<TimelineWindow> window;
    private final ObjectProperty<Integer> hoverYear = new SimpleObjectProperty<>(null);
    private final ObjectProperty<Integer> pinnedYear = new SimpleObjectProperty<>(null);
    private final ObjectProperty<Selection> selection = new SimpleObjectProperty<>(null);
    private final BooleanProperty darkMode = new SimpleBooleanProperty(false);

    public TimelineModel(NusantaraRegistry registry) {
        this.registry = registry;
        this.timeline = Timeline.of(registry);
        TimelineWindow full = timeline.fullWindow();
        this.window = new SimpleObjectProperty<>(
                new TimelineWindow(1200, 1600, full.getMinBound(), full.getMaxBound()));
    }

    public NusantaraRegistry registry() {
        return registry;
    }

    public Timeline timeline() {
        return timeline;
    }

    public ObjectProperty<TimelineWindow> windowProperty() {
        return window;
    }

    public TimelineWindow window() {
        return window.get();
    }

    public void setWindow(TimelineWindow value) {
        window.set(value);
    }

    /** Menggeser rentang, menjaga batas dan rentang minimum. */
    public void setRange(int start, int end) {
        TimelineWindow current = window.get();
        window.set(new TimelineWindow(start, end, current.getMinBound(), current.getMaxBound()));
    }

    public void zoomTo(int span) {
        TimelineWindow current = window.get();
        int centre = (current.getStart() + current.getEnd()) / 2;
        setRange(centre - span / 2, centre + span / 2);
    }

    public void showFullRange() {
        TimelineWindow current = window.get();
        setRange(current.getMinBound(), current.getMaxBound());
    }

    public ObjectProperty<Integer> hoverYearProperty() {
        return hoverYear;
    }

    public ObjectProperty<Integer> pinnedYearProperty() {
        return pinnedYear;
    }

    /** Tahun yang sedang dibaca: yang ditunjuk kursor, atau yang dikunci. */
    public Integer activeYear() {
        return hoverYear.get() != null ? hoverYear.get() : pinnedYear.get();
    }

    public ObjectProperty<Selection> selectionProperty() {
        return selection;
    }

    public void select(Kind kind, String id) {
        if (kind == Kind.EVENT) {
            HistoricalEvent event = registry.events().byId(id);
            if (event != null) {
                hoverYear.set(null);
                pinnedYear.set(event.getYear());
            }
        }
        selection.set(new Selection(kind, id));
    }

    public boolean isSelected(Kind kind, String id) {
        Selection current = selection.get();
        return current != null && current.kind() == kind && current.id().equals(id);
    }

    public Kingdom selectedKingdom() {
        Selection current = selection.get();
        return current != null && current.kind() == Kind.KINGDOM
                ? registry.kingdom(current.id())
                : null;
    }

    public HistoricalEvent selectedEvent() {
        Selection current = selection.get();
        return current != null && current.kind() == Kind.EVENT
                ? registry.events().byId(current.id())
                : null;
    }

    public BooleanProperty darkModeProperty() {
        return darkMode;
    }
}
