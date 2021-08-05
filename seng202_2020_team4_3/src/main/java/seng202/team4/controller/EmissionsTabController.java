package seng202.team4.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.math3.util.Precision;
import seng202.team4.model.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Performs logic for the 'Emissions' tab of the application
 * Responsible for connecting the selected route data to the JavaFX interface,
 * this includes displaying the selected routes in the JavaFX TableView with data
 * from the 'RoutesSelected' SQLite database table and also using a search filter
 * on this data. Analysis is performed on this data, specifically the distance and
 * carbon emissions for each route is calculated. The suggested donation amount to
 * offset a users carbon footprint is calculated, along with how many trees they
 * could plant with this derived figure.
 */
public class EmissionsTabController extends DataController {

    /**
     * Initialization of users suggested dollar offset amount
     */
    private double totalDonation = 0.0;
    /**
     * Initialization of the trees the user could plant with dollarOffset
     */
    private int totalTrees = 0;

    /**
     * TableView of the selected routes raw data table
     */
    @FXML private TableView<Route> dataTable;
    /**
     * Airline code column of the raw data table
     */
    @FXML private TableColumn<Route, String> airlineColumn;
    /**
     * Departure airport IATA column of the raw data table
     */
    @FXML private TableColumn<Route, String> sourceColumn;
    /**
     * Destination airport IATA column of the raw data table
     */
    @FXML private TableColumn<Route, String> destinationColumn;
    /**
     * Plane type column of the raw data table
     */
    @FXML private TableColumn<Route, String> planeColumn;
    /**
     * Distance column of the raw data table
     */
    @FXML private TableColumn<Route, Integer> distanceColumn;
    /**
     * Carbon emissions (C02) column of the raw data table
     */
    @FXML private TableColumn<Route, Integer> emissionsColumn;
    /**
     * Text field used to search the data table
     */
    @FXML private TextField searchField;
    /**
     * Current emissions label displaying the users total carbon footprint for
     * all routes in the data table
     */
    @FXML private Label currentEmissionsValue;
    /**
     * Mutable ObservableList containing Route objects
     */
    private ObservableList<Route> selectedRoutes;

    /**
     *  On Action method for the 'Environmental Donation' button
     *  Gets the calculated donation amount and displays this
     *  in a new Alert window
     *
     */
    @FXML
    public void pressEnvironmentalDonationButton() {
        String donationDollarsCents = String.format("%.2f", totalDonation);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Environmental Donation Equivalent");
        alert.setHeaderText("Donation required to offset your emissions: $" + donationDollarsCents + " NZD");

        FlowPane fp = new FlowPane();
        Label explainText = new Label("Donations can be made at the carbonfund.org site");
        fp.getChildren().addAll(explainText);
        alert.getDialogPane().contentProperty().set( fp );
        alert.show();
    }

    /**
     * On Action method for the 'Trees Equivalent' button
     * Gets how many trees the user could plant with the donation amount
     * and displays this in a new Alert window
     */
    @FXML
    public void pressTreesEquivalentButton() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Trees Equivalent");
        alert.setHeaderText("With the suggested donation amount you could plant " + totalTrees + " trees");

