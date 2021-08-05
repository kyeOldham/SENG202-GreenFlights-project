package seng202.team4.model;

import seng202.team4.controller.ErrorController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Performs calculations for the emissions tab of the application.
 * Responsible for calculating the distance and carbon emissions
 * of a route.
 */
public class Calculations {

    /**
     * Constant for the figure used as the Average Carbon emitted
     * per passenger per kilometre on a flight. This baseline figure
     * assumes the passenger is on a 65% capacity Boeing 737 with a fuel
     * use of 36.6g per passenger km.
     */
    private static final double AVG_PASSENGER_C02_EMITTED_KM = 0.115;
    /**
     * Constant for the radius of the earth in KM
     */
    private static final double EARTH_RADIUS_KM = 6371;
    /**
     * Constant for the cost to offset 1km of flying per
     * passenger. Assumes an offset payment of $14.79 NZD
     * per tonne of C02
     */
    private static final double C02_COST_TO_OFFSET_PER_KG = 0.01479;
    /**
     * Calculates the estimated carbon footprint of a passenger on the
     * provided route. This assumes each plane is a Boeing 737 at 65% capacity
     * Hence it is assumed 0.115kg of C02 is emitted per passenger per kilometre.
     * In the next release the fuel efficiency for different aircraft types will
     * be taken into account
     *
     * @param route Route to calculate carbon emissions for
     * @return double the calculated emissions figure for a single passenger
     */
    public static double calculateEmissions(Route route) {
        double distance = route.getDistance();
        return distance * AVG_PASSENGER_C02_EMITTED_KM;
    }

    /**
     * Calculates the distance between two airports, a departure airport with
     * airportCodeOne and a destination airport with airportCodeTwo. Gets each
     * airports coordinates from the Airport database table and uses the
     * Haversine formula to calculate the distance between these coordinates
     *
     * @param airportCodeOne String representing code of the departure airport
     * @param airportCodeTwo String representing code of the destination airport
     * @param stmt The given statement
     * @return double the calculated distance between two given airports
     * @throws Exception Exception
     */
    public static double calculateDistance(String airportCodeOne, String airportCodeTwo, Statement stmt) throws Exception {
        double lat1;
        double lat2;
        double long1;
        double long2;

        //Sets up query required for accessing the provided airports in the Airport database table
        String query = "SELECT Latitude,Longitude from Airport WHERE IATA = '" + airportCodeOne + "' OR IATA = '" + airportCodeTwo + "'";
        ArrayList<Double> lats = new ArrayList<>();
        ArrayList<Double> longs = new ArrayList<>();
        //Execute query, store results in result set
        try (ResultSet result = stmt.executeQuery(query);
            ) {
            //Store latitude and longitudes of airports
            while (result.next()) {
                lats.add(result.getDouble("Latitude"));
                longs.add(result.getDouble("Longitude"));
            }
        } catch (SQLException e) {
            String message = "Airports with selected IATA's do not exist";
            ErrorController.createErrorMessage(message, false);
        }
        //Convert latitude and longitude of airports to radians
        lat1 = Math.toRadians(lats.get(0));
        lat2 = Math.toRadians(lats.get(1));
        long1 = Math.toRadians(longs.get(0));
        long2 = Math.toRadians(longs.get(1));


        // Calculate distance between airports
        // Using the Haversine formula
        double dlon = long2 - long1;
        double dlat = lat2 - lat1;
        double arcInverse = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double twiceArcInverse = 2 * Math.asin(Math.sqrt(arcInverse));
        // Radius of earth in kilometers.
        //return distance to be stored in DB
        return (EARTH_RADIUS_KM * twiceArcInverse);
    }

    /**
     * Calculates the amount of money required to offset the amount of carbon emissions
     * from the selected route.
     * @param route the route to calculate the equivalent dollar donations from
     * @return      the dollar donations required to offset flight carbon emissions
     */
    public static double calculateDollarOffset(Route route) {
        double emission = route.getCarbonEmissions();
        return emission * C02_COST_TO_OFFSET_PER_KG;
    }

    /**
     * Calculates the equivalent number of trees required to offset the flight carbon emission
     * of the selected route.
     * @param route the route to calculate the equivalent trees required from
     * @return      the number of trees to offset flight carbon emissions
     */
    public static int calculateTreesEquivalent(Route route) {
        double dollars = route.getDollarOffset();
        return (int) Math.ceil(dollars);
    }
}
