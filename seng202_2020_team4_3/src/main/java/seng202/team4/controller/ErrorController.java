package seng202.team4.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import seng202.team4.model.Path;

/**
 * Controls the operation of the changeable error popup.
 */
public class ErrorController {

    @FXML
    public Label messageField;
    public boolean exitSystem;
    private Stage stage;

    /**
     * Constructs the error message by calling the setup method.
     * @param message message to show
     * @param exitSystem boolean of whether or not the system should exit
     */
    public static void createErrorMessage(String message, boolean exitSystem) {
        Stage stage = new Stage();
        stage.setTitle("Error");
        stage.getIcons().add(new Image(ErrorController.class.getResourceAsStream(Path.APP_ICON)));
        FXMLLoader loader = new FXMLLoader(ErrorController.class.getResource(Path.ERROR_FXML));
        try {
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            ErrorController controller = loader.getController();
            controller.setUp(stage, message, exitSystem);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(message);
        }
    }

    /**
     * Initialises all the class attributes
     * @param stage error message stage
     * @param message string to show
     * @param exitSystem boolean of whether or not the system should exit
     */
    private void setUp(Stage stage, String message, boolean exitSystem) {
        this.stage = stage;
        messageField.setText(message);
        this.exitSystem = exitSystem;
    }

    /**
     * Exits the application if exitSystem is true
     */
    @FXML
    public void onExit() {
        if (exitSystem) {
            System.exit(0);
        }
        else {
            stage.close();
        }
    }
}
