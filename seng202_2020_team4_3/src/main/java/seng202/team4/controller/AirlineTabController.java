package seng202.team4.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.textfield.TextFields;
import seng202.team4.model.Airline;
import seng202.team4.model.DataLoader;
import seng202.team4.model.DataType;
import seng202.team4.model.Path;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Performs logic for the 'Airline' tab of the application
 * Responsible for connecting the airline data to the JavaFX interface,
 * this includes displaying the airlines in the JavaFX TableView with data
 * from the 'Airlines' SQLite database table and also initialising/updating the
 * additive filtering and searching of said data.
 */
public class AirlineTabController extends DataController {

    /**
     * TableView of the airline raw data table.
     */
    @FXML private TableView<Airline> dataTable;
    /**
     * Airline column of the raw data table.
     */
    @FXML private TableColumn<Airline, String> airlineColumn;
    /**
     * Country column of the raw data table.
     */
    @FXML private TableColumn<Airline, String> countryColumn;
    /**
     * Airline code column of the raw data table.
     */
    @FXML private TableColumn<Airline, String> iataColumn;
    /**
     * Searchable combobox for filtering by country.
     */
    @FXML private ComboBox<String> countryCombobox;
    /**
     * Text field used to search data table.
     */
    @FXML private TextField searchField;


    /**
     * Mutable ObservableLst containing a list of airlines for the search filter.
     */
    private ObservableList<Airline> airlines = FXCollections.observableArrayList();
    /**
     * Mutable ObservableList containing a list of countries for the countryComboBox.
     */
    private ObservableList<String> countries = FXCollections.observableArrayList();
    /**
     * Initialization of FilteredList for countryComboBox.
     */
    private FilteredList<Airline> countryFilter = new FilteredList<>(airlines, p -> true);
    /**
     * Initialization of FilteredList for the search text field
     */
    private FilteredList<Airline> searchFilter = new FilteredList<>(countryFilter, p -> true);

    /**
     * Holds the high level logic (set of instructions) for initialisation.
     * Initialisation order: Set New Record Button, Table Columns, Set DataSet ComboBox, Set DataSet Listener,
     * Set Table
     */
    @FXML
    public void initialize() {
        airlineColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        iataColumn.setCellValueFactory(new PropertyValueFactory<>("iata"));


        // Multiple rows can be selected
        dataTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        try {
            initialiseButtons();
            setDataSetComboBox();
            setDataSetListener();
            setTable();
        } catch (Exception e) {
            ErrorController.createErrorMessage("Error loading airline table.", false);
        }

    }

    /**
     * Sets the JavaFX table with rows from the 'Airline' database table.
     * This is done using the table query and assigning each record to a row in the table
     * @param rs JDBC ResultSet obtained from querying the Database Airline table and is used to set the rows
     *           of the JavaFX data table by creating N Airline objects from the query that results in N tuples.
     * @throws SQLException if the query fails, throws an exception
     */
    @Override
    public void setTableData(ResultSet rs) throws SQLException {
        airlines = FXCollections.observableArrayList();
        countries = FXCollections.observableArrayList();
        while (rs.next()) {
            int id = rs.getInt("Id");
            String name = rs.getString("Name");
            String country = rs.getString("Country");
            String alias = rs.getString("Alias");
            if (alias.equals("\\N")) {
                alias = "N/A";
            }
            String iata = rs.getString("Iata");
            String icao = rs.getString("Icao");
            String callSign = rs.getString("Callsign");
            boolean active = rs.getString("RecentlyActive").equals("true");

            Airline airline = new Airline(name, alias, iata, icao, callSign, country, active);
            airline.setId(id);
            airlines.add(airline);
            addToComboBoxList(countries, country);
        }
        dataTable.setItems(airlines);
    }

