module se233.chapter2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.io;
    requires org.json;
    requires java.naming;

    opens se233.chapter2 to javafx.fxml;
    exports se233.chapter2;
    exports se233.chapter2.controller;
    exports se233.chapter2.model;
    exports se233.chapter2.view;
}