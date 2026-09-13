package id.nusantara.ui;

import id.nusantara.events.HistoricalEvent;
import id.nusantara.model.Island;
import id.nusantara.model.Kingdom;
import id.nusantara.timeline.TimelineWindow;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

/**
 * Panggung utama: satu jalur per pulau, satu batang per polity, satu titik per
 * peristiwa.
 *
 * <p>Ini bagian yang tidak ada pada rujukan analitik. Di sana setiap tanda adalah
 * satu saat; di sini sebagian besar data justru berupa rentang, sehingga batang
 * bergaya Gantt harus menjadi lapisan utamanya.
 */
public final class StagePane extends VBox {

    private static final double LABEL_WIDTH = 132;
    private static final double ROW_HEIGHT = 22;
    private static final double DOT_STRIP_HEIGHT = 15;
    private static final double TRACK_PADDING = 7;

    private final TimelineModel model;
    private final Pane axis = new Pane();
    private final VBox lanes = new VBox();
    private final Pane overlay = new Pane();
    private final StackPane stack = new StackPane();
    private final Region scrubLine = new Region();
    private final Label scrubFlag = new Label();
    private final HBox legend = new HBox(15);

    private final List<TrackPane> tracks = new ArrayList<>();

    public StagePane(TimelineModel model) {
        this.model = model;
        getStyleClass().add("panel");

        axis.getStyleClass().add("axis-strip");
        axis.setPrefHeight(26);
        axis.setMinHeight(26);
        axis.setPadding(new Insets(0, 0, 0, LABEL_WIDTH));

        scrubLine.getStyleClass().add("scrub-line");
        scrubLine.setPrefWidth(1);
        scrubLine.setManaged(false);
        scrubFlag.getStyleClass().add("scrub-flag");
        scrubFlag.setManaged(false);
        overlay.getChildren().addAll(scrubLine, scrubFlag);
        overlay.setMouseTransparent(true);
        overlay.setVisible(false);

        stack.getChildren().addAll(lanes, overlay);
        StackPane.setAlignment(lanes, Pos.TOP_LEFT);

        legend.getStyleClass().add("legend-strip");
        legend.setAlignment(Pos.CENTER_LEFT);

        getChildren().addAll(axis, stack, legend);

        rebuild();
        buildLegend();

        model.windowProperty().addListener((obs, old, now) -> {
            rebuild();
            requestLayout();
        });
        model.selectionProperty().addListener((obs, old, now) -> markSelection());
        model.hoverYearProperty().addListener((obs, old, now) -> layoutScrub());
        model.pinnedYearProperty().addListener((obs, old, now) -> layoutScrub());

        stack.setOnMouseMoved(e -> {
            Integer year = yearAt(e.getX());
            model.hoverYearProperty().set(year);
        });
        stack.setOnMouseExited(e -> model.hoverYearProperty().set(null));
        stack.setOnMouseClicked(e -> {
            Integer year = yearAt(e.getX());
            if (year != null) {
                model.pinnedYearProperty().set(year);
                model.hoverYearProperty().set(null);
            }
        });

        axis.widthProperty().addListener((obs, old, now) -> layoutAxis());
        stack.widthProperty().addListener((obs, old, now) -> layoutScrub());
        stack.heightProperty().addListener((obs, old, now) -> layoutScrub());
    }

    private Integer yearAt(double xInStack) {
        double x = xInStack - LABEL_WIDTH;
        double w = stack.getWidth() - LABEL_WIDTH;
        if (x < 0 || x > w || w <= 0) {
            return null;
        }
        TimelineWindow win = model.window();
        return (int) Math.round(win.getStart() + (x / w) * win.getSpan());
    }

    /* ---------- jalur ---------- */

    public void rebuild() {
        lanes.getChildren().clear();
        tracks.clear();
        TimelineWindow win = model.window();
        int index = 0;

        for (Island island : Island.values()) {
            List<Kingdom> visible = model.timeline().lane(island, win);
            List<HistoricalEvent> dots = new ArrayList<>();
            for (HistoricalEvent event : model.registry().events().all()) {
                if (event.getIslands().contains(island)
                        && event.occurredBetween(win.getStart(), win.getEnd())) {
                    dots.add(event);
                }
            }
            if (visible.isEmpty() && dots.isEmpty()) {
                continue;
            }

            HBox lane = new HBox();
            lane.getStyleClass().add("lane");
            if (index % 2 == 1) {
                lane.getStyleClass().add("lane-alt");
            }
            index++;

            VBox labelBox = new VBox(1);
            labelBox.getStyleClass().add("lane-label");
            labelBox.setPrefWidth(LABEL_WIDTH);
            labelBox.setMinWidth(LABEL_WIDTH);
            labelBox.setMaxWidth(LABEL_WIDTH);
            Label name = new Label(island.getDisplayName());
            name.getStyleClass().add("lane-name");
            name.setWrapText(true);
            Label count = new Label(visible.size() + " polity");
            count.getStyleClass().add("lane-count");
            labelBox.getChildren().addAll(name, count);

            TrackPane track = new TrackPane(island, visible, dots);
            track.getStyleClass().add("lane-track");
            HBox.setHgrow(track, Priority.ALWAYS);
            tracks.add(track);

            lane.getChildren().addAll(labelBox, track);
            lanes.getChildren().add(lane);
        }
        markSelection();
    }

