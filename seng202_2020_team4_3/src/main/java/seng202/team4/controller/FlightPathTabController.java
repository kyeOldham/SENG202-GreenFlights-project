package seng202.team4.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import seng202.team4.model.DataLoader;
import seng202.team4.model.DataType;
import seng202.team4.model.FlightPath;
import seng202.team4.model.Path;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Performs logic for the 'Flight Path' tab of the application
 * Responsible for connecting the flight path data to the JavaFX interface,
 * this includes displaying the coordinates and altitude in the JavaFX TableView with data
 * from the 'FlightPath' SQLite database table and also initialising/updating the
 * additive filtering and searching of said data.
 */
public class FlightPathTabController extends DataController {

    /**
     * TableView of the flight path raw data table.
     */
    @FXML private TableView<FlightPath> dataTable;
    /**
     * Type column of the raw data table.
     */
    @FXML private TableColumn<FlightPath, String> typeColumn;
    /**
     * ID column of the raw data table.
     */
    @FXML private TableColumn<FlightPath, String> idColumn;
    /**
     * Altitude column of the raw data table.
     */
    @FXML private TableColumn<FlightPath, Integer> altitudeColumn;
    /**
     * Latitude column of the raw data table.
     */
    @FXML private TableColumn<FlightPath, Double> latitudeColumn;
    /**
     * Longitude column of the raw data table.
     */
    @FXML private TableColumn<FlightPath, Double> longitudeColumn;
    /**
     * Text field used to search data table.
     */
    @FXML private TextField searchField;
    /**
     * Mutable ObservableLst containing a list of flight paths.
     */
    private ObservableList<FlightPath> flightPaths = FXCollections.observableArrayList();
    /**
     * Initialization of SortedList of routes that will be used by the filters
     */
    private SortedList<FlightPath> sortedFlightPath;

    /**
     * Holds the high level logic (set of instructions) for initialisation.
     * Initialisation order: Record Button, Table Columns, Set DataSet ComboBox, Set DataSet Listener,
     * Set Table
     */
    @FXML
    public void initialize() {
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("flightPathId"));
        altitudeColumn.setCellValueFactory(new PropertyValueFactory<>("altitude"));
        latitudeColumn.setCellValueFactory(new PropertyValueFactory<>("latitude"));
        longitudeColumn.setCellValueFactory(new PropertyValueFactory<>("longitude"));

        // Multiple rows can be selected
        dataTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        try {
            initialiseButtons();
            setDataSetComboBox();
            setDataSetListener();
            setTable();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

    }

    /**
     * Sets the JavaFX table with rows from the 'FlightPath' database table.
     * This is done using the table query and assigning each record to a row in the table
     * @param rs JDBC ResultSet obtained from querying the Database FlightPath table and is used to set the rows
     *           of the JavaFX data table by creating N FlightPath objects from the query that results in N tuples.
     * @throws SQLException if the query fails, throws an exception
     */
    @Override
    public void setTableData(ResultSet rs) throws SQLException {
        while (rs.next()) {
            int id = rs.getInt("ID");
            String type = rs.getString("Type");
            String flightPathID = rs.getString("FlightPathID");
            int altitude = rs.getInt("Altitude");
            double latitude = rs.getDouble("Latitude");
            double longitude = rs.getDouble("Longitude");

            FlightPath flightPath = new FlightPath(type, flightPathID, altitude, latitude, longitude);
            flightPath.setId(id);
            flightPaths.add(flightPath);
        }
        dataTable.setItems(flightPaths);
    }

    /**
     * Override the parent's abstract class as to return the new record FXML file relating to the FlightPath class.
     * @return String the path to the NEW_FLIGHT_PATH_FXML file.
     */
    @Override
    public String getNewRecordFXML() {
        return Path.NEW_FLIGHT_PATH_FXML;
    }

    /**
     * Returns the 'FlightPath' datatype specifically used for this controller.
     * @return DataType a new FlightPath object.
     */
    @Override
    public DataType getDataType() { return new FlightPath(); }

    /**
     * Returns the JDBC/SQL query for selecting all rows from the 'FlightPath' table.
     * @return String for the  JDBC/SQL query for selecting all rows from the 'FlightPath' table.
     */
    @Override
    public String getTableQuery() {
        return "SELECT * FROM FlightPath";
    }

    @Override
    public void initialiseComboBoxes() {
        filterData();

    }

    @Override
    public void filterData() {
        FilteredList<FlightPath> searchFilter = addSearchBarFilter(flightPaths);
        sortedFlightPath = new SortedList<>(searchFilter);
        sortedFlightPath.comparatorProperty().bind(dataTable.comparatorProperty());

        dataTable.setItems(sortedFlightPath);

    }

    private FilteredList<FlightPath> addSearchBarFilter(ObservableList<FlightPath> flightPaths) {
        FilteredList<FlightPath> searchFilter = new FilteredList<>(flightPaths, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) ->
                searchFilter.setPredicate(flightPath -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    if (flightPath.getType().toLowerCase().contains(newValue.toLowerCase())) {
                        return true;
                    } else if (flightPath.getFlightPathId().toLowerCase().contains(newValue.toLowerCase())) {
                        return true;
                    } else if (String.valueOf(flightPath.getAltitude()).contains(newValue.toLowerCase())) {
                        return true;
                    } else if (String.valueOf(flightPath.getLatitude()).contains(newValue.toLowerCase())) {
                        return true;
                    } else {
                        return (String.valueOf(flightPath.getLongitude()).contains(newValue.toLowerCase()));
                    }
                }));
        return searchFilter;
    }

    /**
     * Delete each row selected in the table view and database
     */
    @FXML
    @Override
    public void deleteRows() {
        DataType rows[] = dataTable.getSelectionModel().getSelectedItems().toArray(new DataType[0]);
        for(DataType row : rows) {
            boolean deleted = DataLoader.deleteRecord(row.getId(), getDataType().getTypeName());
            if (deleted) {
                flightPaths.remove(row);
            }
            else {
                ErrorController.createErrorMessage("Can't delete record: \n" + row.toString(), false);
            }
        }
    }

    @Override
    public void clearFilters() {

    }

}
