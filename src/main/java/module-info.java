module fxinit {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.lang3;

    opens ro.bid90.controller to javafx.fxml;

    exports ro.bid90;
    exports ro.bid90.controller;
}