        FlowPane treesFp = new FlowPane();
        Label explainText = new Label("Plant trees at the teamtrees.org site");
        treesFp.getChildren().addAll(explainText);
        alert.getDialogPane().contentProperty().set( treesFp );
        alert.show();
    }

    /**
     * On Action method for the 'Contributions Graph' button
     * Creates a bar chart that visualizes the contribution
     * of each selected route to the users overall carbon footprint
     * and shows it in a new modal stage
     */
    @FXML
    public void pressContributionsGraphButton() {
        Stage stage = new Stage();
        stage.setTitle("Route Contributions To Carbon Footprint");
        stage.setMinHeight(417);
        stage.setMinWidth(600);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Path.CONTRIBUTIONS_GRAPH));
        try {
            stage.setScene(new Scene(loader.load(), 600, 417));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        ContributionsGraphController contributions = loader.getController();
        //Set up the bar chart by passing it the users currently selected routes
        contributions.setUp(selectedRoutes);
    }


    /**
     * Holds the high level logic (set of instructions) for initialisation.
     * Initialisation order: Table Columns, Set Table
     */
    public void initialize() {
        //Connects table columns to their respective Route class attribute
        airlineColumn.setCellValueFactory(new PropertyValueFactory<>("airlineCode"));
        sourceColumn.setCellValueFactory(new PropertyValueFactory<>("sourceAirportCode"));
        destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destinationAirportCode"));
        planeColumn.setCellValueFactory(new PropertyValueFactory<>("planeTypeCode"));
        distanceColumn.setCellValueFactory(new PropertyValueFactory<>("distance"));
        emissionsColumn.setCellValueFactory(new PropertyValueFactory<>("carbonEmissions"));

        // Multiple rows can be selected
        dataTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        try {
            initialiseButtons();
            setTable();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Calculates the total carbon emissions for the selected routes in
     * the data table. The recommended donation to offset the emissions
     * figure is calculated using this figure.
     */
    private void setTotalEmissions() {
        double sumEmissions = 0.0;
        totalDonation = 0.0;
        totalTrees = 0;
        for(Route route: selectedRoutes){
            sumEmissions += route.getCarbonEmissions();
            totalDonation += route.getDollarOffset();
            totalTrees += route.getTreeEquivalent();
        }

        currentEmissionsValue.setText(String.format("%.2f", sumEmissions) + "kg C02");
    }

    /**
     * On action method for the 'Load Selected Routes' button
     * updates the table to display the routes selected by the user
     * on the 'Routes' tab.
     */
    @FXML
    public void updateTable() {
        try {
            setTable();
            setTotalEmissions();
        } catch (Exception e) {
            String message = "Error on table update";
            ErrorController.createErrorMessage(message, false);
        }
    }

    /**
     * Required method from the abstract DataController class
     * @return DataType in this case it is of type 'Route'
     */
    @Override
    public DataType getDataType() {
        return new Route();
    }

    /**
     * Returns the JDBC/SQL query for selecting all distinct/unique rows from the 'RoutesSelected' table.
     * @return String for the  JDBC/SQL query for selecting all rows from the 'RoutesSelected' table.
     */
    @Override
    public String getTableQuery() {
        return "Select distinct Airline, SourceAirport, DestinationAirport, Equipment, distance, carbonEmissions from RoutesSelected";
    }

    /**
     * Sets the JavaFX table with rows from the 'RoutesSelected' database table.
     * This is done using the table query and assigning each record to a row in the table
     * @param rs JDBC ResultSet obtained from querying the Database RoutesSelected table and is used to set the rows
     *           of the JavaFX data table by creating N Route objects from the query that results in N tuples.
     * @throws SQLException if the query fails, throws an exception
     */
    @Override
    public void setTableData(ResultSet rs) throws SQLException {
        selectedRoutes = FXCollections.observableArrayList();
        while (rs.next()) {
            Route route = new Route();
            String airline = rs.getString("Airline");
            String sourceAirport = rs.getString("SourceAirport");
            String destinationAirport = rs.getString("DestinationAirport");
            String planeType = rs.getString("Equipment");

            route.setAirlineCode(airline);
            route.setSourceAirportCode(sourceAirport);
            route.setDestinationAirportCode(destinationAirport);
            route.setPlaneTypeCode(planeType);
            route.setDistance(Precision.round(rs.getDouble("distance"), 2));
            route.setCarbonEmissions(Precision.round(rs.getDouble("carbonEmissions"), 2));
            route.setDollarOffset(Calculations.calculateDollarOffset(route));
            route.setTreeEquivalent(Calculations.calculateTreesEquivalent(route));
            selectedRoutes.add(route);

        }
        dataTable.setItems(selectedRoutes);

    }

    /**
     * Filtering functionality for the Search field.
     * Filters the routes in the table view that have a value in
     * either of the: airlineCode, sourceAirportCode, destinationAirportCode or planeTypeCode
     * columns that matches the users search input
     * @return FilteredList of routes that match the input
     */
    private FilteredList<Route> searchBarFilter() {
        FilteredList<Route> searchFilter = new FilteredList<>(selectedRoutes, p -> true);
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
     * Required method from the abstract DataController class
     * calls the filterData method, to apply the filters selected
     * by the user
     */
    @Override
    public void initialiseComboBoxes() {
        filterData();
    }

    /**
     * Required method from the abstract DataController class
     * applies the search filter to the data table view
     */
    @Override
    public void filterData() {
        FilteredList<Route> searchFilter = searchBarFilter();
        SortedList<Route> sortedRoute = new SortedList<>(searchFilter);
        sortedRoute.comparatorProperty().bind(dataTable.comparatorProperty());

        dataTable.setItems(sortedRoute);

    }

    /**
     * Required method from the abstract DataController class
     * @return String an empty record as no new records are added
     * to the emissions table
     */
    @Override
    public String getNewRecordFXML() {
        return Path.NEW_ROUTE_FXML;
    }

    /**
     * Delete each row selected in the table view and database
     */
    @FXML
    @Override
    public void deleteRows() {
        DataType rows[] = dataTable.getSelectionModel().getSelectedItems().toArray(new DataType[0]);
        for(DataType row : rows) {
            boolean deleted = DataLoader.removeFromRoutesSelectedDatabase((Route) row);
            if (deleted) {
                selectedRoutes.remove(row);
            }
            else {
                ErrorController.createErrorMessage("Can't delete record: \n" + row.toString(), false);
            }
        }
    }

    /**
     * Clear filter method, not used in the emissions tab as there are no filters
     */
    @Override
    public void clearFilters() {

    }


}
