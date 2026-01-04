package edu.bethlehemuniversity.library.views;

import edu.bethlehemuniversity.library.dao.UserDAO;
import edu.bethlehemuniversity.library.model.*;
import edu.bethlehemuniversity.library.security.SessionManager;
import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class LoginView {

    private VBox root;
    private Runnable onLoginSuccess;
    private Runnable onSignupClick;

    public LoginView() {
        buildUI();
    }

    private void buildUI() {
        root = new VBox(25);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(35));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #ffffff, #e7f0ff);");

        // ========== Images ==========
        ImageView universityLogo = load("/assets/bethlehem_logo.png", 120);
        ImageView flag = load("/assets/palestine_flag.png", 130);

        // waving animation
        TranslateTransition wave = new TranslateTransition(Duration.seconds(1.5), flag);
        wave.setFromX(-6);
        wave.setToX(6);
        wave.setCycleCount(Animation.INDEFINITE);
        wave.setAutoReverse(true);
        wave.play();

        HBox images = new HBox(20, universityLogo, flag);
        images.setAlignment(Pos.CENTER);

        // ========== Title ==========
        Label title = new Label("Library Management System");
        title.setFont(Font.font("Arial", 28));
        title.setStyle("-fx-font-weight: bold;");

        Label sub = new Label("Please login to continue");
        sub.setFont(Font.font(14));
        sub.setStyle("-fx-text-fill: #444;");

        // ========== Card ==========
        VBox card = new VBox(18);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(25));
        card.setMaxWidth(460);
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 14;" +
                        "-fx-border-radius: 14;" +
                        "-fx-border-color: #b8c0d0;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.13), 14,0,0,4);"
        );

        Label lblUser = new Label("Username:");
        TextField txtUser = new TextField();
        txtUser.setPrefWidth(260);

        Label lblPass = new Label("Password:");
        PasswordField txtPass = new PasswordField();
        txtPass.setPrefWidth(260);

        card.getChildren().addAll(
                row(lblUser, txtUser),
                row(lblPass, txtPass)
        );

        // ========= Login Button (INSIDE card) =========
        Button btnLogin = styled("Login");
        btnLogin.setPrefWidth(260);
        btnLogin.setFont(Font.font(15));
        card.getChildren().add(btnLogin);

        // ========= Sign Up Hyperlink (OUTSIDE card) =========
        Hyperlink linkSignup = new Hyperlink("Create new account");
        linkSignup.setFont(Font.font(13));
        linkSignup.setUnderline(true);
        linkSignup.setStyle("-fx-text-fill: #0066cc;");

        // Hover effect
        linkSignup.setOnMouseEntered(e ->
                linkSignup.setStyle("-fx-text-fill: #004a99; -fx-underline: true;")
        );
        linkSignup.setOnMouseExited(e ->
                linkSignup.setStyle("-fx-text-fill: #0066cc; -fx-underline: true;")
        );

        linkSignup.setOnAction(e -> {
            if (onSignupClick != null) onSignupClick.run();
        });

        HBox signupContainer = new HBox(linkSignup);
        signupContainer.setAlignment(Pos.CENTER_RIGHT);
        signupContainer.setPadding(new Insets(5, 8, 0, 0));
        signupContainer.setMaxWidth(460);

        // ======= Login Action =======
        btnLogin.setOnAction(e -> {
            if (txtUser.getText().isEmpty() || txtPass.getText().isEmpty()) {
                showMessage("Please fill both fields!", false);
                return;
            }

            User user = UserDAO.authenticateUser(txtUser.getText(), txtPass.getText());
            if (user != null) {
                SessionManager.setCurrentUser(user);
                showMessage("Welcome back, " + user.getUsername(), true);
                if (onLoginSuccess != null) onLoginSuccess.run();
            } else {
                showMessage("Invalid username or password", false);
            }
        });

        root.getChildren().addAll(
                images,
                title,
                sub,
                card,
                signupContainer
        );
    }

    private Button styled(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(120);
        btn.setFont(Font.font(14));
        btn.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #a4a4a4;" +
                        "-fx-padding: 7 18;" +
                        "-fx-background-radius: 9;" +
                        "-fx-border-radius: 9;"
        );

        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: #e3f0ff;" +
                        "-fx-border-color: #4a90e2;" +
                        "-fx-padding: 7 18;" +
                        "-fx-background-radius: 9;" +
                        "-fx-border-radius: 9;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #a4a4a4;" +
                        "-fx-padding: 7 18;" +
                        "-fx-background-radius: 9;" +
                        "-fx-border-radius: 9;"
        ));
        return btn;
    }

    private HBox row(Label label, Control input) {
        HBox row = new HBox(15, label, input);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private ImageView load(String path, int width) {
        return new ImageView(
                new Image(
                        getClass().getResource(path).toExternalForm(),
                        width,
                        0,
                        true,
                        true
                )
        );
    }

    public VBox getRoot() {
        return root;
    }

    public void setOnLoginSuccess(Runnable r) {
        this.onLoginSuccess = r;
    }

    public void setOnSignupClick(Runnable r) {
        this.onSignupClick = r;
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
}
