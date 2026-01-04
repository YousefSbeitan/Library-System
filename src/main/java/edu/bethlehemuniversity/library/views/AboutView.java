package edu.bethlehemuniversity.library.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class AboutView {

    private VBox root;

    public AboutView() {
        createView();
    }

    private void createView() {
        root = new VBox(30);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #ffffff, #eef5ff);");

        // ===== Logo =====
        ImageView logo = null;
        try {
            logo = new ImageView(
                    new Image(getClass().getResource("/assets/bethlehem_logo.png").toExternalForm())
            );
            logo.setFitHeight(120);
            logo.setPreserveRatio(true);
        } catch (Exception ex) {
            // Ignoring if image not found
        }

        // ===== Title =====
        Label titleLabel = new Label("Library Management System");
        titleLabel.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 30));
        titleLabel.setStyle("-fx-text-fill: #0a3b72;");

        // ===== Card Container =====
        VBox card = new VBox(18);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(30));
        card.setMaxWidth(520);

        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #c7d4e8;" +
                        "-fx-border-radius: 14;" +
                        "-fx-background-radius: 14;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12, 0, 0, 6);"
        );

        // ===== Section Titles =====
        Label developerTitle = sectionTitle("Developer Information");
        Label universityTitle = sectionTitle("Academic Information");
        Label descTitle = sectionTitle("System Description");

        // ===== Data rows =====
        VBox devBox = new VBox(
                row("Name", "Yousef Sbeitan"),
                row("Student ID", "202303834"),
                row("Email", "202303834@bethlehem.edu")
        );
        devBox.setSpacing(8);

        VBox uniBox = new VBox(
                row("University", "Bethlehem University"),
                row("Department", "Technology Department"),
                row("Course", "SWER351 – Database Management Systems")
        );
        uniBox.setSpacing(8);

        VBox descBox = new VBox();
        descBox.setSpacing(8);

        Label desc = new Label(
                "A complete Library Management System supporting book management,\n" +
                        "user authentication, role permissions, and advanced reporting."
        );
        desc.setWrapText(true);
        desc.setFont(Font.font("Arial", 13));
        desc.setTextAlignment(TextAlignment.CENTER);

        Label ver = new Label("Version 1.0");
        ver.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        ver.setStyle("-fx-text-fill: #777;");

        descBox.getChildren().addAll(desc, ver);

        // ===== Build card visually =====
        card.getChildren().addAll(
                developerTitle,
                devBox,
                new Separator(),
                universityTitle,
                uniBox,
                new Separator(),
                descTitle,
                descBox
        );

        // ===== Final Assembly =====
        if (logo != null) {
            root.getChildren().add(logo);
        }

        root.getChildren().addAll(
                titleLabel,
                card
        );
    }

    private Label sectionTitle(String text) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        lbl.setStyle("-fx-text-fill: #003b73;");
        return lbl;
    }

    private HBox row(String key, String value) {
        Label lblKey = new Label(key + ":");
        lblKey.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lblKey.setStyle("-fx-text-fill: #00355a;");

        Label lblVal = new Label(value);
        lblVal.setFont(Font.font("Arial", 14));

        HBox box = new HBox(8, lblKey, lblVal);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    public VBox getRoot() {
        return root;
    }
}
