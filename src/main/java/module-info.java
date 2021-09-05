module fxinit {
    requires javafx.controls;
    requires javafx.fxml;

    opens ro.bid90.controller to javafx.fxml;

    exports ro.bid90;
    exports ro.bid90.controller;
}