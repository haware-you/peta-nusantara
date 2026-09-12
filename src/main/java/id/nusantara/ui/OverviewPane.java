package id.nusantara.ui;

import id.nusantara.model.Era;
import id.nusantara.model.Island;
import id.nusantara.model.Kingdom;
import id.nusantara.timeline.TimelineWindow;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

import java.util.ArrayList;
import java.util.List;

/**
 * Peta kecil seluruh rentang dengan kotak pilihan yang bisa diseret.
 *
 * <p>Menggantikan histogram pada rujukan analitik: tinggi batang di sana berarti
 * "seberapa ramai", sedangkan di sini kepadatan hanya menandakan sumber yang
 * selamat. Pita era dipakai sebagai gantinya karena menyampaikan sesuatu yang benar.
 */
public final class OverviewPane extends VBox {

    private static final double STRIP_HEIGHT = 96;
    private static final double BAR_HEIGHT = 4;
    private static final double BAR_GAP = 6;

    private final TimelineModel model;
    private final Pane ribbon = new Pane();
    private final Pane strip = new Pane();
    private final Region brush = new Region();
    private final Region handleLeft = new Region();
    private final Region handleRight = new Region();

    private final List<Label> eraBands = new ArrayList<>();
    private final List<Region> miniBars = new ArrayList<>();
    private final List<Kingdom> barOrder = new ArrayList<>();
    private final List<Label> tickLabels = new ArrayList<>();
    private final List<Region> tickLines = new ArrayList<>();
    private final int[] tickYears = { 500, 1000, 1500 };

    private String dragMode = null;
    private int dragAnchorYear;
    private int dragStart;
    private int dragEnd;

    public OverviewPane(TimelineModel model) {
        this.model = model;
        setSpacing(4);

        ribbon.setPrefHeight(20);
        ribbon.setMinHeight(20);
        strip.setPrefHeight(STRIP_HEIGHT);
        strip.setMinHeight(STRIP_HEIGHT);
        strip.getStyleClass().add("overview-strip");

        brush.getStyleClass().add("brush");
        handleLeft.getStyleClass().add("brush-handle");
        handleRight.getStyleClass().add("brush-handle");
        handleLeft.setPrefSize(9, 30);
        handleRight.setPrefSize(9, 30);

        // Seluruh isi strip ditempatkan sendiri lewat resizeRelocate(). Tanpa
        // setManaged(false), Pane mengembalikan ukuran anaknya ke ukuran pilihan
        // pada tahap tata letak berikutnya -- yaitu nol untuk Region kosong.
        brush.setManaged(false);
        handleLeft.setManaged(false);
        handleRight.setManaged(false);

        // Brush lebih dulu masuk: penanda dan batang disisipkan relatif terhadapnya
        // supaya kotak pilihan selalu berada di lapisan paling atas.
        strip.getChildren().addAll(brush, handleLeft, handleRight);

        buildEraBands();
        buildTicks();
        buildMiniBars();

        getChildren().addAll(ribbon, strip);

        wireDrag();
        model.windowProperty().addListener((obs, old, now) -> layoutBrush());
        strip.widthProperty().addListener((obs, old, now) -> { layoutStrip(); layoutBrush(); });
        strip.heightProperty().addListener((obs, old, now) -> { layoutStrip(); layoutBrush(); });
        ribbon.widthProperty().addListener((obs, old, now) -> layoutRibbon());
    }

    /* ---------- pembangunan ---------- */

    private void buildEraBands() {
        ribbon.getChildren().clear();
        eraBands.clear();
        TimelineWindow full = model.timeline().fullWindow();
        int i = 0;
        for (Era era : Era.values()) {
            int s = Math.max(era.getStartYear(), full.getMinBound());
            int e = Math.min(era.getEndYear(), full.getMaxBound());
            if (e <= s) {
                continue;
            }
            Label band = new Label(era.getDisplayName().toUpperCase());
            band.getStyleClass().add("era-band");
            if (i % 2 == 1) {
                band.getStyleClass().add("era-band-alt");
            }
            band.setTooltip(new Tooltip(era.getDisplayName() + " · "
                    + era.getStartYear() + "–" + era.getEndYear()));
            band.setManaged(false);
            band.getProperties().put("from", s);
            band.getProperties().put("to", e);
            eraBands.add(band);
            ribbon.getChildren().add(band);
            i++;
        }
    }

    private void buildTicks() {
        for (int year : tickYears) {
            Region line = new Region();
            line.setStyle("-fx-background-color: " + (Palette.isDark() ? "#32363B" : "#C9C6B4") + ";");
            line.setPrefWidth(1);
            line.setManaged(false);
            Label label = new Label(String.valueOf(year));
            label.getStyleClass().add("mini-tick-label");
            label.setManaged(false);
            tickLines.add(line);
            tickLabels.add(label);
            int at = strip.getChildren().indexOf(brush);
            strip.getChildren().addAll(at < 0 ? strip.getChildren().size() : at, List.of(line, label));
        }
    }

    private void buildMiniBars() {
        miniBars.forEach(bar -> strip.getChildren().remove(bar));
        miniBars.clear();
        barOrder.clear();
        for (Island island : Island.values()) {
            for (Kingdom kingdom : model.registry().byIsland(island)) {
                Region bar = new Region();
                bar.setStyle(miniBarStyle(island));
                bar.setPrefHeight(BAR_HEIGHT);
                bar.setManaged(false);
                Tooltip.install(bar, new Tooltip(kingdom.getName() + " · " + kingdom.getSpan()));
                miniBars.add(bar);
                barOrder.add(kingdom);
                int at = strip.getChildren().indexOf(brush);
                strip.getChildren().add(at < 0 ? strip.getChildren().size() : at, bar);
            }
        }
    }

