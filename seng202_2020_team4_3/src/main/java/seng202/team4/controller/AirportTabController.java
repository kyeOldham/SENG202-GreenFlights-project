package seng202.team4.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.textfield.TextFields;
import seng202.team4.model.Airport;
import seng202.team4.model.DataLoader;
import seng202.team4.model.DataType;
import seng202.team4.model.Path;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Performs logic for the 'Airport' tab of the application
 * Responsible for connecting the airport data to the JavaFX interface,
 * this includes displaying the airports in the JavaFX TableView with data
 * from the 'Airports' SQLite database table and also initialising/updating the
 * additive filtering and searching of said data.
 */
public class AirportTabController extends DataController {

    /**
     * TableView of the airport raw data table.
     */
    @FXML private TableView<Airport> dataTable;
    /**
     * Airport column of the raw data table.
     */
    @FXML private TableColumn<Airport, String> airportColumn;
    /**
     * IATA column of raw data table.
     */
    @FXML private TableColumn<Airport, String> iataColumn;
    /**
     * City column of the raw data table.
     */
    @FXML private TableColumn<Airport, String> cityColumn;
    /**
     * Country column of the raw data table.
     */
    @FXML private TableColumn<Airport, String> countryColumn;
    /**
     * Coordinates column of the raw data table.
     */
    @FXML private TableColumn<Airport, String> coordinatesColumn;
    /**
     * Searchable ComboBox for filtering by city.
     */
    @FXML private ComboBox<String> cityCombobox;
    /**
     * Searchable ComboBox for filtering by country.
     */
    @FXML private ComboBox<String> countryCombobox;
    /**
     * Text field used to search raw data table.
     */
    @FXML private TextField searchField;
    /**
     * Initialization of FilteredList for the search text field.
     */
    private ObservableList<Airport> airports;
    /**
     * Initialization of FilteredList for the countries ComboBox.
     */
    private ObservableList<String> countries;
    /**
     * Initialization of FilteredList for the cities ComboBox.
     */
    private ObservableList<String> cities;
    /**
     * Initialization of the SortedList to be used by filters and the checkboxes.
     */
    private SortedList<Airport> sortedAirport;

