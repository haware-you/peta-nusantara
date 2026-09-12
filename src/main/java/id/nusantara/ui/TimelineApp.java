package id.nusantara.ui;

import id.nusantara.model.Kingdom;
import id.nusantara.registry.NusantaraRegistry;
import id.nusantara.timeline.TimelineWindow;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.List;

/**
 * Jendela utama Garis Waktu Nusantara.
 *
 * <p>Jalankan dengan JDK yang sudah memuat JavaFX (mis. Azul Zulu FX):
 * {@code java -cp out id.nusantara.ui.TimelineApp}
 */
public final class TimelineApp extends Application {

    private static final int[] ZOOMS = { 500, 200, 100, 50, 20 };

    private TimelineModel model;
    private OverviewPane overview;
    private StagePane stage;
    private EventListPane eventList;
    private DetailPane detail;
    private Label readoutYears;
    private Label readoutSpan;
    private Label snapshotNote;
    private ToggleGroup zoomGroup;

    @Override
    public void start(Stage primaryStage) {
        loadFonts();

        NusantaraRegistry registry = NusantaraRegistry.load();
        model = new TimelineModel(registry);
        model.select(TimelineModel.Kind.EVENT, "event.bubat");

        VBox shell = new VBox(16);
        shell.setPadding(new Insets(24, 24, 32, 24));
        shell.setMaxWidth(1180);

        shell.getChildren().addAll(
                buildMasthead(registry),
                buildOverviewPanel(),
                buildStagePanel(),
                buildSnapshotNote(),
                buildLower(),
                buildFootnote());

        VBox centering = new VBox(shell);
        centering.setAlignment(Pos.TOP_CENTER);

        ScrollPane root = new ScrollPane(centering);
        root.setFitToWidth(true);
        root.getStyleClass().add("detail-scroll");

        Scene scene = new Scene(root, 1200, 900);
        scene.getStylesheets().add(
                TimelineApp.class.getResource("nusantara.css").toExternalForm());

        model.darkModeProperty().addListener((obs, old, now) -> {
            Palette.setDark(now);
            if (now) {
                root.getStyleClass().add("dark");
            } else {
                root.getStyleClass().remove("dark");
            }
            overview.refreshTheme();
            stage.refreshTheme();
            eventList.refreshTheme();
            detail.rebuild();
        });

        model.windowProperty().addListener((obs, old, now) -> updateReadout());
        model.hoverYearProperty().addListener((obs, old, now) -> updateSnapshot());
        model.pinnedYearProperty().addListener((obs, old, now) -> updateSnapshot());
        updateReadout();
        updateSnapshot();

        primaryStage.setTitle("Garis Waktu Nusantara");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Memilih baris di daftar membuat ScrollPane menggulir untuk menampakkannya;
        // kembalikan ke puncak supaya tampilan pertama dimulai dari kepala halaman.
        javafx.application.Platform.runLater(() -> root.setVvalue(0));
    }

    /** Memuat rupa huruf yang dikemas bersama aplikasi; diam saja bila gagal. */
    private void loadFonts() {
        String[] files = {
                "fonts/Marcellus-Regular.ttf",
                "fonts/IBMPlexSans-Regular.ttf",
                "fonts/IBMPlexMono-Regular.ttf"
        };
        for (String file : files) {
            try (InputStream in = TimelineApp.class.getResourceAsStream(file)) {
                if (in != null) {
                    Font.loadFont(in, 12);
                }
            } catch (Exception ignored) {
                // Rupa huruf sistem akan dipakai sebagai gantinya.
            }
        }
    }

    /* ---------- kepala ---------- */

    private Region buildMasthead(NusantaraRegistry registry) {
        Label eyebrow = new Label("KERAJAAN & PERISTIWA DI NUSANTARA");
        eyebrow.getStyleClass().add("eyebrow");
        Label title = new Label("Garis Waktu Nusantara");
        title.getStyleClass().add("display");
        Label lede = new Label("Polity ditata per pulau, bukan per periode — kerajaan menempati "
                + "satu tempat, tetapi jarang muat dalam satu era. Geser rentang untuk membaca waktu.");
        lede.getStyleClass().add("lede");
        lede.setMaxWidth(520);
        VBox left = new VBox(3, eyebrow, title, lede);

        TimelineWindow full = model.timeline().fullWindow();
        Label rangeLabel = new Label("CAKUPAN DATA");
        rangeLabel.getStyleClass().add("eyebrow");
        Label rangeValue = new Label(full.getMinBound() + " – " + full.getMaxBound());
        rangeValue.getStyleClass().addAll("stat-value", "num");
        Label counts = new Label(registry.allKingdoms().size() + " POLITY · "
                + registry.events().all().size() + " PERISTIWA");
        counts.getStyleClass().add("eyebrow");

        ToggleButton theme = new ToggleButton("Tema gelap");
        theme.getStyleClass().add("ghost-button");
        theme.selectedProperty().bindBidirectional(model.darkModeProperty());

        VBox right = new VBox(2, rangeLabel, rangeValue, counts, theme);
        right.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox masthead = new HBox(left, spacer, right);
        masthead.getStyleClass().add("masthead");
        masthead.setAlignment(Pos.BOTTOM_LEFT);
        return masthead;
    }

