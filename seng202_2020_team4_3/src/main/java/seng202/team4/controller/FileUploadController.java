package seng202.team4.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import seng202.team4.model.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Controller used for uploading datasets
 * to the application.
 */
public class FileUploadController {

    @FXML private TextField nameField;
    @FXML private Text filePath;
    @FXML private Label errorText;
    @FXML private Label imageHolderLabel;
    private File file;
    private DataController controller;
    private Stage stage;

    /**
     * Initial setup of the controller, sets the stage
     * of the scene and the controller that called it.
     * @param controller the controller that opened the stage.
     * @param stage the stage.
     */
    public void setUp(DataController controller, Stage stage) {
        this.controller = controller;
        this.stage = stage;
        showInsertInfo();
    }

    /**
     * Shows the info icon of the expected (general) file format
     */
    private void showInsertInfo() {
        Image image = new Image(getClass().getResourceAsStream(Path.INFO_ICON));
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(30);
        imageView.setFitWidth(30);
        imageView.setPreserveRatio(true);
        imageView.setPickOnBounds(true);
        imageHolderLabel.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        imageHolderLabel.setGraphic(imageView);
        Tooltip imageToolTip = new Tooltip();
        if (controller.getDataType() instanceof Airline) {
            imageToolTip.setGraphic(new ImageView(new Image(Path.AIRLINE_FORMAT)));
            imageHolderLabel.setTooltip(imageToolTip);
        } else if (controller.getDataType() instanceof Airport) {
            imageToolTip.setGraphic(new ImageView(new Image(Path.AIRPORT_FORMAT)));
            imageHolderLabel.setTooltip(imageToolTip);
        } else if (controller.getDataType() instanceof Route) {
            imageToolTip.setGraphic(new ImageView(new Image(Path.ROUTE_FORMAT)));
            imageHolderLabel.setTooltip(imageToolTip);
        } else {
            imageHolderLabel.setTooltip(new Tooltip("See Flightplandatabase.org for file format"));
        }
    }

    /**
     * Gets the name of the data set from the name field.
     * @return the name in the name field, otherwise null if invalid.
     */
    private String getName() {
        String name = nameField.getText().trim();
        if (!name.isBlank() && !name.toLowerCase().equals(DataController.ALL.toLowerCase())) { // Name doesn't equal keyword All
            return name;
        }
        else {
            return null;
        }
    }

    /**
     * Launches a file explorer to get the file from the user.
     */
    public void getFile() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(".DAT, .CSV or .TXT files", "*.dat", "*.csv", "*.txt")
        );
        File f = fc.showOpenDialog(stage);
        if (f != null) {
            file = f;
            filePath.setText(file.toString());
            errorText.setText("");
        }
    }

    /**
     * Closes the stage.
     */
    public void cancel() {
        stage.close();
    }

    /**
     * If dataset name is valid and file is chosen attempts to
     * upload file and closes stage.
     */
    public void confirm() {
        String confirmName = getName();
        if (confirmName == null) {
            errorText.setText("*Pick a name");
        } else if (file == null) {
            errorText.setText("*Pick a file");
        } else if (controller.getDataSetComboBox().getItems().contains(nameField.getText())) {
            errorText.setText("*Dataset name already chosen");
        } else {
            uploadData(confirmName);
            controller.newData(confirmName);
        }
    }

    /**
     * Calls DataLoader class to upload new user inputted file. Shows error popup if data is erroneous.
     * @param name name of the data set user has chosen
     */
    private void uploadData(String name) {
        ArrayList<String> invalidLines = DataLoader.uploadData(name, file, controller.getDataType());
        if (invalidLines.size() > 0) {
            showErrorPopUp(invalidLines);
        } else {
            stage.close();
        }
    }

    /**
     * Shows a popup of how many lines are erroneous and also shows those lines to the user.
     * @param invalidLines erroneous lines to add to popup ListView
     */
    private void showErrorPopUp(ArrayList<String> invalidLines) {
        Stage errorStage = new Stage();
        errorStage.setTitle("Error");
        errorStage.setMinHeight(320);
        errorStage.setMinWidth(510);
        errorStage.setResizable(false);
        errorStage.getIcons().add(new Image(getClass().getResourceAsStream(Path.APP_ICON)));
        errorStage.initModality(Modality.APPLICATION_MODAL);


        FXMLLoader loader = new FXMLLoader(getClass().getResource(Path.INVALID_LINES_POPUP_FXML));

        try {
            errorStage.setScene(new Scene(loader.load()));
            InvalidLinesPopUpController errorController = loader.getController();
            errorController.setUp(errorStage, this.stage);
            errorController.addErrorLines(invalidLines);
        } catch (IOException e) {
            String message = "Malfunction while loading error controller";
            ErrorController.createErrorMessage(message, false);
        }
        errorStage.show();
    }
}