    private void markSelection() {
        for (TrackPane track : tracks) {
            track.applySelection();
        }
    }

    /** Menggambar ulang seluruh panggung setelah tema berganti. */
    public void refreshTheme() {
        rebuild();
        buildLegend();
    }

    /* ---------- sumbu ---------- */

    private static int niceStep(int span) {
        int[] candidates = { 1, 2, 5, 10, 20, 25, 50, 100, 200, 250, 500 };
        for (int candidate : candidates) {
            if (span / candidate <= 9) {
                return candidate;
            }
        }
        return 500;
    }

    private void layoutAxis() {
        axis.getChildren().clear();
        TimelineWindow win = model.window();
        double w = axis.getWidth() - LABEL_WIDTH;
        if (w <= 0) {
            return;
        }
        int step = niceStep(win.getSpan());
        int first = (int) (Math.ceil(win.getStart() / (double) step) * step);
        for (int year = first; year <= win.getEnd(); year += step) {
            Label tick = new Label(String.valueOf(year));
            tick.getStyleClass().add("axis-tick");
            tick.setManaged(false);
            double x = LABEL_WIDTH + ((year - win.getStart()) / (double) win.getSpan()) * w;
            tick.resizeRelocate(x, 0, 60, 26);
            axis.getChildren().add(tick);
        }
    }

    private void layoutScrub() {
        Integer year = model.activeYear();
        TimelineWindow win = model.window();
        if (year == null || year < win.getStart() || year > win.getEnd()) {
            overlay.setVisible(false);
            return;
        }
        double w = stack.getWidth() - LABEL_WIDTH;
        if (w <= 0) {
            overlay.setVisible(false);
            return;
        }
        overlay.setVisible(true);
        double x = LABEL_WIDTH + ((year - win.getStart()) / (double) win.getSpan()) * w;
        scrubLine.resizeRelocate(x, 0, 1, stack.getHeight());
        scrubFlag.setText(String.valueOf(year));
        scrubFlag.autosize();
        scrubFlag.relocate(x - scrubFlag.getWidth() / 2, 0);
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        layoutAxis();
        layoutScrub();
    }

    /* ---------- legenda ---------- */

    private void buildLegend() {
        legend.getChildren().clear();
        List<id.nusantara.events.EventType> used = new ArrayList<>();
        for (HistoricalEvent event : model.registry().events().all()) {
            if (!used.contains(event.getType())) {
                used.add(event.getType());
            }
        }
        for (id.nusantara.events.EventType type : used) {
            Circle dot = new Circle(4, Palette.of(type));
            Label label = new Label(type.getDisplayName());
            label.getStyleClass().add("legend-label");
            HBox item = new HBox(6, dot, label);
            item.setAlignment(Pos.CENTER_LEFT);
            legend.getChildren().add(item);
        }

        Region circaSwatch = new Region();
        circaSwatch.setPrefSize(26, 8);
        String faded = Palette.rgba(Palette.ink(), 0.55);
        circaSwatch.setStyle("-fx-background-color: linear-gradient(to right, transparent, "
                + faded + " 26%, " + faded + " 74%, transparent);"
                + "-fx-background-radius: 2;");
        Label circaLabel = new Label("tanggal perkiraan");
        circaLabel.getStyleClass().add("legend-label");
        HBox circa = new HBox(6, circaSwatch, circaLabel);
        circa.setAlignment(Pos.CENTER_LEFT);

        Circle ring = new Circle(4.5, Color.TRANSPARENT);
        ring.setStroke(Palette.contested());
        ring.getStrokeDashArray().addAll(2.0, 2.0);
        Label ringLabel = new Label("sumber berselisih");
        ringLabel.getStyleClass().add("legend-label");
        HBox contested = new HBox(6, ring, ringLabel);
        contested.setAlignment(Pos.CENTER_LEFT);

        legend.getChildren().addAll(circa, contested);
    }

