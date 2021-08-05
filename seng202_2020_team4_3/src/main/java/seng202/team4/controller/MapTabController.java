package seng202.team4.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.controlsfx.control.textfield.TextFields;
import seng202.team4.model.DatabaseManager;
import seng202.team4.model.Path;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Performs the logic for the 'Map' tab of the application.
 * Responsible for filtering and displaying route and airport data using the Google Maps API with the
 * JavaFX WebView. Filtering is done through searchable comboboxes, where the selected filters
 * are passed through to the map.html HTML/Javascript script to be executed - here, the
 * airport or routes are shown on the map. Radio button selection is used
 * to switch between route and airport filtering.
 */
public class MapTabController {
    /**
     * WebView that will host the Google Map.
     */
    @FXML private WebView googleMapView;
    /**
     * A button that refreshes route comboboxes and map by calling init()
     */
    @FXML private Button refreshButtonRoute;
    /**
     * A button that refreshes airport comboboxes and map by calling init()
     */
    @FXML private Button refreshButtonAirport;
    /**
     * A radio container that contains the route and airport filter showing radio buttons.
     */
    @FXML private ToggleGroup filterSelection;
    /**
     * Radio button that when selected shows the route filters.
     */
    @FXML private RadioButton routeFilterRadio;
    /**
     * Grid container that holds all the filters for routes.
     */
    @FXML private GridPane routeFilterGrid;
    /**
     * Grid container the holds all the filters for airports.
     */
    @FXML private GridPane airportFilterGrid;
    /**
     * Searchable combobox for filtering by airline code.
     */
    @FXML private ComboBox routeAirlineFilterCombobox;
    /**
     * Searchable combobox for filtering by airport code.
     */
    @FXML private ComboBox routeAirportFilterCombobox;
    /**
     * Searchable combobox for filtering by plane type.
     */
    @FXML private ComboBox routePlaneTypeFilterCombobox;
    /**
     * Searchable combobox for filtering by country.
     */
    @FXML private ComboBox airportCountryFilterCombobox;

    /**
     * Mutable ObservableList containing a list of airline codes for the routeAirlineFilterCombobox.
     */
    private ObservableList<String> airlineCodes = FXCollections.observableArrayList();
    /**
     * Mutable ObservableList containing a list of countries for the airportCountryFilterCombobox.
     */
    private ObservableList<String> departureCountries = FXCollections.observableArrayList();
    /**
     * Mutable ObservableList containing a list of plane types for the routePlaneTypeFilterCombobox.
     */
    private ObservableList<String> planeTypes = FXCollections.observableArrayList();
    /**
     * Mutable ObservableList containing a list of airports for the airportCountryFilterCombobox.
     */
    private ObservableList<String> airportCountries = FXCollections.observableArrayList();

    /**
     * Limit for how many routes can be shown on the map (to avoid clutter).
     */
    private static final int ROUTELIMIT = 400;
    /**
     * Web engine used to load the Google Map onto the WebView.
     */
    private WebEngine webEngine;
    /**
     * SQL query used to find longitude and latitude for a specific airport.
     */
    private static String airportCoordQuery = "SELECT Longitude, Latitude, Name FROM Airport WHERE IATA = '%s'";

    /**
     * Initialises map by calling init(). Abstracted as init has to be called by other methods too.
     */
    @FXML
    public void initialize() {
        initialiseRefreshButtons();
        init();
    }

    /**
     * Initialise refresh buttons by setting their images and tooltips.
     */
    private void initialiseRefreshButtons() {
        Image refreshImage = new Image(getClass().getResourceAsStream(Path.REFRESH_BUTTON_PNG));
        refreshButtonRoute.setGraphic(new ImageView(refreshImage));
        refreshButtonAirport.setGraphic(new ImageView(refreshImage));
        refreshButtonRoute.setTooltip(new Tooltip("Refresh drop-down menus"));
        refreshButtonAirport.setTooltip(new Tooltip("Refresh drop-down menus"));
    }

    /**
     * Executes set of instructions for initialising the map tab.
     * Initialises the map, then the radio buttons, then finally the comboboxes.
     */
    @FXML
    private void init() {
        initMap();
        initialiseRadioButtons();
        initialiseComboBoxes();
    }

    /**
     * Initialises the Google Map by creating a new WebEngine for the JavaFX WebView then loading the
     * map.html resource onto the WebEngine.
     */
    private void initMap() {
        webEngine = googleMapView.getEngine();
        webEngine.load(getClass().getResource(Path.MAP_RSC).toExternalForm());
    }

