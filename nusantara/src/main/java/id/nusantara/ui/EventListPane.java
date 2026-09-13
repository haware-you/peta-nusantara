package id.nusantara.ui;

import id.nusantara.events.HistoricalEvent;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Daftar peristiwa yang jatuh di dalam rentang yang sedang tampak. */
public final class EventListPane extends VBox {

    private final TimelineModel model;
    private final ListView<HistoricalEvent> list = new ListView<>();
    private final Label count = new Label();

    private boolean syncing = false;

    public EventListPane(TimelineModel model) {
        this.model = model;
        getStyleClass().add("panel");

        Label title = new Label("PERISTIWA DALAM RENTANG");
        title.getStyleClass().add("eyebrow");
        count.getStyleClass().add("eyebrow");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox head = new HBox(title, spacer, count);
        head.getStyleClass().add("panel-head");
        head.setAlignment(Pos.CENTER_LEFT);

        list.getStyleClass().add("event-list");
        list.setPrefHeight(430);
        list.setCellFactory(view -> new EventCell());
        // Hanya bereaksi pada tindakan pengguna. Mendengarkan selectedItemProperty
        // ikut menangkap perubahan yang dipicu setItems(), sehingga pilihan yang
        // dipulihkan justru menimpa pilihan yang sedang aktif.
        list.setOnMouseClicked(e -> pushSelection());
        list.setOnKeyReleased(e -> pushSelection());

        getChildren().addAll(head, list);

        refresh();
        model.windowProperty().addListener((obs, old, now) -> refresh());
        model.selectionProperty().addListener((obs, old, now) -> syncSelection());
    }

    public void refresh() {
        List<HistoricalEvent> inWindow = new ArrayList<>(
                model.timeline().eventsIn(model.window()));
        inWindow.sort(Comparator.comparingInt(HistoricalEvent::getYear));
        list.setItems(FXCollections.observableArrayList(inWindow));
        count.setText(inWindow.size() + " PERISTIWA");
        syncSelection();
    }

    private void pushSelection() {
        HistoricalEvent chosen = list.getSelectionModel().getSelectedItem();
        if (!syncing && chosen != null) {
            model.select(TimelineModel.Kind.EVENT, chosen.getId());
        }
    }

    private void syncSelection() {
        HistoricalEvent selected = model.selectedEvent();
        syncing = true;
        if (selected == null) {
            list.getSelectionModel().clearSelection();
        } else {
            list.getSelectionModel().select(selected);
        }
        syncing = false;
    }

    /** Dipanggil setelah tema berganti agar warna titik ikut berubah. */
    public void refreshTheme() {
        list.refresh();
    }

    private final class EventCell extends ListCell<HistoricalEvent> {

        @Override
        protected void updateItem(HistoricalEvent event, boolean empty) {
            super.updateItem(event, empty);
            if (empty || event == null) {
                setGraphic(null);
                setText(null);
                return;
            }

            Label year = new Label(event.getWhen().toString());
            year.getStyleClass().add("event-row-year");
            year.setMinWidth(56);
            year.setMaxWidth(56);

            Circle pip = new Circle(4, Palette.of(event.getType()));

            Label name = new Label(event.getName());
            name.getStyleClass().add("event-row-name");
            name.setWrapText(true);

            StringBuilder sub = new StringBuilder(event.getType().getDisplayName());
            if (event.isContested()) {
                sub.append(" · sumber berselisih");
            }
            if (event.spansMultipleIslands()) {
                sub.append(" · lintas pulau");
            }
            Label subtitle = new Label(sub.toString());
            subtitle.getStyleClass().add("event-row-sub");

            VBox text = new VBox(1, name, subtitle);
            HBox row = new HBox(9, year, pip, text);
            row.setAlignment(Pos.TOP_LEFT);
            row.setPadding(new Insets(9, 14, 9, 14));
            HBox.setHgrow(text, Priority.ALWAYS);

            setGraphic(row);
        }
    }
}
