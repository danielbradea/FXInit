package ro.bid90.controller;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
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
    TextArea description;

    @FXML
    public void clickGenerate(ActionEvent actionEvent) {
        String appName = StringUtils.capitalize(this.appName.getText());
        String appGroupId = this.appGroupId.getText();
        String appArtefactId = this.appArtefactId.getText();
        String appVersion = this.appVersion.getText();
        if (appVersion.isEmpty() || appVersion.isBlank()) {
            appVersion = "1.0.0";
        }
        String fxVersion = this.fxVersion.getValue();
        String appDescription = this.description.getText();
        String path = selectPath(actionEvent);
        if (appDescription.isEmpty() || appDescription.isBlank()) {
            appDescription = appName + " project with JavaFx";
        }

        if (path != null) {
            Alert alert = new Alert(Alert.AlertType.NONE);
            new Thread(new ProjectCreator(appName,
                    appGroupId,
                    appArtefactId,
                    appVersion,
                    appDescription,
                    fxVersion,
                    Paths.get(path), o -> {
                progressBar.setProgress(o);
            }, (type, message) -> {
                Platform.runLater(() -> {
                    alert.setAlertType(type);
                    alert.setContentText(message);
                    alert.show();
                });

            })).start();
        }
        clearProgress();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> items = Arrays.asList("11", "11.0.2", "12", "12.0.2", "13", "13.0.2", "14", "14.0.2.1", "15", "15.0.1", "16");
        ObservableList<String> itemsList = FXCollections.observableList(items);
        fxVersion.setItems(itemsList);
        fxVersion.getSelectionModel().select(0);

        buttonGenerate.setDisable(true);

        appName.textProperty().addListener((observableValue, s, t1) -> {
            appArtefactId.setText(t1.toLowerCase());
            description.setPromptText(StringUtils.capitalize(t1) + " project with JavaFx");
            validateInput();
            clearProgress();
        });

        appVersion.textProperty().addListener((observableValue, s, t1) -> {
            validateInput();
            clearProgress();
        });

        appArtefactId.textProperty().addListener((observableValue, s, t1) -> {
            validateInput();
            clearProgress();
        });
        fxVersion.valueProperty().addListener(observable -> {
            clearProgress();
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
        if (appArtefactId.getText().isEmpty() || appName.getText().isEmpty() || appGroupId.getText().isEmpty()) {
            buttonGenerate.setDisable(true);
        } else if (appArtefactId.getText().isBlank() || appName.getText().isBlank() || appGroupId.getText().isBlank()) {
            buttonGenerate.setDisable(true);
        } else
            buttonGenerate.setDisable(false);
    }

    void clearProgress() {
        progressBar.setProgress(0);
    }


}
