package seng202.team4.controller;

import com.jfoenix.controls.JFXSlider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.textfield.TextFields;
import seng202.team4.model.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Performs logic for the 'Routes' tab of the application; extends the DataController Class.
 * Responsible for connecting the route data provided/added to the JavaFX interface, this includes
 * initialising/updating the JavaFX TableView with data from the SQLite database table 'Routes' and also
 * initialising/updating the additive filtering and searching of said data. Checkboxes in this table are used to add
 * to a separate 'RoutesSelected' table and are used by both the Maps tab and the Emissions tab.
 */
public class RouteTabController extends DataController {

    private final int DEFAULT_MAX_STOPS = 2;
    /**
     * TableView of the route raw data table.
     */
    @FXML private TableView<Route> dataTable;
    /**
     * Airline code column of the raw data table.
     */
    @FXML private TableColumn<Route, String> airlineColumn;
    /**
     * Departure airport IATA column of the raw data table.
     */
    @FXML private TableColumn<Route, String> departureAirportColumn;
    /**
     * Destination airport IATA column of the raw data table.
     */
    @FXML private TableColumn<Route, String> destinationAirportColumn;
    /**
     * Number of stops column of the raw data table.
     */
    @FXML private TableColumn<Route, Integer> numStopsColumn;
    /**
     * Plane type column of the raw data table.
     */
    @FXML private TableColumn<Route, String> planeTypeColumn;
    /**
     * Searchable combobox for filtering by airline code.
     */
    @FXML private ComboBox<String> airlineFilterCombobox;
    /**
     * Searchable combobox for filtering by departure IATA.
     */
    @FXML private ComboBox<String> departureFilterCombobox;
    /**
     * Searchable combobox for filtering by destination IATA.
     */
    @FXML private ComboBox<String> destinationFilterCombobox;
    /**
     * Slider for filtering by number of stops for a route.
     */
    @FXML private JFXSlider stopsFilterSlider;
    /**
     * Label that shows how many stops are selected by stopsFilterSlider.
     */
    @FXML private Label stopsLabel;
    /**
     * Searchable combobox for filtering by plane type.
     */
    @FXML private ComboBox<String> planeTypeFilterCombobox;
    /**
     * Text field used to search data table.
     */
    @FXML private TextField searchField;
    /**
     * Mutable ObservableList containing Route objects.
     */
    private ObservableList<Route> routes;
    /**
     * Mutable ObservableList containing a list of airline codes for the airlineFilterCombobox.
     */
    private ObservableList<String> airlineCodes;
    /**
     * Mutable ObservableList containing a list of departure country IATAs for the departureFilterCombobox.
     */
    private ObservableList<String> departureCountries;
    /**
     * Mutable ObservableList containing a list of destination country IATAs for the destinationFilterCombobox.
     */
    private ObservableList<String> destinationCountries;
    /**
     * Mutable ObservableList containing a list of plane types for the planeTypeFilterCombobox.
     */
    private ObservableList<String> planeTypes;