    /**
     * Holds the high level logic (set of instructions) for initialisation.
     * Initialisation order: Table Columns, Make CheckBox Column, Set DataSet ComboBox,
     * Set DataSet Listener, Set Table
     */
    @FXML
    public void initialize() {
        // Initialise columns to data type attributes
        airportColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        coordinatesColumn.setCellValueFactory(new PropertyValueFactory<>("coordinates"));
        iataColumn.setCellValueFactory(new PropertyValueFactory<>("iata"));

        // Multiple rows can be selected
        dataTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


        // Make and connect checkbox column to AirportsSelected database table
        makeCheckboxColumn();
        initialiseButtons();
        try {
            setDataSetComboBox();
            setDataSetListener();
            setTable(); // Super class method which calls setTableData
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

    }

    /**
     * Adds an extra column, with a checkbox for each airport record, to the table
     * Whenever this checkbox is toggled, the selected airport will be either added
     * or removed from the 'AirportsSelected' database table. This feature enables
     * selected airports to be displayed on the map, on the 'Map' tab
     */
    private void makeCheckboxColumn() {
        final TableColumn<Airport, Boolean> airportTabSelectColumn = new TableColumn<>("Select");
        dataTable.getColumns().addAll(airportTabSelectColumn);
        airportTabSelectColumn.setCellValueFactory(new PropertyValueFactory<>("select"));
        airportTabSelectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(airportTabSelectColumn));
        airportTabSelectColumn.setEditable(true);
        dataTable.setEditable(true);

        airportTabSelectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(param -> {
            if (sortedAirport.get(param).isSelect()) {
                DataLoader.addToAirportsSelectedDatabase(sortedAirport.get(param));
            } else {
                DataLoader.removeFromAirportsSelectedDatabase(sortedAirport.get(param));
            }

            return sortedAirport.get(param).selectProperty();
        }));
    }


    /**
     * Sets the JavaFX table with rows from the 'Airport' database table.
     * This is done using the table query and assigning each record to a row in the table
     * @param rs JDBC ResultSet obtained from querying the Database Airport table and is used to set the rows
     *           of the JavaFX data table by creating N Airport objects from the query that results in N tuples.
     * @throws SQLException if the query fails, throws an exception
     */
    @Override
    public void setTableData(ResultSet rs) throws SQLException {
        airports = FXCollections.observableArrayList();
        countries = FXCollections.observableArrayList();
        cities = FXCollections.observableArrayList();
        countries.clear(); cities.clear();
        countries.add(""); cities.add("");
        while (rs.next()) {
            int id = rs.getInt("Id");
            String name = rs.getString("Name");
            String country = rs.getString("Country");
            String city = rs.getString("City");
            String iata = rs.getString("Iata");
            String icao = rs.getString("Icao");
            double longitude = rs.getDouble("Longitude");
            double latitude = rs.getDouble("Latitude");
            int altitude = rs.getInt("Altitude");
            float timezone = rs.getFloat("TimeZone");
            char dst = rs.getString("Dst").charAt(0);
            String tz = rs.getString("TzDatabaseTime");

            Airport airport = new Airport(name, city, country, iata, icao, latitude, longitude, altitude, timezone, dst, tz);
            airport.setId(id);
            airports.add(airport);

            addToComboBoxList(countries, country);
            addToComboBoxList(cities, city);
        }
        dataTable.setItems(airports);
    }

    /**
     * Sorts the FX observable lists for the country and cities ComboBoxes and
     * uses ControlsFX library to make these ComboBoxes searchable.
     * filterData() is also called here because filtering of the table is based on ComboBox selections
     * and is required to be refreshed whenever a new dataset is chosen to be displayed.
     */
    @Override
    public void initialiseComboBoxes() {
        // Sort and set combobox items
        FXCollections.sort(countries); countryCombobox.setItems(countries);
        FXCollections.sort(cities); cityCombobox.setItems(cities);

        // Make combobox searching autocomplete
        TextFields.bindAutoCompletion(countryCombobox.getEditor(), countryCombobox.getItems());
        TextFields.bindAutoCompletion(cityCombobox.getEditor(), cityCombobox.getItems());

        filterData();

    }

    /**
     * Filtering of table data is done here by initialising then iteratively adding each combobox filter
     * to a FilteredList object. The country and cities filter require addFilter(). Then the search bar filter
     * is added through addSearchBarFilter(). Finally the resulting SortedList is bound to the TableView dataTable
     * and the result of the filtering is shown to the user.
     */
    @Override
    public void filterData() {
        // Connect combobox and slider filters to table
        FilteredList<Airport> countryFilter = addFilter(new FilteredList<>(airports, p -> true), countryCombobox, "Country");
        FilteredList<Airport> cityFilter = addFilter(countryFilter, cityCombobox, "City");

        // Add search bar filter
        FilteredList<Airport> searchFilter = addSearchBarFilter(cityFilter);
        sortedAirport = new SortedList<>(searchFilter);
        sortedAirport.comparatorProperty().bind(dataTable.comparatorProperty());
        dataTable.setItems(sortedAirport);

    }

    /**
     * Override the parent's abstract class as to return the new record FXML file relating to the Airport class.
     * @return String the path to the NEW_AIRPORT_FXML file.
     */
    @Override
    public String getNewRecordFXML() {
        return Path.NEW_AIRPORT_FXML;
    }

    /**
     * Adds a combobox filter, comboBox, to an input FilteredList, filteredList through
     * adding a listener to comboBox (which works with combobox searching as well). The result is
     * a new FilteredList which has the comboBox filter applied.
     *
     * @param filteredList the filtered list to add a filter to.
     * @param comboBox     the searchable combobox filter that is added to the filteredList.
     * @param filter       a String parameter used to specify which filter is being applied.
     * @return the FilteredList with the new filter added.
     */
    public FilteredList<Airport> addFilter(FilteredList<Airport> filteredList, ComboBox<String> comboBox, String filter) {
        FilteredList<Airport> newFilter = new FilteredList<>(filteredList, p -> true);
        comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                newFilter.setPredicate(airport -> {
                    if (newValue == null) {
                        return true;
                    }
                    String lower = newValue.toLowerCase();
                    if (filter.equals("Country")) {
                        return airport.getCountry().toLowerCase().contains(lower);
                    } else if (filter.equals("City")) {
                        return airport.getCity().toLowerCase().contains(lower);
                    }
                    return false;
                }));
        return newFilter;
    }

    /**
     * Adds holistic search bar filter which searches the Airport's name, country and it's city.
     * @param countryFilter the last filter to be added to before the search bar.
     * @return FilteredList with the search bar filter added.
     */
    private FilteredList<Airport> addSearchBarFilter(FilteredList<Airport> countryFilter) {
        FilteredList<Airport> searchFilter = new FilteredList<>(countryFilter, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) ->
                searchFilter.setPredicate(airport -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lower = newValue.toLowerCase();
                    if (airport.getName().toLowerCase().contains(lower)) {
                        return true;
                    } else if (airport.getCountry().toLowerCase().contains(lower)) {
                        return true;
                    } else if (airport.getIata().toLowerCase().contains(lower)) {
                        return true;
                    } else if (airport.getCity().toLowerCase().contains(lower)) {
                        return true;
                    }
                    return false;
                }));
        return searchFilter;
    }

    /**
     * Returns the 'Airport' datatype specifically used for this controller.
     * @return DataType a new Airport object.
     */
    @Override
    public DataType getDataType() { return new Airport(); }

    /**
     * Returns the JDBC/SQL query for selecting all rows from the 'Airport' table.
     * @return String for the JDBC/SQL query for selecting all rows from the 'Airport' table.
     */
    @Override
    public String getTableQuery() { return "SELECT * FROM Airport"; }

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
                airports.remove(row);
            }
            else {
                ErrorController.createErrorMessage("Can't delete record: \n" + row.toString(), false);
            }
        }
    }

    @Override
    public void clearFilters() {
        cityCombobox.setValue("");
        countryCombobox.setValue("");
    }

}
