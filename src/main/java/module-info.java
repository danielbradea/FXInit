module ro.bid90 {
    requires javafx.controls;
    requires javafx.fxml;

    exports ro.bid90;

    opens ro.bid90.controller to javafx.fxml;
    exports ro.bid90.controller;
}
