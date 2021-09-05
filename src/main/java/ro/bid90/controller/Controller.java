package ro.bid90.controller;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import ro.bid90.ProjectCreator;

public class Controller implements Initializable {

    @FXML
    TextField appGroupId;

    @FXML
    TextField appArtefactId;

    @FXML
    TextField appVersion;

    @FXML
    TextField appName;

    @FXML
    ComboBox<String> fxVersion;

    @FXML
    Button buttonGenerate;

    @FXML
    ProgressBar progressBar;

    @FXML
    public void clickGenerate(ActionEvent actionEvent) {
        String appName = this.appName.getText();
        String appGroupId = this.appGroupId.getText();
        String appArtefactId = this.appArtefactId.getText();
        String appVersion = this.appVersion.getText();
        String fxVersion = this.fxVersion.getValue();
        String path = selectPath(actionEvent);
        if (path != null) {
            new Thread(new ProjectCreator(appName, appGroupId, appArtefactId, appVersion, fxVersion, Paths.get(path), o -> {
                progressBar.setProgress(o);
            })).start();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> items = Arrays.asList("11", "12", "13", "14", "15", "16");
        ObservableList<String> itemsList = FXCollections.observableList(items);
        fxVersion.setItems(itemsList);
        fxVersion.getSelectionModel().select(5);

        buttonGenerate.setDisable(true);

        appName.textProperty().addListener((observableValue, s, t1) -> {
            appArtefactId.setText(t1.toLowerCase());
            validateInput();
        });

        appVersion.textProperty().addListener((observableValue, s, t1) -> {
            validateInput();
        });

        appArtefactId.textProperty().addListener((observableValue, s, t1) -> {
            validateInput();
        });
    }

    String selectPath(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(thisStage);

        if (selectedDirectory == null) {
            return null;
        } else {
            return selectedDirectory.getAbsolutePath();
        }
    }

    void validateInput() {
        if (appArtefactId.getText().isEmpty() || appName.getText().isEmpty() || appVersion.getText().isEmpty() || appGroupId.getText().isEmpty()) {
            buttonGenerate.setDisable(true);
        } else if (appArtefactId.getText().isBlank() || appName.getText().isBlank() || appVersion.getText().isBlank() || appGroupId.getText().isBlank()) {
            buttonGenerate.setDisable(true);
        } else
            buttonGenerate.setDisable(false);
    }


}