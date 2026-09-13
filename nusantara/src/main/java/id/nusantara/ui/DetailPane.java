package id.nusantara.ui;

import id.nusantara.events.Account;
import id.nusantara.events.HistoricalEvent;
import id.nusantara.model.Island;
import id.nusantara.model.Kingdom;
import id.nusantara.model.Ruler;
import id.nusantara.model.Source;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * Panel rincian untuk polity atau peristiwa.
 *
 * <p>Untuk peristiwa yang sumbernya berselisih, setiap versi mendapat tabnya
 * sendiri lengkap dengan catatan keandalan -- perselisihan itu ditampilkan,
 * bukan diringkas menjadi satu cerita.
 */
public final class DetailPane extends VBox {

    private final TimelineModel model;
    private final Label kindLabel = new Label("PERISTIWA");
    private final VBox body = new VBox(12);

    public DetailPane(TimelineModel model) {
        this.model = model;
        getStyleClass().add("panel");

        Label title = new Label("RINCIAN");
        title.getStyleClass().add("eyebrow");
        kindLabel.getStyleClass().add("eyebrow");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox head = new HBox(title, spacer, kindLabel);
        head.getStyleClass().add("panel-head");
        head.setAlignment(Pos.CENTER_LEFT);

        body.setPadding(new Insets(16, 18, 18, 18));
        ScrollPane scroll = new ScrollPane(body);
        scroll.getStyleClass().add("detail-scroll");
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(430);
        VBox.setVgrow(scroll, Priority.ALWAYS);

        getChildren().addAll(head, scroll);

        rebuild();
        model.selectionProperty().addListener((obs, old, now) -> rebuild());
        model.hoverYearProperty().addListener((obs, old, now) -> {
            if (model.selectedKingdom() != null) {
                rebuild();
            }
        });
        model.pinnedYearProperty().addListener((obs, old, now) -> {
            if (model.selectedKingdom() != null) {
                rebuild();
            }
        });
    }

    public void rebuild() {
        body.getChildren().clear();
        Kingdom kingdom = model.selectedKingdom();
        if (kingdom != null) {
            kindLabel.setText("POLITY");
            buildKingdom(kingdom);
            return;
        }
        HistoricalEvent event = model.selectedEvent();
        if (event != null) {
            kindLabel.setText("PERISTIWA");
            buildEvent(event);
            return;
        }
        Label empty = new Label("Pilih batang polity atau titik peristiwa pada garis waktu.");
        empty.getStyleClass().add("body-text");
        body.getChildren().add(empty);
    }

    /* ---------- bagian yang dipakai bersama ---------- */

    private Label chip(String text) {
        Label chip = new Label(text);
        chip.getStyleClass().add("chip");
        return chip;
    }

    private Label solidChip(String text, Color color) {
        Label chip = new Label(text);
        chip.getStyleClass().addAll("chip", "chip-solid");
        // Gaya inline, bukan setBackground(): properti latar yang tidak disebut
        // stylesheet akan dikembalikan ke nilai awal setiap kali CSS dipasang ulang,
        // sehingga latar yang dipasang dari kode ikut terhapus.
        chip.setStyle("-fx-background-color: " + Palette.hex(color) + ";");
        return chip;
    }

    private Label contestedChip() {
        Label chip = new Label("sumber berselisih");
        chip.getStyleClass().addAll("chip", "chip-contested");
        return chip;
    }

    private GridPane factGrid(String[][] pairs) {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(12, 0, 12, 0));
        ColumnConstraints half = new ColumnConstraints();
        half.setPercentWidth(50);
        grid.getColumnConstraints().addAll(half, half);

