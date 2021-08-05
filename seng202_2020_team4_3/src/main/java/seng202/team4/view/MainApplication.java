package seng202.team4.view;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import seng202.team4.model.Path;

import java.io.IOException;

/**
 * Called by Main, loads and shows the main scene FXML.
 */
public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(Path.MAIN_SCENE_FXML));
        primaryStage.setTitle("Green Flights");
        primaryStage.setMinHeight(720);
        primaryStage.setMinWidth(1280);
        Scene mainScene = new Scene(root);
        mainScene.getStylesheets().add(Path.STYLE_SHEET);
        primaryStage.setScene(mainScene);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(Path.APP_ICON)));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
