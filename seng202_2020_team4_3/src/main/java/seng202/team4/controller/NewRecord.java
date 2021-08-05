package seng202.team4.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import seng202.team4.model.DataLoader;
import seng202.team4.model.DataType;
import seng202.team4.model.DatabaseManager;
import seng202.team4.model.Path;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Abstract class defining common functionality between
 * the controllers used for adding new records.
 */
abstract class NewRecord {
    @FXML
    public Label editAddAirline;
    @FXML
    public Label editAddAirport;
    @FXML
    public Label editAddRoute;
    @FXML
    public Label editAddFlightPath;

    @FXML
    private ComboBox setComboBox;
    @FXML
    private Text errorText;
    private Stage stage;
    private DataController controller;
    private DataType dataType;

    abstract String[] getRecordData();
    public abstract void setRecordData(DataType data);
    public abstract void initialiseToolTips();
    /**
     * Initial set up for the controller, declares the stage
     * the controller is running on and the controller that
     * called it.
     * @param stage stage the controller is running on.
     * @param controller controller that called it.
     * @param dataType The type of data
     */
    void setUp(Stage stage, DataController controller, DataType dataType) {
        this.controller = controller;
        this.stage = stage;
        this.dataType = dataType;
        if (dataType != null) {
            setRecordData(dataType);
            if (dataType.getTypeName() == "Airline") {
                editAddAirline.setText("Edit Airline Record");
            } else if (dataType.getTypeName() == "Airport") {
                editAddAirport.setText("Edit Airport Record");
            } else if (dataType.getTypeName() == "Route") {
                editAddRoute.setText("Edit Route Record");
            } else if (dataType.getTypeName() == "FlightPath") {
                editAddFlightPath.setText("Edit Flight Path Record");
            }
        }
        try {
            setDataSetComboBox(setComboBox);
        } catch (Exception e) {
            String message = "Unable to populate the Data Set combobox";
            ErrorController.createErrorMessage(message, false);
        }
    }

    /**
     * Sets the data set names int the dataset comboBox.
     * @param comboBox the comboBox the names are set in.
     */
    private void setDataSetComboBox(ComboBox comboBox) {
        ObservableList<String> dataSetNames = FXCollections.observableArrayList();
        try (Connection connection = DatabaseManager.connect();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("Select Name from " + controller.getDataType().getSetName());
        ) {
            while (rs.next()) {
                dataSetNames.add(rs.getString("Name"));
            }
        } catch (SQLException e) {
            ErrorController.createErrorMessage("Could not initialize data set combo boxes.", false);
        }
        comboBox.setItems(dataSetNames);
    }

    /**
     * Closes the stage and adds the record to the database if
     * the record was valid, otherwise an error message is shown
     * to the user.
     * @throws Exception SQL Exception
     */
    @FXML
    private void confirm() throws Exception {
        ArrayList<String> errorMessage = new ArrayList<String>();
        String[] recordData = getRecordData();
        DataType record = controller.getDataType().getValid(recordData, errorMessage);
        if (record == null) { // If the record is null display the error message if it exists
            if (errorMessage.size() > 0) {
                errorText.setText(errorMessage.get(0));
                errorText.setVisible(true);
            }
        }
        else { // Otherwise add the new record to the database
            if (setComboBox.getValue() == null) {
                errorText.setText("Cannot find set");
                errorText.setVisible(true);
            }
            else {
                String setName = setComboBox.getValue().toString();
                if (dataType == null) { // If new record
                    DataLoader.addNewRecord(record, setName);
                }
                else { // If updating record
                    record.setId(dataType.getId());
                    DataLoader.updateRecord(record, setName);
                }
                controller.setTable();
                stage.close();
            }
        }
    }

    /**
     * Creates a tooltip for a label that shows information about the entry field when adding a new record
     * @param imageHolderLabel Label that holds the info icon image
     * @param tooltip String that holds the tooltip information
     * @param isImage True if it is an image
     */
    protected void createToolTip(Label imageHolderLabel, String tooltip, boolean isImage) {
        Image image = new Image(getClass().getResourceAsStream(Path.INFO_ICON));
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(30);
        imageView.setFitWidth(30);
        imageView.setPreserveRatio(true);
        imageView.setPickOnBounds(true);
        imageHolderLabel.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        imageHolderLabel.setGraphic(imageView);

        if (isImage) {
            Tooltip imageToolTip = new Tooltip();
            imageToolTip.setGraphic(new ImageView(new Image(tooltip)));
            imageHolderLabel.setTooltip(imageToolTip);
        } else {
            imageHolderLabel.setTooltip(new Tooltip(tooltip));
        }
    }

    /**
     * Closes the stage.
     */
    @FXML
    private void cancel() {
        stage.close();
    }
}
