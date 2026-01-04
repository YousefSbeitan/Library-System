package edu.bethlehemuniversity.library;

import edu.bethlehemuniversity.library.dao.*;
import edu.bethlehemuniversity.library.model.*;
import edu.bethlehemuniversity.library.security.SessionManager;
import edu.bethlehemuniversity.library.utils.*;
import edu.bethlehemuniversity.library.views.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



public class LibraryApp extends Application {

    private Stage primaryStage;
    private BorderPane mainLayout;
    private MenuBar menuBar;
    private VBox sidebar;
    private BorderPane contentArea;

    private ObservableList<Author> authorList;
    private ObservableList<Book> bookList;
    private ObservableList<Borrower> borrowerList;

    private TableView<Author> authorTable;
    private TableView<Book> bookTable;
    private TableView<Borrower> borrowerTable;

    private AuthorDAO authorDAO;
    private BookDAO bookDAO;
    private BorrowerDAO borrowerDAO;

    private LoginView loginView;
    private SignupView signupView;
    private ReportsView reportsView;
    private AboutView aboutView;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        UserDAO.createUsersTable();
        showLoginView();

        primaryStage.setTitle("Library Management System");

        primaryStage.setMaximized(true);
        primaryStage.setFullScreen(false);


        primaryStage.show();
    }

    private void showLoginView() {
        loginView = new LoginView();

        loginView.setOnLoginSuccess(() -> showMainWindow());
        loginView.setOnSignupClick(() -> showSignupView());

        Scene scene = new Scene(loginView.getRoot(),
                javafx.stage.Screen.getPrimary().getBounds().getWidth(),
                javafx.stage.Screen.getPrimary().getBounds().getHeight()
        );


        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.setFullScreen(false);
    }

    private void showSignupView() {
        signupView = new SignupView();

        signupView.setOnSignupSuccess(() -> showLoginView());
        signupView.setOnBackClick(() -> showLoginView());

        Scene scene = new Scene(signupView.getRoot(),
                javafx.stage.Screen.getPrimary().getBounds().getWidth(),
                javafx.stage.Screen.getPrimary().getBounds().getHeight()
        );


        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.setFullScreen(false);
    }

    private void showMainWindow() {
        mainLayout = new BorderPane();

        authorDAO = new AuthorDAO();
        bookDAO = new BookDAO();
        borrowerDAO = new BorrowerDAO();

        authorList = FXCollections.observableArrayList();
        bookList = FXCollections.observableArrayList();
        borrowerList = FXCollections.observableArrayList();

        reloadAuthors();
        reloadBooks();
        reloadBorrowers();

        createMenuBar();
        mainLayout.setTop(menuBar);

        createSidebar();
        mainLayout.setLeft(sidebar);

        contentArea = new BorderPane();
        mainLayout.setCenter(contentArea);

        showAuthorView();

        Scene scene = new Scene(
                mainLayout,
                javafx.stage.Screen.getPrimary().getBounds().getWidth(),
                javafx.stage.Screen.getPrimary().getBounds().getHeight()
        );

        primaryStage.setMaximized(true);
        primaryStage.setFullScreen(false);


        primaryStage.setScene(scene);
    }

    private void reloadAuthors() {
        var list = authorDAO.getAll();
        if (list == null) list = new java.util.ArrayList<>();
        authorList.setAll(list);
    }

    private void reloadBooks() {
        var list = bookDAO.getAll();
        if (list == null) list = new java.util.ArrayList<>();
        bookList.setAll(list);
    }

    private void reloadBorrowers() {
        var list = borrowerDAO.getAll();
        if (list == null) list = new java.util.ArrayList<>();
        borrowerList.setAll(list);
    }

    private void createMenuBar() {

        menuBar = new MenuBar();

        // تغيير ارتفاع المنيو بار و خلفيته
        menuBar.setStyle(
                "-fx-background-color: linear-gradient(to right, #0077aa, #00bcd4);" +
                        "-fx-padding: 8;" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;"
        );

        // ---------- FILE MENU ----------
        Menu fileMenu = new Menu("📁  File");
        fileMenu.setStyle("-fx-text-fill: white;");

        MenuItem logoutItem = new MenuItem("🚪 Logout");
        logoutItem.setOnAction(e -> {
            SessionManager.logout();
            showLoginView();
        });

        MenuItem exitItem = new MenuItem("❌ Exit");
        exitItem.setOnAction(e -> primaryStage.close());

        fileMenu.getItems().addAll(logoutItem, new SeparatorMenuItem(), exitItem);

        // ---------- VIEW MENU ----------
        Menu viewMenu = new Menu("👁️  View");
        viewMenu.setStyle("-fx-text-fill: white;");

        MenuItem reportsItem = new MenuItem("📊 Reports");
        reportsItem.setOnAction(e -> showReportsView());

        MenuItem aboutItem = new MenuItem("ℹ️ About");
        aboutItem.setOnAction(e -> showAboutView());

        viewMenu.getItems().addAll(reportsItem, new SeparatorMenuItem(), aboutItem);

        // ---------- USER MENU ----------
        Menu userMenu = new Menu("👤 User");
        userMenu.setStyle("-fx-text-fill: white;");

        User currentUser = SessionManager.getCurrentUser();
        if (currentUser != null) {
            MenuItem userInfoItem = new MenuItem(
                    "Logged in as: " + currentUser.getUsername() + " (" + currentUser.getRole() + ")"
            );
            userInfoItem.setDisable(true);

            userMenu.getItems().add(userInfoItem);
        }

        // إضافة منيو للأعلى
        menuBar.getMenus().addAll(fileMenu, viewMenu, userMenu);

        // تغيير لون الخلفية أثناء فتح المنيو
        menuBar.getMenus().forEach(menu -> {
            menu.setOnShowing(ev -> menu.setStyle("-fx-background-color: #006b95; -fx-text-fill: white;"));
            menu.setOnHiding(ev -> menu.setStyle("-fx-background-color: transparent; -fx-text-fill: white;"));
        });
    }


    private void createSidebar() {

        sidebar = new VBox(15);
        sidebar.setPadding(new Insets(25));
        sidebar.setPrefWidth(240);

        // خلفية وتنسيق بسيط بدون CSS
        sidebar.setStyle(
                "-fx-background-color: rgba(230,230,230,0.45);" +
                        "-fx-border-width: 0 2 0 0;" +
                        "-fx-border-color: #b8b8b8;"
        );

        // العنوان الرئيسي
        Label title = new Label("System Sections");
        title.setStyle(
                "-fx-font-size: 17;" +
                        "-fx-font-weight: bold;"
        );

        Separator separator = new Separator();

        // Buttons
        Button btnAuthor = createStyledButton("📚  Authors", () -> showAuthorView());
        Button btnBook = createStyledButton("📘  Books", () -> showBookView());
        Button btnBorrower = createStyledButton("👤  Borrowers", () -> showBorrowerView());
        Button btnPublisher = createStyledButton("🏢  Publishers", () -> showPublisherView());
        Button btnLoan = createStyledButton("⏳  Loans", () -> showLoanView());
        Button btnSale = createStyledButton("💰  Sales", () -> showSaleView());
        Button btnBorrowerType = createStyledButton("🧩  Borrower Types", () -> showBorrowerTypeView());
        Button btnLoanPeriod = createStyledButton("📅  Loan Periods", () -> showLoanPeriodView());

        // إضافة كل شيء
        sidebar.getChildren().addAll(
                title,
                separator,
                btnAuthor, btnBook, btnBorrower, btnPublisher,
                btnLoan, btnSale, btnBorrowerType, btnLoanPeriod
        );
    }


    private Button createStyledButton(String text, Runnable action) {

        Button btn = new Button(text);
        btn.setPrefWidth(200);
        btn.setPrefHeight(45);

        btn.setStyle(
                "-fx-background-color: #ffffff;" +
                        "-fx-font-size: 14;" +
                        "-fx-text-fill: #202020;" +
                        "-fx-alignment: CENTER_LEFT;" +
                        "-fx-padding: 0 0 0 10;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;" +
                        "-fx-border-color: #c5c5c5;"
        );

        // تأثير hover بسيط
        btn.setOnMouseEntered(e -> {
            btn.setStyle(
                    "-fx-background-color: #f3f3f3;" +
                            "-fx-font-size: 14;" +
                            "-fx-text-fill: #202020;" +
                            "-fx-alignment: CENTER_LEFT;" +
                            "-fx-padding: 0 0 0 10;" +
                            "-fx-border-radius: 5;" +
                            "-fx-background-radius: 5;" +
                            "-fx-border-color: #a0a0a0;"
            );
        });

        btn.setOnMouseExited(e -> {
            btn.setStyle(
                    "-fx-background-color: #ffffff;" +
                            "-fx-font-size: 14;" +
                            "-fx-text-fill: #202020;" +
                            "-fx-alignment: CENTER_LEFT;" +
                            "-fx-padding: 0 0 0 10;" +
                            "-fx-border-radius: 5;" +
                            "-fx-background-radius: 5;" +
                            "-fx-border-color: #c5c5c5;"
            );
        });

        btn.setOnAction(e -> action.run());
        return btn;
    }


    private void showAuthorView() {


        contentArea.setTop(null);
        contentArea.setCenter(null);
        contentArea.setBottom(null);
        contentArea.setRight(null);

        if (authorTable == null) {
            authorTable = TableBuilder.build(Author.class, authorList);
        }

        HBox searchBox = new SearchBox<Author>()
                .build(Author.class, authorList, authorTable);

        GenericFormBuilder<Author> form = new GenericFormBuilder<>(
                Author.class,
                authorDAO,
                authorTable,
                authorList,
                SessionManager.canEdit(),
                () -> reloadAuthors()
        );

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        authorTable.prefHeightProperty().bind(
                vbox.heightProperty().multiply(0.65)
        );

        vbox.getChildren().addAll(authorTable, form.buildForm());
        VBox.setVgrow(authorTable, Priority.ALWAYS);

        contentArea.setTop(searchBox);
        contentArea.setCenter(vbox);
    }



    private void showBookView() {

        if (bookTable == null) {
            bookTable = TableBuilder.build(Book.class, bookList);
        }

        HBox searchBox = new SearchBox<Book>()
                .build(Book.class, bookList, bookTable);

        GenericFormBuilder<Book> form = new GenericFormBuilder<>(
                Book.class,
                bookDAO,
                bookTable,
                bookList,
                SessionManager.canEdit(),
                () -> reloadBooks()
        );

        contentArea.setTop(null);
        contentArea.setCenter(null);
        contentArea.setBottom(null);
        contentArea.setRight(null);
        contentArea.setLeft(null);


        contentArea.setTop(searchBox);
        contentArea.setCenter(bookTable);
        contentArea.setBottom(form.buildForm());
    }

    private void showBorrowerView() {

        if (borrowerTable == null) {
            borrowerTable = TableBuilder.build(Borrower.class, borrowerList);
        }

        HBox searchBox = new SearchBox<Borrower>()
                .build(Borrower.class, borrowerList, borrowerTable);

        GenericFormBuilder<Borrower> form = new GenericFormBuilder<>(
                Borrower.class,
                borrowerDAO,
                borrowerTable,
                borrowerList,
                SessionManager.canEdit(),
                () -> reloadBorrowers()
        );

        contentArea.setTop(null);
        contentArea.setCenter(null);
        contentArea.setBottom(null);
        contentArea.setRight(null);
        contentArea.setLeft(null);


        contentArea.setTop(searchBox);
        contentArea.setCenter(borrowerTable);
        contentArea.setBottom(form.buildForm());
    }

    private void showPublisherView() {
        ObservableList<Publisher> publishers =
                FXCollections.observableArrayList(DataCollector.getAllPublisher());

        TableView<Publisher> table = TableBuilder.build(Publisher.class, publishers);

        HBox searchBox = new SearchBox<Publisher>().build(Publisher.class, publishers, table);

        contentArea.setCenter(table);
        contentArea.setBottom(new VBox());
        contentArea.setRight(searchBox);
    }


    private void showLoanView() {
        ObservableList<Loan> loans =
                FXCollections.observableArrayList(DataCollector.getAllLoan());

        TableView<Loan> table = TableBuilder.build(Loan.class, loans);

        HBox searchBox = new SearchBox<Loan>().build(Loan.class, loans, table);

        contentArea.setCenter(table);
        contentArea.setBottom(new VBox());
        contentArea.setRight(searchBox);
    }


    private void showSaleView() {
        ObservableList<Sale> sales =
                FXCollections.observableArrayList(DataCollector.getAllSale());

        TableView<Sale> table = TableBuilder.build(Sale.class, sales);

        HBox searchBox = new SearchBox<Sale>().build(Sale.class, sales, table);

        contentArea.setCenter(table);
        contentArea.setBottom(new VBox());
        contentArea.setRight(searchBox);
    }


    private void showBorrowerTypeView() {
        ObservableList<BorrowerType> types =
                FXCollections.observableArrayList(DataCollector.getAllBorrowerType());

        TableView<BorrowerType> table = TableBuilder.build(BorrowerType.class, types);

        HBox searchBox = new SearchBox<BorrowerType>().build(BorrowerType.class, types, table);

        contentArea.setCenter(table);
        contentArea.setBottom(new VBox());
        contentArea.setRight(searchBox);
    }


    private void showLoanPeriodView() {
        ObservableList<LoanPeriod> periods =
                FXCollections.observableArrayList(DataCollector.getAllLoanPeriod());

        TableView<LoanPeriod> table = TableBuilder.build(LoanPeriod.class, periods);

        HBox searchBox = new SearchBox<LoanPeriod>().build(LoanPeriod.class, periods, table);

        contentArea.setCenter(table);
        contentArea.setBottom(new VBox());
        contentArea.setRight(searchBox);
    }


    private void showReportsView() {
        if (reportsView == null) reportsView = new ReportsView();
        contentArea.setTop(null);
        contentArea.setCenter(reportsView.getRoot());
        contentArea.setBottom(null);
    }

    private void showAboutView() {
        if (aboutView == null) aboutView = new AboutView();
        contentArea.setTop(null);
        contentArea.setCenter(aboutView.getRoot());
        contentArea.setBottom(null);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