    /**
     * Adds a listener to the radio toggle group such that switching between radio buttons means switching
     * between different filters shown. Uses the displayRouteFilters() and displayAirportAirlineFilters()
     * methods.
     */
    private void initialiseRadioButtons() {
        filterSelection.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == routeFilterRadio) {
                displayRouteFilters();
            } else {
                displayAirportAirlineFilters();
            }

        });
    }

    /**
     * Populates filtering comboboxes by querying the SQL database.
     * Uses initialiseRouteComboBoxes() to initialises the route grid comboboxes.
     * Catches exceptions caused by SQL errors.
     */
    private void initialiseComboBoxes() {
        initialiseRouteComboBoxes();
        initialiseAirportComboBoxes();
    }

    /**
     * Specifically initialises/populates the route filtering comboboxes by querying
     * the SQL database.
     * @throws SQLException thrown if there is an error querying the SQL database
     */
    private void initialiseAirportComboBoxes() {
        try (Connection connection = DatabaseManager.connect();
             Statement stmt = connection.createStatement();
            ResultSet airportResultSet = stmt.executeQuery("SELECT Country FROM Airport");
        ) {
            while (airportResultSet.next()) {
                String country = airportResultSet.getString("Country");
                if (!airportCountries.contains(country)) {
                    airportCountries.add(country);
                }
            }
        } catch (SQLException e) {
            ErrorController.createErrorMessage("Could not initialize airline combo boxes.", false);
        }
        FXCollections.sort(airportCountries); airportCountryFilterCombobox.setItems(airportCountries);
        TextFields.bindAutoCompletion(airportCountryFilterCombobox.getEditor(), airportCountryFilterCombobox.getItems());
    }

    /**
     * Specifically initialises/populates the route filtering comboboxes by querying
     * the SQL database.
     */
    private void initialiseRouteComboBoxes() {
        try (Connection connection = DatabaseManager.connect();
            Statement stmt = connection.createStatement();
            ResultSet routesResultSet = stmt.executeQuery("SELECT Airline, SourceAirport, Equipment FROM Route");
        ) {
            while (routesResultSet.next()) {
                String airline = routesResultSet.getString("Airline");
                String sourceAirport = routesResultSet.getString("SourceAirport");
                String planeType = routesResultSet.getString("Equipment");

                if (!airlineCodes.contains(airline)) {
                    airlineCodes.add(airline);
                }
                if (!departureCountries.contains(sourceAirport)) {
                    departureCountries.add(sourceAirport);
                }
                if (!planeTypes.contains(planeType)) {
                    planeTypes.add(planeType);
                }
            }
        } catch (SQLException e) {
            ErrorController.createErrorMessage("Could not initialize route combo boxes.", false);
        }
        FXCollections.sort(airlineCodes); routeAirlineFilterCombobox.setItems(airlineCodes);
        FXCollections.sort(departureCountries); routeAirportFilterCombobox.setItems(departureCountries);
        FXCollections.sort(planeTypes); routePlaneTypeFilterCombobox.setItems(planeTypes);
        // Make combobox searching autocomplete
        TextFields.bindAutoCompletion(routeAirlineFilterCombobox.getEditor(), routeAirlineFilterCombobox.getItems());
        TextFields.bindAutoCompletion(routeAirportFilterCombobox.getEditor(), routeAirportFilterCombobox.getItems());
        TextFields.bindAutoCompletion(routePlaneTypeFilterCombobox.getEditor(), routePlaneTypeFilterCombobox.getItems());

    }

    /**
     * Toggles on displaying of the route filer grid.
     * Toggles off the displaying of the airport filter grid.
     */
    private void displayRouteFilters() {
        routeFilterGrid.setVisible(true);
        airportFilterGrid.setVisible(false);
    }

    /**
     * Toggles off displaying of the route filer grid.
     * Toggles on the displaying of the airport filter grid.
     */
    private void displayAirportAirlineFilters() {
        routeFilterGrid.setVisible(false);
        airportFilterGrid.setVisible(true);
    }

    /**
     * Clears map and then shows the routes selected from the Routes tab by querying the
     * RoutesSelected table. Catches any error/exception caused by SQL querying. Each route is shown separately
     * using showOneRoute(). Activated when 'Show Selected Routes' button is clicked in the GUI.
     */
    @FXML
    private void showSelectedRoutes() {
        clearMap();
        try (Connection connection = DatabaseManager.connect();
             Statement stmt = connection.createStatement();
             ResultSet routesResultSet = stmt.executeQuery("SELECT SourceAirport, DestinationAirport FROM RoutesSelected");
        ) {
            for (int count = 0; routesResultSet.next() && count < ROUTELIMIT; count++) {
                showOneRoute(routesResultSet, connection);
            }

        } catch (SQLException e) {
            String message = "Unable to connect to SQLite database";
            ErrorController.createErrorMessage(message, false);
        }
        repositionMap("Routes");

    }

    /**
     * Clears map then, using values in the comboboxes, shows filtered results of routes in the map.
     * Routes must be less than ROUTELIMIT. All SQL Exceptions are handled here.
     * Activated when the 'Apply Button' in the route filters grid is clicked.
     */
    @FXML
    private void routeApplyFilter() {
        clearMap();
        String airline = (String) routeAirlineFilterCombobox.getValue();
        String airport = (String) routeAirportFilterCombobox.getValue();
        String planeType = (String) routePlaneTypeFilterCombobox.getValue();

        airline = getValidInput(airline);
        airport = getValidInput(airport);
        planeType = getValidInput(planeType);

        String query = String.format("SELECT SourceAirport, DestinationAirport FROM Route WHERE Airline is %s and SourceAirport is %s and Equipment is %s",
                airline, airport, planeType);


        try (Connection connection = DatabaseManager.connect();
             Statement stmt = connection.createStatement();
             ResultSet filteredResultSet = stmt.executeQuery(query);
        ) {
            for (int count = 0; filteredResultSet.next() && count < ROUTELIMIT; count++) {
                showOneRoute(filteredResultSet, connection);
            }
        } catch (SQLException e) {
            ErrorController.createErrorMessage("Could not apply route filter.", false);
        }
        repositionMap("Routes");
    }


    /**
     * Shows one route on the Google Map by querying the Airport table for coordinates then
     * using the WebEngine to execute a javascript script from map.html.
     * @param routesResultSet JDBC ResultSet of all routes that need to be shown (only top one is shown here).
     * @param connection The database connection.
     * @throws SQLException When there is an error when interacting with the SQL database using JDBC.
     */
    private void showOneRoute(ResultSet routesResultSet, Connection connection) throws SQLException {
        String sourceAirportIATA = routesResultSet.getString("SourceAirport");
        String destinationAirportIATA = routesResultSet.getString("DestinationAirport");
        try (ResultSet sourceAirportQuery = connection.createStatement().executeQuery(String.format(airportCoordQuery, sourceAirportIATA));
             ResultSet destAirportQuery = connection.createStatement().executeQuery(String.format(airportCoordQuery, destinationAirportIATA));
             ) {

            sourceAirportQuery.next();
            destAirportQuery.next();
            double sourceLatitude = (sourceAirportQuery.getDouble("Latitude"));
            double sourceLongitude = (sourceAirportQuery.getDouble("Longitude"));
            String sourceName = (sourceAirportQuery.getString("Name"));

            double destLatitude = (destAirportQuery.getDouble("Latitude"));
            double destLongitude = (destAirportQuery.getDouble("Longitude"));
            String destName = (destAirportQuery.getString("Name"));

            String routePoints = String.format("[{lat: %f, lng: %f}, {lat: %f, lng: %f}, ]",
                    sourceLatitude, sourceLongitude, destLatitude, destLongitude);

            sourceName = sourceName.replaceAll("'", "");
            destName = destName.replaceAll("'", "");

            String scriptToExecute = "addRoute(" + routePoints + ", '" + sourceName + "', '" + destName + "');";
            executeScript(scriptToExecute);
        } catch (SQLException e) {
            ErrorController.createErrorMessage("Could not show route from '" + sourceAirportIATA + "' to '" + destinationAirportIATA + "'.", false);
        }
    }

    /**
     * Clears map and then shows the airports selected from the Airports tab by querying the
     * AirportsSelected table. Catches any error/exception caused by SQL querying. Each airport is shown separately
     * using showOneAirport(). Activated when 'Show Selected Airports' button is click in the GUI.
     */
    @FXML
    public void showSelectedAirports() {
        clearMap();
        try (Connection connection = DatabaseManager.connect();
             Statement stmt = connection.createStatement();
             ResultSet airportResultSet = stmt.executeQuery("SELECT Name, Longitude, Latitude FROM AirportsSelected");
            ) {
            showAirportsOnMap(airportResultSet);
        } catch (SQLException e) {
            ErrorController.createErrorMessage("Could not show selected airports.", false);
        }
        repositionMap("Airports");

    }

    /**
     * Given a ResultSet of Airports from SQL, show all these airports on the Google Maps using showOneAirport() for each.
     * @param airportResultSet ResultSet of airports to show
     * @throws SQLException thrown when there is an error with resultset.next() method
     */
    private void showAirportsOnMap(ResultSet airportResultSet) throws SQLException {
        while (airportResultSet.next()) {
            showOneAirport(airportResultSet);
        }
        clusterMarkers();
    }

    /**
     * Shows one airport on the Google Map by using the given ResultSet for coordinates then
     * using the WebEngine to execute a javascript script from map.html.
     * @param airportResultSet JDBC ResultSet of all routes that need to be shown (only top Airport is shown here).
     * @throws SQLException When there is an error when interacting with the SQL database using JDBC.
     */
    private void showOneAirport(ResultSet airportResultSet) throws SQLException {
        double sourceLatitude = (airportResultSet.getDouble("Latitude"));
        double sourceLongitude = (airportResultSet.getDouble("Longitude"));
        String sourceName = (airportResultSet.getString("Name"));
        String airportPoint = String.format("{lat: %f, lng: %f}", sourceLatitude, sourceLongitude);
        String scriptToExecute = "addAirport(" + airportPoint + ", \"" + sourceName + "\");";
        executeScript(scriptToExecute);
    }

    /**
     * Clears map then, using values in the comboboxes, shows filtered results of airports in the map.
     * All SQL Exceptions are handled here.
     * Activated when the 'Apply Button' in the airport filters grid is clicked.
     */
    @FXML
    private void airportApplyFilter() {
        clearMap();
        String country = (String) airportCountryFilterCombobox.getValue();
        country = getValidInput(country);
        String query = String.format("SELECT Longitude, Latitude, Name FROM Airport WHERE Country is %s", country);

        try (Connection connection = DatabaseManager.connect();
             Statement stmt = connection.createStatement();
            ResultSet airportResultSet = stmt.executeQuery(query);
            ) {
            showAirportsOnMap(airportResultSet);
        } catch (SQLException e) {
            ErrorController.createErrorMessage("Could not apply airport filter.", false);
        }
        // Reposition map only if there is a filter input
        if (!country.equals("not null")) {
            repositionMap("Airports");
        }

    }

    /**
     * Gets valid inputs for the SQL queries in routeApplyFilter() and airportApplyFilter().
     * If inputString == null this means that no value is entered in the combobox(es) so
     * it means 'all' of that type are chosen, so in SQL this means 'is not null' for getting every value.
     * @param inputString Value obtained from the respective combobox.
     * @return Either a String "not null" or "'inputString'"
     */
    private String getValidInput(String inputString) {
        if (inputString == null || inputString.length() == 0) {
            return "not null";
        } else {
            return String.format("'%s'", inputString);
        }
    }

    /**
     * Uses clearMap() method in map.html javascript to remove all points/routes on the Google Map.
     * Activated when the 'Clear Map' button is clicked in the GUI (and is used by other methods in this class).
     */
    @FXML
    private void clearMap() {
        String scriptToExecute = "clearMap();";
        executeScript(scriptToExecute);
    }

    /**
     * Make airport markers cluster in Google Maps
     */
    public void clusterMarkers() {
        String scriptToExecute = "makeClusters();";
        executeScript(scriptToExecute);
    }

    /**
     * Reposition map according to either routes or airports
     * @param filter either 'Routes' or 'Airports' to determine which markers to reposition on
     */
    private void repositionMap(String filter) {
        String scriptToExecute;
        if (filter.equals("Routes")) {
            scriptToExecute = "repositionMapByRoutes();";
        } else {
            scriptToExecute = "repositionMapByAirports();";
        }
        executeScript(scriptToExecute);
    }

    /**
     * Connects the Java String for a JavaScript method execution to the map.html script.
     * Converts Java to JavaScript and uses the WebEngine to execute the script.
     * @param scriptToExecute Javascript script to execute.
     */
    public void executeScript(String scriptToExecute) {
        webEngine.executeScript(scriptToExecute);

    }



}
