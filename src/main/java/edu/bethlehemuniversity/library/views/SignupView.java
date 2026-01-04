package edu.bethlehemuniversity.library.views;

import edu.bethlehemuniversity.library.dao.UserDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class SignupView {

    private VBox root;
    private TextField usernameField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private TextField emailField;
    private ComboBox<String> roleComboBox;
    private Button signupButton;
    private Button backButton;
    private Runnable onSignupSuccess;
    private Runnable onBackClick;

    public SignupView() {
        createView();
    }

    private void createView() {

        root = new VBox(25);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(35));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #ffffff, #e7f0ff);");

        // ====== University Logo ======
        ImageView universityLogo = loadLogo();
        root.getChildren().add(universityLogo);

        // ====== Title =======
        Label titleLabel = new Label("Create New Account");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: #222;");
        root.getChildren().add(titleLabel);

        // ====== Card Container ======
        VBox card = new VBox(20);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(25));
        card.setMaxWidth(480);
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 14;" +
                        "-fx-border-radius: 14;" +
                        "-fx-border-color: #b8c0d0;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.13), 14,0,0,4);"
        );

        // ====== Form Grid ======
        GridPane form = new GridPane();
        form.setAlignment(Pos.CENTER);
        form.setHgap(10);
        form.setVgap(15);

        // username
        form.add(new Label("Username:"), 0, 0);
        usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setPrefWidth(260);
        form.add(usernameField, 1, 0);

        // email
        form.add(new Label("Email:"), 0, 1);
        emailField = new TextField();
        emailField.setPromptText("Enter email address");
        emailField.setPrefWidth(260);
        form.add(emailField, 1, 1);

        // password
        form.add(new Label("Password:"), 0, 2);
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setPrefWidth(260);
        form.add(passwordField, 1, 2);

        // confirm password
        form.add(new Label("Confirm Password:"), 0, 3);
        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Re-enter password");
        confirmPasswordField.setPrefWidth(260);
        form.add(confirmPasswordField, 1, 3);

        // role
        form.add(new Label("Role:"), 0, 4);
        roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("student", "staff", "admin");
        roleComboBox.setValue("student");
        roleComboBox.setPrefWidth(260);
        form.add(roleComboBox, 1, 4);

        card.getChildren().add(form);

        // ===== Buttons =====
        signupButton = styledButton("Sign Up");
        signupButton.setPrefWidth(200);

        signupButton.setOnAction(e -> handleSignup());

        backButton = styledButton("Back to Login");
        backButton.setPrefWidth(200);
        backButton.setOnAction(e -> {
            if (onBackClick != null) onBackClick.run();
        });

        VBox btnBox = new VBox(10, signupButton, backButton);
        btnBox.setAlignment(Pos.CENTER);

        card.getChildren().add(btnBox);
        root.getChildren().add(card);
    }

    private void handleSignup() {

        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String role = roleComboBox.getValue();

        // validation
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showMessage("Please fill in all fields!", false);
            return;
        }

        if (username.length() < 3) {
            showMessage("Username must be at least 3 characters!", false);
            return;
        }

        if (!isValidEmail(email)) {
            showMessage("Invalid email address!", false);
            return;
        }

        if (!isValidPassword(password)) {
            showMessage("Weak password (min 6 chars, letters & digits).", false);
            return;
        }

        if (!password.equals(confirmPassword)) {
            showMessage("Passwords do not match!", false);
            return;
        }

        if (UserDAO.usernameExists(username)) {
            showMessage("Username already exists!", false);
            return;
        }

        if (UserDAO.registerUser(username, password, email, role)) {

            showMessage("Account created successfully!", true);

            // delay then return to login
            new Thread(() -> {
                try { Thread.sleep(1800); } catch (Exception ignored) {}
                javafx.application.Platform.runLater(() -> {
                    if (onSignupSuccess != null) onSignupSuccess.run();
                });
            }).start();

        } else {
            showMessage("Failed to create account!", false);
        }
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".") && email.length() > 5;
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 6) return false;
        boolean hasLetter = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            if (Character.isDigit(c)) hasDigit = true;
        }
        return hasLetter && hasDigit;
    }

    // ===== Styling and Helpers =====

    private ImageView loadLogo() {
        return new ImageView(
                new Image(
                        getClass().getResource("/assets/bethlehem_logo.png").toExternalForm(),
                        180, // image width
                        0,
                        true,
                        true
                )
        );
    }

    private Button styledButton(String text) {
        Button btn = new Button(text);
        btn.setFont(Font.font(15));
        btn.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #a4a4a4;" +
                        "-fx-padding: 9 20;" +
                        "-fx-background-radius: 9;" +
                        "-fx-border-radius: 9;"
        );

        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: #e3f0ff;" +
                        "-fx-border-color: #4a90e2;" +
                        "-fx-padding: 9 20;" +
                        "-fx-background-radius: 9;" +
                        "-fx-border-radius: 9;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #a4a4a4;" +
                        "-fx-padding: 9 20;" +
                        "-fx-background-radius: 9;" +
                        "-fx-border-radius: 9;"
        ));
        return btn;
    }

    private void showMessage(String text, boolean success) {

        Label msg = new Label(text);

        msg.setStyle(
                "-fx-padding: 14 22;" +
                        "-fx-font-size: 14;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-radius: 8;" +
                        (success
                                ? "-fx-background-color: #d4ffd6; -fx-border-color: #5cb85c; -fx-text-fill: #25632b;"
                                : "-fx-background-color: #ffe0e0; -fx-border-color: #e75b5b; -fx-text-fill: #A40000;"
                        )
        );

        StackPane overlay = new StackPane(msg);
        overlay.setAlignment(Pos.CENTER);
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.10); -fx-padding: 30;");

        root.getChildren().add(overlay);

        new Thread(() -> {
            try { Thread.sleep(2000); } catch (Exception ignored) {}
            javafx.application.Platform.runLater(() -> {
                root.getChildren().remove(overlay);
            });
        }).start();
    }

    // ===== public methods =====

    public VBox getRoot() {
        return root;
    }

    public void setOnSignupSuccess(Runnable callback) {
        this.onSignupSuccess = callback;
    }

    public void setOnBackClick(Runnable callback) {
        this.onBackClick = callback;
    }
}
