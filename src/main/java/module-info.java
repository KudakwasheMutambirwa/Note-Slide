module com.byteclass.noteslide {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.feather;
    requires java.sql;
    requires org.kordamp.ikonli.core;
    requires atlantafx.base;


    opens com.byteclass.noteslide to javafx.fxml;
    opens com.byteclass.noteslide.controller;

    exports com.byteclass.noteslide;
}