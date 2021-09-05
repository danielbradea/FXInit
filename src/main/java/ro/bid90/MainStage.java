package ro.bid90;

import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class MainStage extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/main.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("FXInit");
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResource("Fx.png").openStream()));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}