        for (int i = 0; i < pairs.length; i++) {
            Label key = new Label(pairs[i][0].toUpperCase());
            key.getStyleClass().add("fact-label");
            Label value = new Label(pairs[i][1] == null ? "—" : pairs[i][1]);
            value.getStyleClass().add("fact-value");
            VBox cell = new VBox(2, key, value);
            grid.add(cell, i % 2, i / 2);
        }
        return grid;
    }

    private Region rule() {
        Region line = new Region();
        line.getStyleClass().add("rule-line");
        line.setPrefHeight(1);
        line.setMinHeight(1);
        return line;
    }

    private Label eyebrow(String text) {
        Label label = new Label(text.toUpperCase());
        label.getStyleClass().add("eyebrow");
        return label;
    }

    /* ---------- polity ---------- */

    private void buildKingdom(Kingdom kingdom) {
        Label title = new Label(kingdom.getName());
        title.getStyleClass().add("detail-title");
        title.setWrapText(true);

        FlowPane chips = new FlowPane(7, 7);
        Island island = kingdom.getIsland();
        chips.getChildren().add(solidChip(island.getDisplayName(), Palette.of(island)));
        chips.getChildren().add(chip(kingdom.getSpan().toString()));
        if (kingdom.getSpan().isApproximate()) {
            chips.getChildren().add(chip("tanggal perkiraan"));
        }
        int length = kingdom.getSpan().lengthInYears();
        if (length > 0) {
            chips.getChildren().add(chip(length + " tahun"));
        }

        Label summary = new Label(kingdom.getSummary());
        summary.getStyleClass().add("body-text");

        StringBuilder sources = new StringBuilder();
        for (Source source : kingdom.getSources()) {
            if (sources.length() > 0) {
                sources.append(" · ");
            }
            sources.append(source.getTitle());
        }

        body.getChildren().addAll(title, chips, summary, rule(),
                factGrid(new String[][] {
                        { "Ibu kota", kingdom.getCapital() },
                        { "Kepercayaan", kingdom.getReligion() },
                        { "Sumber", sources.length() == 0 ? "belum dicatat" : sources.toString() }
                }),
                rule());

        if (!kingdom.getRulers().isEmpty()) {
            Integer year = model.activeYear();
            body.getChildren().add(eyebrow(year == null
                    ? "Penguasa"
                    : "Penguasa · yang memerintah pada " + year + " disorot"));

            VBox rulers = new VBox();
            for (Ruler ruler : kingdom.getRulers()) {
                Label reign = new Label(ruler.getReign().toString());
                reign.getStyleClass().add("ruler-reign");
                reign.setMinWidth(92);
                reign.setMaxWidth(92);
                Label name = new Label(ruler.getTitle() == null
                        ? ruler.getName()
                        : ruler.getName() + " · " + ruler.getTitle());
                name.getStyleClass().add("ruler-name");
                name.setWrapText(true);
                HBox row = new HBox(10, reign, name);
                row.getStyleClass().add("ruler-row");
                if (year != null && ruler.getReign().contains(year)) {
                    row.getStyleClass().add("ruler-row-live");
                }
                HBox.setHgrow(name, Priority.ALWAYS);
                rulers.getChildren().add(row);
            }
            body.getChildren().add(rulers);
        }

        List<HistoricalEvent> involved = model.registry().events().involving(kingdom.getId());
        if (!involved.isEmpty()) {
            body.getChildren().add(eyebrow("Terlibat dalam"));
            FlowPane links = new FlowPane(7, 7);
            for (HistoricalEvent event : involved) {
                Button link = new Button(event.getYear() + " · " + event.getName());
                link.getStyleClass().add("ghost-button");
                link.setOnAction(e -> model.select(TimelineModel.Kind.EVENT, event.getId()));
                links.getChildren().add(link);
            }
            body.getChildren().add(links);
        }
    }

    /* ---------- peristiwa ---------- */

    private void buildEvent(HistoricalEvent event) {
        Label title = new Label(event.getName());
        title.getStyleClass().add("detail-title");
        title.setWrapText(true);

        FlowPane chips = new FlowPane(7, 7);
        chips.getChildren().add(chip(event.getWhen().toString()));
        chips.getChildren().add(solidChip(event.getType().getDisplayName(), Palette.of(event.getType())));
        for (Island island : event.getIslands()) {
            chips.getChildren().add(solidChip(island.getDisplayName(), Palette.of(island)));
        }
        if (event.isContested()) {
            chips.getChildren().add(contestedChip());
        }

        Label summary = new Label(event.getSummary());
        summary.getStyleClass().add("body-text");

        StringBuilder participants = new StringBuilder();
        for (String id : event.getParticipantIds()) {
            if (participants.length() > 0) {
                participants.append(" · ");
            }
            Kingdom kingdom = model.registry().kingdom(id);
            participants.append(kingdom == null ? id + " (belum dimodelkan)" : kingdom.getName());
        }

        body.getChildren().addAll(title, chips, summary, rule(),
                factGrid(new String[][] {
                        { "Tempat", event.getLocation() },
                        { "Pihak terlibat", participants.toString() },
                        { "Akibat", event.getOutcome() }
                }),
                rule());

        if (event.getAccounts().isEmpty()) {
            Label none = new Label("Belum ada kutipan sumber yang dicatat untuk peristiwa ini.");
            none.getStyleClass().add("footnote");
            body.getChildren().add(none);
            return;
        }

        body.getChildren().add(eyebrow(event.getAccounts().size() > 1
                ? "Versi sumber · " + event.getAccounts().size() + " catatan yang tidak sepakat"
                : "Sumber"));

        TabPane tabs = new TabPane();
        tabs.getStyleClass().add("source-tabs");
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabs.setMinHeight(190);

        for (Account account : event.getAccounts()) {
            Tab tab = new Tab(account.getSource().getTitle());

            Label meta = new Label((account.getSource().getKind().getDisplayName()
                    + " · sudut pandang "
                    + (account.getPerspective() == null
                        ? account.getSource().getOrigin()
                        : account.getPerspective())).toUpperCase());
            meta.getStyleClass().add("eyebrow");

            Label quote = new Label(account.getNarrative());
            quote.getStyleClass().add("account-quote");

            VBox content = new VBox(8, meta, quote);
            content.setPadding(new Insets(13, 2, 6, 2));

            if (account.getSource().getNote() != null) {
                Label caveat = new Label(account.getSource().getNote());
                caveat.getStyleClass().add("account-caveat");
                content.getChildren().add(caveat);
            }

            tab.setContent(content);
            tabs.getTabs().add(tab);
        }
        body.getChildren().add(tabs);
    }
}