    /* ---------- ikhtisar ---------- */

    private Region buildOverviewPanel() {
        VBox panel = new VBox();
        panel.getStyleClass().add("panel");

        Label title = new Label("RENTANG PENUH & PILIHAN");
        title.getStyleClass().add("eyebrow");
        Label hint = new Label("Seret kotak untuk menggeser · seret pegangan untuk mengubah rentang");
        hint.getStyleClass().add("eyebrow");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox head = new HBox(title, spacer, hint);
        head.getStyleClass().add("panel-head");
        head.setAlignment(Pos.CENTER_LEFT);

        overview = new OverviewPane(model);
        VBox overviewBox = new VBox(overview);
        overviewBox.setPadding(new Insets(12, 14, 10, 14));

        panel.getChildren().addAll(head, overviewBox, buildControls());
        return panel;
    }

    private Region buildControls() {
        Label label = new Label("RENTANG");
        label.getStyleClass().add("eyebrow");

        HBox zoomSet = new HBox();
        zoomGroup = new ToggleGroup();
        for (int span : ZOOMS) {
            ToggleButton button = new ToggleButton(span + "th");
            button.getStyleClass().add("zoom-button");
            button.setToggleGroup(zoomGroup);
            button.setUserData(span);
            button.setOnAction(e -> model.zoomTo(span));
            zoomSet.getChildren().add(button);
        }

        ToggleButton full = new ToggleButton("Seluruh rentang");
        full.getStyleClass().add("ghost-button");
        full.setOnAction(e -> {
            model.showFullRange();
            full.setSelected(false);
        });

        Label tampak = new Label("TAMPAK");
        tampak.getStyleClass().add("eyebrow");
        readoutYears = new Label();
        readoutYears.getStyleClass().addAll("readout-years", "num");
        readoutSpan = new Label();
        readoutSpan.getStyleClass().addAll("eyebrow", "num");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row = new HBox(12, label, zoomSet, full, spacer, tampak, readoutYears, readoutSpan);
        row.getStyleClass().add("panel-head-bottom");
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private void updateReadout() {
        TimelineWindow win = model.window();
        readoutYears.setText(win.getStart() + " – " + win.getEnd());
        readoutSpan.setText(win.getSpan() + " TAHUN");
        for (javafx.scene.control.Toggle toggle : zoomGroup.getToggles()) {
            boolean match = ((Integer) toggle.getUserData()) == win.getSpan();
            toggle.setSelected(match);
        }
    }

    /* ---------- panggung ---------- */

    private Region buildStagePanel() {
        stage = new StagePane(model);
        return stage;
    }

    private Region buildSnapshotNote() {
        snapshotNote = new Label();
        snapshotNote.getStyleClass().add("snapshot-note");
        snapshotNote.setWrapText(true);
        return snapshotNote;
    }

    private void updateSnapshot() {
        Integer year = model.activeYear();
        if (year == null) {
            snapshotNote.setText("Arahkan kursor ke garis waktu untuk menelusuri tahun; "
                    + "klik untuk mengunci.");
            return;
        }
        List<Kingdom> alive = model.timeline().snapshot(year);
        StringBuilder names = new StringBuilder();
        for (Kingdom kingdom : alive) {
            if (names.length() > 0) {
                names.append(" · ");
            }
            names.append(kingdom.getName());
        }
        snapshotNote.setText("Tahun " + year + " — " + alive.size()
                + " polity hidup berdampingan:  " + names);
    }

    /* ---------- bawah ---------- */

    private Region buildLower() {
        eventList = new EventListPane(model);
        detail = new DetailPane(model);

        GridPane grid = new GridPane();
        grid.setHgap(16);
        ColumnConstraints left = new ColumnConstraints();
        left.setPercentWidth(30);
        ColumnConstraints right = new ColumnConstraints();
        right.setPercentWidth(70);
        grid.getColumnConstraints().addAll(left, right);
        grid.add(eventList, 0, 0);
        grid.add(detail, 1, 0);
        return grid;
    }

    private Region buildFootnote() {
        Label note = new Label("Rentang bertanda c. ditampilkan dengan tepi memudar: tanggalnya "
                + "perkiraan, bukan catatan pasti. Lingkaran putus-putus menandai peristiwa yang "
                + "sumbernya berselisih — setiap versi ditampilkan terpisah beserta sumbernya, tidak "
                + "diringkas menjadi satu cerita. Kepadatan peristiwa di sini mencerminkan sumber "
                + "yang selamat, bukan seberapa banyak yang benar-benar terjadi.");
        note.getStyleClass().add("footnote");
        note.setWrapText(true);
        note.setMaxWidth(820);
        return note;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