    /**
     * Sorts the FX observable lists for the country ComboBox and
     * uses ControlsFX library to make the ComboBox searchable.
     * filterData() is also called here because filtering of the table is based on ComboBox selections
     * and is required to be refreshed whenever a new dataset is chosen to be displayed.
     */
    @Override
    public void initialiseComboBoxes() {
        // Sort and set combobox items
        FXCollections.sort(countries);
        countryCombobox.setItems(countries);

        TextFields.bindAutoCompletion(countryCombobox.getEditor(), countryCombobox.getItems());

        filterData();

    }

    /**
     * Filtering of table data is done here by initialising then iteratively the country combobox filter
     * to a FilteredList object. The country filter requires addFilter(). Then the search bar filter
     * is added through addSearchBar(). Finally the resulting SortedList is bound to the TableView dataTable
     * and the result of the filtering is shown to the user.
     */
    @Override
    public void filterData() {
        // Connect combobox and slider filters to table
        FilteredList<Airline> countryFilter = addFilter(new FilteredList<>(airlines, p -> true), countryCombobox, "Country");

        // Add search bar filter
        FilteredList<Airline> searchFilter = addSearchBar(countryFilter);
        SortedList<Airline> sortedAirline = new SortedList<>(searchFilter);
        sortedAirline.comparatorProperty().bind(dataTable.comparatorProperty());

        dataTable.setItems(sortedAirline);

    }

    /**
     * Override the parent's abstract class as to return the new record FXML file relating to the Airline class.
     * @return String the path to the NEW_AIRLINE_FXML file.
     */
    @Override
    public String getNewRecordFXML() {
        return Path.NEW_AIRLINE_FXML;
    }

    /**
     * Adds a combobox filter, comboBox, to an input FilteredList, filteredList through
     * adding a listener to comboBox (which works with combobox searching as well). The result is
     * a new FilteredList which has the comboBox filter applied.
     *
     * @param filteredList the filtered list to add a filter to.
     * @param comboBox     the searchable combobox filter that is added to the filteredList.
     * @param filter       a String parameter used to specify which filter is being applied.
     * @return FilteredList with the new filter added.
     */
    public FilteredList<Airline> addFilter(FilteredList<Airline> filteredList, ComboBox<String> comboBox, String filter) {
        FilteredList<Airline> newFilter = new FilteredList<>(filteredList, p -> true);
        comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                newFilter.setPredicate(airline -> {
                    if (newValue == null) {
                        return true;
                    }
                    String lower = newValue.toLowerCase();
                    if (filter.equals("Country")) {
                        return airline.getCountry().toLowerCase().contains(lower);
                    }
                    return false;
                }));
        return newFilter;
    }

    /**
     * Adds holistic search bar filter which searches the Airline's name and it's country.
     * @param countryFilter the last filter to be added to before the search bar.
     * @return FilteredList with the search bar filter added.
     */
    private FilteredList<Airline> addSearchBar(FilteredList<Airline> countryFilter) {
        FilteredList<Airline> searchFilter = new FilteredList<>(countryFilter, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) ->
                searchFilter.setPredicate(airline -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lower = newValue.toLowerCase();
                    if (airline.getName().toLowerCase().contains(lower)) {
                        return true;
                    } else if (airline.getCountry().toLowerCase().contains(lower)) {
                        return true;
                    } else if (airline.getIata().toLowerCase().contains(lower)) {
                        return true;
                    }
                    return false;
                }));
        return searchFilter;
    }

    /**
     * Returns the 'Airline' datatype specifically used for this controller.
     * @return DataType a new Airline object.
     */
    @Override
    public DataType getDataType() { return new Airline(); }

    /**
     * Returns the JDBC/SQL query for selecting all rows from the 'Airline' table.
     * @return String for the  JDBC/SQL query for selecting all rows from the 'Airline' table.
     */
    @Override
    public String getTableQuery() {
        return "SELECT * FROM Airline";
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
                airlines.remove(row);
            }
            else {
                ErrorController.createErrorMessage("Can't delete record: \n" + row.toString(), false);
            }
        }
    }

    @Override
    public void clearFilters() {
        countryCombobox.setValue("");
    }


}
