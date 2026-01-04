module edu.bethlehemuniversity.library {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires jdk.dynalink;
    requires javafx.graphics;

    // للسماح بمعالجة الـ FXML و الوصول للكلاسات المطلوبة
    opens edu.bethlehemuniversity.library to javafx.fxml;
    opens edu.bethlehemuniversity.library.model to javafx.fxml;
    opens edu.bethlehemuniversity.library.dao to javafx.fxml;
    opens edu.bethlehemuniversity.library.security to javafx.fxml;
    opens edu.bethlehemuniversity.library.utils to javafx.fxml;
    opens edu.bethlehemuniversity.library.views to javafx.fxml;

    // هذه الحزم التي تحتاج أن تكون متاحة لباقي الوحدات
    exports edu.bethlehemuniversity.library;
    exports edu.bethlehemuniversity.library.model;
    exports edu.bethlehemuniversity.library.dao;
    exports edu.bethlehemuniversity.library.security;
    exports edu.bethlehemuniversity.library.utils;
    exports edu.bethlehemuniversity.library.views;
}
