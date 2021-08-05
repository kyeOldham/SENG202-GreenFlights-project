package seng202.team4.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import seng202.team4.model.DataType;
import seng202.team4.model.DatabaseManager;
import seng202.team4.model.Path;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Describes the required functionality of
 * all the controllers that display tables
 * of data in their scene.
 */
public abstract class DataController {

    public abstract DataType getDataType();
    public abstract String getTableQuery();
    public abstract void setTableData(ResultSet rs) throws SQLException;
    public abstract void initialiseComboBoxes();
    public abstract void filterData();
    public abstract String getNewRecordFXML();
    public abstract void deleteRows();
    public abstract void clearFilters();

    /**
     * TableView of the raw data table.
     */
    @FXML private TableView<DataType> dataTable;

    @FXML
    private ComboBox dataSetComboBox;
    /**
     * Button that opens window to add new record.
     */
    @FXML
    protected Button newRecordButton;
    /**
     * Button that deletes the selected record.
     */
    @FXML
    protected Button deleteRecordButton;

    public final static String ALL = "All";


    public void setDataSetListener() {
        dataSetComboBox.valueProperty().addListener((obs, oldItem, newItem) -> {
            if (newItem != null) {
                try {
                    setDataSet(newItem.toString());
                    clearFilters();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * Sets the images of buttons
     */
    void initialiseButtons() {
        if (newRecordButton != null) {
            Image addRecordImage = new Image(getClass().getResourceAsStream(Path.ADD_RECORD_BUTTON_PNG));
            newRecordButton.setGraphic(new ImageView(addRecordImage));
            newRecordButton.setTooltip(new Tooltip("Add a new individual record"));
        }
        if (deleteRecordButton != null) {
            Image deleteRecordImage = new Image(getClass().getResourceAsStream(Path.DELETE_RECORD_BUTTON_PNG));
            deleteRecordButton.setGraphic(new ImageView(deleteRecordImage));
            deleteRecordButton.setTooltip(new Tooltip("Delete the selected record(s)"));
        }
    }

    /**
     * Displays the data set names in the data set
     * combo box.
     */
    public void setDataSetComboBox() {
        // Connects to the database and gets the names of the data sets.

        try (Connection connection = DatabaseManager.connect();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("Select Name from " + getDataType().getSetName());) {
            // Creates a list to store the keyword ALL in and the names.
            ObservableList<String> dataSetNames = FXCollections.observableArrayList();
            dataSetNames.add(ALL);
            while (rs.next()) {
                dataSetNames.add(rs.getString("Name"));
            }
            dataSetComboBox.setItems(dataSetNames); // Sets the names into the combo box

        } catch (SQLException e) {
            String message = "Failed loading " + getDataType().getTypeName() + " data set combo box, will exit application.";
            ErrorController.createErrorMessage(message, true);
        }
    }

    /**
     * Sets the dataset displayed in the table.
     * @param dataSetName name of the dataset.
     */
    public void setDataSet(String dataSetName) {
        String query = "Select * from " + getDataType().getTypeName() + " ";
        // If dataset name is not equal to keyword all, gets dataset matching name.
        if (dataSetName != ALL) {
            String idQuery = "Select ID from " + getDataType().getSetName() + " Where Name = '" + dataSetName + "';";
            try (Connection connection = DatabaseManager.connect();
                 Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(idQuery)) {
                rs.next();
                query += "WHERE SetID = '" + rs.getInt("ID") + "'";
            } catch (SQLException e) {
                ErrorController.createErrorMessage("Could not show data set.", false);
            }
        }
        setTable(query);
    }

    /**
     * Sets the table with all data of the table's datatype.
     */
    public void setTable() {
        setTable(getTableQuery());
    }

    /**
     * Sets the table using the query provided.
     * @param query specifications for the content of the table.
     */
    public void setTable(String query) {
        try (Connection connection = DatabaseManager.connect();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            setTableData(rs);
        } catch (SQLException e) {
            ErrorController.createErrorMessage("Could not load " + getDataType().getTypeName() + "table data.", false);
        }

        initialiseComboBoxes();
    }

    /**
     * Adds an String to a given combobox if it is not already in it
     * @param comboBoxList combobox to add to
     * @param dataName string to add
     */
    public void addToComboBoxList(ObservableList comboBoxList, String dataName) {
        if (!comboBoxList.contains(dataName)) {
            comboBoxList.add(dataName);
        }
    }

    /**
     * Launches a new stage for uploading data.
     * @throws IOException IO Exception
     */
    public void uploadData() throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Upload " + getDataType().getTypeName() + " Data");
        stage.setMinHeight(400);
        stage.setMinWidth(720);
        stage.getIcons().add(new Image(getClass().getResourceAsStream(Path.APP_ICON)));
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Path.UPLOAD_SCENE_FXML));
        stage.setScene(new Scene(loader.load(), 700, 250));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        FileUploadController controller = loader.getController();
        controller.setUp(this, stage);

    }

    /**
     * Uploads the new data to the database and sets the table to show the new data set.
     * @param name name of the new dataset.
     */
    public void newData(String name) {
        try {
            setDataSet(name);
            dataSetComboBox.getSelectionModel().select(name);
            setDataSetComboBox();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Launches and sets up a stage for adding new records.
     * @throws IOException IO Exception
     */
    public void newRecord() throws IOException {
        Stage stage = new Stage();
        stage.setTitle("New " + getDataType().getSetName() + " Record");

        stage.getIcons().add(new Image(getClass().getResourceAsStream(Path.APP_ICON)));
        FXMLLoader loader = new FXMLLoader(getClass().getResource(getNewRecordFXML()));
        stage.setScene(new Scene(loader.load()));
        stage.initModality(Modality.APPLICATION_MODAL);
        NewRecord controller = loader.getController();
        controller.setUp(stage, this, null);
        stage.showAndWait();
    }

    /**
     * Gets the dataset combo box.
     * @return the dataset combo box.
     */
    public ComboBox getDataSetComboBox() {
        return dataSetComboBox;
    }

    /**
     * Show all attributes of the selected row that can be edited.
     * @param data record to show attributes of
     */
    public void editRecord(DataType data) {
        Stage stage = new Stage();
        stage.setTitle("Edit " + getDataType().getTypeName() + " Record");

        stage.getIcons().add(new Image(getClass().getResourceAsStream(Path.APP_ICON)));
        FXMLLoader loader = new FXMLLoader(getClass().getResource(getNewRecordFXML()));
        try {
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.initModality(Modality.APPLICATION_MODAL);
        NewRecord controller = loader.getController();
        controller.setUp(stage, this, data);
        stage.showAndWait();
    }

    /**
     * Method for getting a record in a data table
     * when it is clicked
     *
     * @param click MouseClick when table is clicked
     */
    @FXML
    public void tableClicked(MouseEvent click) {
        if (click.getClickCount() > 1) {
            DataType data = dataTable.getSelectionModel().getSelectedItem();
            if (data != null) {
                editRecord(data);
            }
        }
    }

}