    /**
     * Initialization of SortedList of routes that will be used by the filters
     */
    private SortedList<Route> sortedRoute;
    /**
     * Holds the high level logic (set of instructions) for initialisation.
     * Initialisation order: New Record Button, Sliders, Table Columns, Dataset Chooser ComboBox, Set Table
     */
    @FXML
    public void initialize() {
        // Multiple rows can be selected
        dataTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        try {
            initialiseButtons();
            initialiseColumns();
            setDataSetComboBox();
            initialiseSliders();
            setDataSetListener();
            setTable(); // Super class method which calls setTableData
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

    }

    /**
     * Connect table columns to their respective Airport class attribute.
     * Also makes the checkbox column for selecting specific rows.
     */
    private void initialiseColumns() {
        airlineColumn.setCellValueFactory(new PropertyValueFactory<>("airlineCode"));
        departureAirportColumn.setCellValueFactory(new PropertyValueFactory<>("sourceAirportCode"));
        destinationAirportColumn.setCellValueFactory(new PropertyValueFactory<>("destinationAirportCode"));
        numStopsColumn.setCellValueFactory(new PropertyValueFactory<>("numStops"));
        planeTypeColumn.setCellValueFactory(new PropertyValueFactory<>("planeTypeCode"));
        dataTable.setEditable(true);

        makeCheckboxColumn();
    }

    /**
     * Connect sliders to their respective columns; sliders' maximum values are their corresponding column's max values.
     */
    private void initialiseSliders() {
    stopsFilterSlider.valueChangingProperty().addListener((source, oldValue, newValue) ->
            stopsLabel.textProperty().setValue(String.valueOf((int)stopsFilterSlider.getValue())));
    stopsFilterSlider.setValue(0);
    setSliderMaxStops();

    }

    /**
     * Makes the checkbox column for selecting specific rows of the Route table.
     * Selected rows are then used by the Emissions tab, to calculated distance and carbon emissions,
     * and the Map tab, to display chosen airports.
     */
    private void makeCheckboxColumn() {
        final TableColumn<Route, Boolean> routeTabSelectedRoute = new TableColumn<>("Select");
        dataTable.getColumns().addAll(routeTabSelectedRoute);
        routeTabSelectedRoute.setCellValueFactory(new PropertyValueFactory<>("select"));
        routeTabSelectedRoute.setCellFactory(CheckBoxTableCell.forTableColumn(routeTabSelectedRoute));
        routeTabSelectedRoute.setEditable(true);
        dataTable.setEditable(true);
        // Add listener for adding/removing a specific route to the RoutesSelected Database table
        routeTabSelectedRoute.setCellFactory(CheckBoxTableCell.forTableColumn(param -> {
            if (sortedRoute.get(param).isSelect()) {
                DataLoader.addToRoutesSelectedDatabase(sortedRoute.get(param));
            } else {
                DataLoader.removeFromRoutesSelectedDatabase(sortedRoute.get(param));
            }

            return sortedRoute.get(param).selectProperty();
        }));
    }

    /**
     * Sets the JavaFX Route table with rows from the 'Routes' table from the database.
     * @param rs JDBC ResultSet obtained from querying the Database Route table and is used to set the rows
     *           of the JavaFX airport table by creating N Route objects from the query that results in N tuples.
     * @throws SQLException if the query fails, throws an exception
     */
    @Override
    public void setTableData(ResultSet rs) throws SQLException {
        routes = FXCollections.observableArrayList();
        airlineCodes = FXCollections.observableArrayList();
        departureCountries = FXCollections.observableArrayList();
        destinationCountries = FXCollections.observableArrayList();
        planeTypes = FXCollections.observableArrayList();
        routes = FXCollections.observableArrayList();
        while (rs.next()) {
            int id = rs.getInt("Id");
            String airline = rs.getString("Airline");
            String sourceAirport = rs.getString("SourceAirport");
            String destinationAirport = rs.getString("DestinationAirport");
            boolean codeshare = rs.getString("Codeshare").equals("true");
            int stops = rs.getInt("Stops");
            String planeType = rs.getString("Equipment");

            Route route = new Route(airline, sourceAirport, destinationAirport, codeshare, stops, planeType);
            route.setId(id);
            route.setDistance(0);
            routes.add(route);

            addToComboBoxList(airlineCodes, airline);
            addToComboBoxList(departureCountries, sourceAirport);
            addToComboBoxList(destinationCountries, destinationAirport);
            addToComboBoxList(planeTypes, planeType);

        }
        dataTable.setItems(routes);
        initialiseSliders();
    }

    /**
     * Sorts and sets the FX observable lists to each respective combobox and
     * uses ControlsFX to make these comboboxes searchable.
     * filterdata() is also called here because filtering of the table is based on combobox selections
     * and is required to be refreshed whenever a new dataset is chosen to be displayed.
     */
    @Override
    public void initialiseComboBoxes() {
        // Sort and set combobox items
        FXCollections.sort(airlineCodes); airlineFilterCombobox.setItems(airlineCodes);
        FXCollections.sort(departureCountries); departureFilterCombobox.setItems(departureCountries);
        FXCollections.sort(destinationCountries); destinationFilterCombobox.setItems(destinationCountries);
        FXCollections.sort(planeTypes); planeTypeFilterCombobox.setItems(planeTypes);

        // Make combobox searching autocomplete
        TextFields.bindAutoCompletion(airlineFilterCombobox.getEditor(), airlineFilterCombobox.getItems());
        TextFields.bindAutoCompletion(departureFilterCombobox.getEditor(), departureFilterCombobox.getItems());
        TextFields.bindAutoCompletion(destinationFilterCombobox.getEditor(), destinationFilterCombobox.getItems());
        TextFields.bindAutoCompletion(planeTypeFilterCombobox.getEditor(), planeTypeFilterCombobox.getItems());


        filterData();

    }

    /**
     * Filtering of table data is done here by initialising then iteratively adding each combobox/slider filter
     * to a FilteredList object. Each filter requires addFilter(). Then the search bar filter
     * is added through addSearchBar(). Finally the resulting SortedList is bound to the TableView dataTable
     * and the result of the filtering is shown to the user.
     */
    @Override
    public void filterData() {
        // Connect combobox and slider filters to table
        FilteredList<Route> airlinesFilter = addFilter(new FilteredList<>(routes, p -> true), airlineFilterCombobox, "Airline");
        FilteredList<Route> sourceFilter = addFilter(airlinesFilter, departureFilterCombobox, "Source");
        FilteredList<Route> stopSliderFilter = new FilteredList<>(sourceFilter, p -> true);

        stopsLabel.textProperty().addListener((observableValue, oldValue, newValue) ->
                stopSliderFilter.setPredicate((route -> (Integer.parseInt(newValue) == route.getNumStops()))));


        FilteredList<Route> destinationFilter = addFilter(stopSliderFilter, destinationFilterCombobox, "Destination");
        FilteredList<Route> planeFilter = addFilter(destinationFilter, planeTypeFilterCombobox, "Plane");


        // Add search bar filter
        FilteredList<Route> searchFilter = addSearchBarFilter(planeFilter);
        sortedRoute = new SortedList<>(searchFilter);
        sortedRoute.comparatorProperty().bind(dataTable.comparatorProperty());

        dataTable.setItems(sortedRoute);

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
    public FilteredList<Route> addFilter(FilteredList<Route> filteredList, ComboBox<String> comboBox, String filter) {
        FilteredList<Route> newFilter = new FilteredList<>(filteredList, p -> true);
        comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                newFilter.setPredicate(route -> {
                    if (newValue == null || newValue.equals("---")) {
                        return true;
                    }
                    String lower = newValue.toLowerCase();

                    if (filter.equals("Airline")) {
                        return route.getAirlineCode().toLowerCase().contains(lower);
                    } else if (filter.equals("Source")) {
                        return route.getSourceAirportCode().toLowerCase().contains(lower);
                    } else if (filter.equals("Destination")) {
                        return route.getDestinationAirportCode().toLowerCase().contains(lower);
                    } else {
                        return route.getPlaneTypeCode().toLowerCase().contains(lower);
                    }

                }));
        return newFilter;
    }

    /**
     * Adds holistic search bar filter which searches the route's Airline Code, Source Airport Code, Destination Airport
     * Code, and Plant Type Code.
     * @param filteredList the last filter to be added to before the search bar.
     * @return FilteredList with the search bar filter added.
     */
    private FilteredList<Route> addSearchBarFilter(FilteredList<Route> filteredList) {
        FilteredList<Route> searchFilter = new FilteredList<>(filteredList, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) ->
                searchFilter.setPredicate(route -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lower = newValue.toLowerCase();
                    if (route.getAirlineCode().toLowerCase().contains(lower)) {
                        return true;
                    } else if (route.getSourceAirportCode().toLowerCase().contains(lower)) {
                        return true;
                    } else if (route.getDestinationAirportCode().toLowerCase().contains(lower)){
                        return true;
                    } else {
                        return (route.getPlaneTypeCode().toLowerCase().contains(lower));
                    }
                }));
        return searchFilter;
    }


    /**
     * Gets maximum number of stops present in the 'Route' table of the database. Used to set the maximum
     * value in the 'stops' slider filter.
     */
    public void setSliderMaxStops() {
        String query = "SELECT max(STOPS) FROM Route";
        try (Connection connection = DatabaseManager.connect();
             Statement stmt = connection.createStatement();
             ResultSet result = stmt.executeQuery(query);
            ) {
            int maxStop = result.getInt("max(STOPS)");
            stopsFilterSlider.setMax(maxStop);

        } catch (SQLException e) {
            stopsFilterSlider.setMax(DEFAULT_MAX_STOPS);
            ErrorController.createErrorMessage("Could not set maximum stops. Set to default max stops of " + DEFAULT_MAX_STOPS + ".", false);
        }
    }

    /**
     * Override the parent's abstract class as to return the new record FXML file relating to the Route class.
     * @return String the path to the NEW_ROUTE_FXML file.
     */
    @Override
    public String getNewRecordFXML() { return Path.NEW_ROUTE_FXML; }

    /**
     * Returns the 'Route' datatype specifically used for this controller.
     * @return DataType a new Route object.
     */
    @Override
    public DataType getDataType() {
        return new Route();
    }

    /**
     * Returns the JDBC/SQL query for selecting all rows from the 'Route' table.
     * @return String for the  JDBC/SQL query for selecting all rows from the 'Route' table.
     */
    @Override
    public String getTableQuery() {
        return "SELECT * FROM Route";
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
                routes.remove(row);
            }
            else {
                ErrorController.createErrorMessage("Can't delete record: \n" + row.toString(), false);
            }
        }
    }

    /**
     * Clears all combobox filters
     */
    @Override
    public void clearFilters() {
        airlineFilterCombobox.setValue("");
        departureFilterCombobox.setValue("");
        destinationFilterCombobox.setValue("");
        planeTypeFilterCombobox.setValue("");
    }

}