    private static String miniBarStyle(Island island) {
        return "-fx-background-color: " + Palette.rgba(Palette.of(island), 0.62) + ";"
                + "-fx-background-radius: 2;";
    }

    /** Menggambar ulang warna setelah tema berganti. */
    public void refreshTheme() {
        for (int i = 0; i < miniBars.size(); i++) {
            miniBars.get(i).setStyle(miniBarStyle(barOrder.get(i).getIsland()));
        }
        for (Region line : tickLines) {
            line.setStyle("-fx-background-color: " + (Palette.isDark() ? "#32363B" : "#C9C6B4") + ";");
        }
    }

    /* ---------- tata letak ---------- */

    private double fraction(int year) {
        TimelineWindow full = model.timeline().fullWindow();
        return (year - full.getMinBound()) / (double) (full.getMaxBound() - full.getMinBound());
    }

    private int yearAt(double x) {
        TimelineWindow full = model.timeline().fullWindow();
        double w = Math.max(1, strip.getWidth());
        double frac = Math.max(0, Math.min(1, x / w));
        return (int) Math.round(full.getMinBound()
                + frac * (full.getMaxBound() - full.getMinBound()));
    }

    private void layoutRibbon() {
        double w = ribbon.getWidth();
        for (Label band : eraBands) {
            int from = (Integer) band.getProperties().get("from");
            int to = (Integer) band.getProperties().get("to");
            double x = fraction(from) * w;
            band.resizeRelocate(x, 0, Math.max(0, fraction(to) * w - x), 20);
        }
    }

    private void layoutStrip() {
        double w = strip.getWidth();
        double h = strip.getHeight();
        for (int i = 0; i < miniBars.size(); i++) {
            Kingdom kingdom = barOrder.get(i);
            double x = fraction(kingdom.getSpan().getStart()) * w;
            double x2 = fraction(kingdom.getSpan().getEnd()) * w;
            miniBars.get(i).resizeRelocate(x, 6 + i * BAR_GAP, Math.max(1, x2 - x), BAR_HEIGHT);
        }
        for (int i = 0; i < tickYears.length; i++) {
            double x = fraction(tickYears[i]) * w;
            tickLines.get(i).resizeRelocate(x, 0, 1, h);
            Label label = tickLabels.get(i);
            label.autosize();
            label.relocate(x + 4, h - label.getHeight() - 2);
        }
    }

    private void layoutBrush() {
        TimelineWindow win = model.window();
        double w = strip.getWidth();
        double h = strip.getHeight();
        double x = fraction(win.getStart()) * w;
        double x2 = fraction(win.getEnd()) * w;
        brush.resizeRelocate(x, 0, Math.max(3, x2 - x), h);
        handleLeft.resizeRelocate(x - 4.5, h / 2 - 15, 9, 30);
        handleRight.resizeRelocate(x2 - 4.5, h / 2 - 15, 9, 30);
        handleLeft.toFront();
        handleRight.toFront();
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        layoutRibbon();
        layoutStrip();
        layoutBrush();
    }

    /* ---------- interaksi ---------- */

    private void wireDrag() {
        handleLeft.setOnMousePressed(e -> { beginDrag("left", e.getX() + handleLeft.getLayoutX()); e.consume(); });
        handleRight.setOnMousePressed(e -> { beginDrag("right", e.getX() + handleRight.getLayoutX()); e.consume(); });
        brush.setOnMousePressed(e -> { beginDrag("pan", e.getX() + brush.getLayoutX()); e.consume(); });
        brush.setCursor(Cursor.OPEN_HAND);

        handleLeft.setOnMouseDragged(this::onDrag);
        handleRight.setOnMouseDragged(this::onDrag);
        brush.setOnMouseDragged(this::onDrag);

        handleLeft.setOnMouseReleased(e -> dragMode = null);
        handleRight.setOnMouseReleased(e -> dragMode = null);
        brush.setOnMouseReleased(e -> dragMode = null);

        strip.setOnMousePressed(e -> {
            if (dragMode != null) {
                return;
            }
            int year = yearAt(e.getX());
            int span = model.window().getSpan();
            model.setRange(year - span / 2, year + span / 2);
        });
    }

    private void beginDrag(String mode, double xInStrip) {
        dragMode = mode;
        dragAnchorYear = yearAt(xInStrip);
        dragStart = model.window().getStart();
        dragEnd = model.window().getEnd();
    }

    private void onDrag(javafx.scene.input.MouseEvent e) {
        if (dragMode == null) {
            return;
        }
        double x = e.getSceneX() - strip.localToScene(0, 0).getX();
        int year = yearAt(x);
        switch (dragMode) {
            case "left" -> model.setRange(Math.min(year, dragEnd - TimelineWindow.MIN_SPAN), dragEnd);
            case "right" -> model.setRange(dragStart, Math.max(year, dragStart + TimelineWindow.MIN_SPAN));
            default -> {
                int delta = year - dragAnchorYear;
                model.setRange(dragStart + delta, dragEnd + delta);
            }
        }
        e.consume();
    }
}