    /** Gradien tepi memudar untuk rentang bertanda <em>c.</em> */
    private static LinearGradient fadeGradient(Color color, double width) {
        double fade = Math.min(11, Math.max(1, width / 4)) / Math.max(1, width);
        return new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.TRANSPARENT),
                new Stop(fade, color),
                new Stop(1 - fade, color),
                new Stop(1, Color.TRANSPARENT));
    }

    /* ---------- satu jalur ---------- */

    private final class TrackPane extends Pane {

        private final Island island;
        private final List<Kingdom> kingdoms;
        private final List<HistoricalEvent> events;
        private final List<Label> bars = new ArrayList<>();
        private final List<Circle> dots = new ArrayList<>();
        private final List<Circle> rings = new ArrayList<>();

        TrackPane(Island island, List<Kingdom> kingdoms, List<HistoricalEvent> events) {
            this.island = island;
            this.kingdoms = kingdoms;
            this.events = events;
            setMinWidth(0);

            for (Kingdom kingdom : kingdoms) {
                Label bar = new Label(kingdom.getName());
                bar.getStyleClass().add("span-bar");
                bar.setMinWidth(0);
                bar.setCursor(Cursor.HAND);
                Tooltip.install(bar, new Tooltip(kingdom.getName() + " · " + kingdom.getSpan()));
                bar.setOnMouseClicked(e -> {
                    model.select(TimelineModel.Kind.KINGDOM, kingdom.getId());
                    e.consume();
                });
                bars.add(bar);
                getChildren().add(bar);
            }

            for (HistoricalEvent event : events) {
                Circle ring = new Circle(7.5, Color.TRANSPARENT);
                ring.setStroke(Palette.of(event.getType()));
                ring.getStrokeDashArray().addAll(2.0, 2.0);
                ring.setVisible(event.isContested());
                ring.setMouseTransparent(true);
                rings.add(ring);
                getChildren().add(ring);

                Circle dot = new Circle(4.5, Palette.of(event.getType()));
                dot.setStroke(Palette.surface());
                dot.setStrokeWidth(1.5);
                dot.setCursor(Cursor.HAND);
                Tooltip.install(dot, new Tooltip(event.getWhen() + " · " + event.getName()
                        + (event.isContested() ? " (sumber berselisih)" : "")));
                dot.setOnMouseClicked(e -> {
                    model.select(TimelineModel.Kind.EVENT, event.getId());
                    e.consume();
                });
                dots.add(dot);
                getChildren().add(dot);
            }
        }

        void applySelection() {
            for (int i = 0; i < kingdoms.size(); i++) {
                Label bar = bars.get(i);
                boolean selected = model.isSelected(TimelineModel.Kind.KINGDOM, kingdoms.get(i).getId());
                bar.getStyleClass().remove("span-bar-selected");
                if (selected) {
                    bar.getStyleClass().add("span-bar-selected");
                }
            }
            for (int i = 0; i < events.size(); i++) {
                boolean selected = model.isSelected(TimelineModel.Kind.EVENT, events.get(i).getId());
                Circle dot = dots.get(i);
                dot.setStroke(selected ? Palette.ink() : Palette.surface());
                dot.setStrokeWidth(selected ? 2 : 1.5);
            }
        }

        @Override
        protected double computePrefHeight(double width) {
            double rows = kingdoms.size() * ROW_HEIGHT;
            double strip = events.isEmpty() ? 0 : DOT_STRIP_HEIGHT;
            return rows + strip + TRACK_PADDING * 2;
        }

        @Override
        protected void layoutChildren() {
            TimelineWindow win = model.window();
            double w = getWidth();
            double span = Math.max(1, win.getSpan());

            for (int i = 0; i < kingdoms.size(); i++) {
                Kingdom kingdom = kingdoms.get(i);
                Label bar = bars.get(i);
                double x1 = Math.max(0, (kingdom.getSpan().getStart() - win.getStart()) / span * w);
                double x2 = Math.min(w, (kingdom.getSpan().getEnd() - win.getStart()) / span * w);
                double barWidth = Math.max(3, x2 - x1);

                boolean clipStart = kingdom.getSpan().getStart() < win.getStart();
                boolean clipEnd = kingdom.getSpan().getEnd() > win.getEnd();
                CornerRadii radii = new CornerRadii(
                        clipStart ? 0 : 2, clipEnd ? 0 : 2, clipEnd ? 0 : 2, clipStart ? 0 : 2, false);

                javafx.scene.paint.Paint paint = kingdom.getSpan().isApproximate()
                        ? fadeGradient(Palette.of(island), barWidth)
                        : Palette.of(island);
                bar.setBackground(new Background(new BackgroundFill(paint, radii, Insets.EMPTY)));
                bar.resizeRelocate(x1, TRACK_PADDING + i * ROW_HEIGHT + 3, barWidth, 16);
            }

            double dotY = TRACK_PADDING + kingdoms.size() * ROW_HEIGHT + 7;
            for (int i = 0; i < events.size(); i++) {
                HistoricalEvent event = events.get(i);
                double x = (event.getYear() - win.getStart()) / span * w;
                dots.get(i).relocate(x - 4.5, dotY - 4.5);
                rings.get(i).relocate(x - 7.5, dotY - 7.5);
            }
        }
    }
}
