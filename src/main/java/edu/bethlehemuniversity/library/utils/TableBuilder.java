package edu.bethlehemuniversity.library.utils;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.lang.reflect.Field;

public class TableBuilder {

    public static <T> TableView<T> build(Class<T> clazz, ObservableList<T> data) {

        if (data == null) {
            data = FXCollections.observableArrayList();
        }

        TableView<T> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        for (Field field : clazz.getDeclaredFields()) {

            field.setAccessible(true);

            TableColumn<T, Object> col =
                    new TableColumn<>(prettify(field.getName()));

            col.setCellValueFactory(cellData -> {
                try {
                    Object fieldValue = field.get(cellData.getValue());

                    // If it is a JavaFX property → return the value inside it
                    if (fieldValue instanceof IntegerProperty)
                        return new SimpleObjectProperty<>(((IntegerProperty) fieldValue).get());

                    if (fieldValue instanceof DoubleProperty)
                        return new SimpleObjectProperty<>(((DoubleProperty) fieldValue).get());

                    if (fieldValue instanceof StringProperty)
                        return new SimpleObjectProperty<>(((StringProperty) fieldValue).get());

                    if (fieldValue instanceof ObjectProperty)
                        return new SimpleObjectProperty<>(((ObjectProperty<?>) fieldValue).get());

                    // Normal field (not property)
                    return new SimpleObjectProperty<>(fieldValue);

                } catch (Exception e) {
                    return new SimpleObjectProperty<>("ERR");
                }
            });

            col.setStyle("-fx-alignment: CENTER;");
            col.setSortable(true);

            table.getColumns().add(col);
        }

        table.setItems(data);
        return table;
    }

    private static String prettify(String raw) {
        if (raw == null || raw.isEmpty()) return raw;
        raw = raw.replace("_", " ").trim();
        return Character.toUpperCase(raw.charAt(0)) + raw.substring(1);
    }
}
