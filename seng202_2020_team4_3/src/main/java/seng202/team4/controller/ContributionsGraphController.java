package seng202.team4.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import seng202.team4.model.Route;

/**
 * Performs the logic for generating a visualization
 * for the contribution of each selected route to the
 * users overall carbon footprint. This is a JavaFX
 * BarChart this is displayed in a new window when the
 * 'Contribution Graph' is clicked on the Emissions tab
 */
public class ContributionsGraphController {

    /**
     * the JavaFX BarChart being displayed
     */
    @FXML private BarChart<String, Double> contributionChart;
    /**
     * The x axis for the bar chart, will have the details of each route
     */
    @FXML private CategoryAxis x;
    /**
     * The y axis for the barchart for the carbon emissions in C02 kg per passenger
     */
    @FXML private NumberAxis y;

    /**
     * Sets up the BarChart by creating an XYChart data object for each
     * route selected by the user and adding them to the contributionChart
     *
     * @param selectedRoutes ObservableList the routes the user has currently selected
     *                       via the routes tab
     */
    public void setUp(ObservableList<Route> selectedRoutes) {
        contributionChart.setLegendVisible(false);
        x.setLabel("Routes selected");
        y.setLabel("Carbon Emissions per passenger (Kg-C02)");
        XYChart.Series<String, Double> routeSeries = new XYChart.Series<>();
        routeSeries.setName("Selected Routes");
        for(Route route: selectedRoutes){
            String routeID = route.getAirlineCode() + ": " + route.getSourceAirportCode() + "-" + route.getDestinationAirportCode();
            routeSeries.getData().add(new XYChart.Data<>(routeID, route.getCarbonEmissions()));
        }
        contributionChart.getData().addAll(routeSeries);
    }


}
