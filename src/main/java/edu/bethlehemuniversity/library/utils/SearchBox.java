package edu.bethlehemuniversity.library.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.lang.reflect.Field;
import java.util.Arrays;

public class SearchBox<T> {

    public HBox build(Class<T> clazz, ObservableList<T> data, TableView<T> table) {

        HBox root = new HBox(10);
        root.setPadding(new Insets(8));
        root.setAlignment(Pos.CENTER_LEFT);

        // Decorative container
        HBox searchContainer = new HBox(6);
        searchContainer.setPadding(new Insets(6));
        searchContainer.setAlignment(Pos.CENTER_LEFT);
        searchContainer.setStyle(
                "-fx-background-color: #f2f2f2;" +
                        "-fx-background-radius: 15;" +
                        "-fx-border-radius: 15;" +
                        "-fx-border-color: #c0c0c0;" +
                        "-fx-border-width: 1;"
        );

        Label icon = new Label("🔍");
        icon.setStyle("-fx-font-size: 14;");

        TextField tfSearch = new TextField();
        tfSearch.setPromptText("Search here...");
        tfSearch.setPrefWidth(220);

        Button clear = new Button("✖");
        clear.setStyle(
                "-fx-background-radius: 50;" +
                        "-fx-font-weight: bold;"
        );
        clear.setOnAction(e -> {
            tfSearch.clear();
            table.setItems(data);
        });

        searchContainer.getChildren().addAll(icon, tfSearch, clear);

        // Search logic
        tfSearch.textProperty().addListener((obs, old, val) -> {
            if (val == null || val.isEmpty()) {
                table.setItems(data);
                return;
            }

            ObservableList<T> filtered = FXCollections.observableArrayList();

            for (T obj : data) {
                for (Field f : clazz.getDeclaredFields()) {
                    try {
                        f.setAccessible(true);
                        Object value = f.get(obj);
                        if (value != null && value.toString().toLowerCase().contains(val.toLowerCase())) {
                            filtered.add(obj);
                            break;
                        }
                    } catch (IllegalAccessException ignored) {}
                }
            }

            table.setItems(filtered);
        });

        // Add to Layout
        root.getChildren().addAll(
                new Label("Search:"),
                searchContainer
        );

        return root;
    }


}
