package seng202.team4.model;

import seng202.team4.controller.ErrorController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Handles loading data into the database. The data can be loaded from either:
 * the default data, the user uploading a file or the user adding a new record
 */
public abstract class DataLoader {

    /**
     * Uploads data to the database from a file of a specific dataType with a name for the new set of data.
     * @param setName String the name of the new set of data.
     * @param file File the file that's being uploaded to the database.
     * @param dataType DataType the data type of new data that's being uploaded.
     * @return ArrayList an ArrayList of erroneous lines
     */
    public static ArrayList<String> uploadData(String setName, File file, DataType dataType) {
        try (Connection connection = DatabaseManager.connect();
             Statement stmt = connection.createStatement();
            ) {
            // Inserts a new set into the related dataType's set table
            String setInsertStatement = "INSERT INTO " + dataType.getSetName() + " ('NAME') VALUES ('" + setName + "');";
            stmt.executeUpdate(setInsertStatement);

            int setID = getSetID(setName, dataType, stmt);

            // Stores the invalid lines in the file
            ArrayList<String> invalidLines = new ArrayList<>();

            // Reads lines from file and adds valid lines to statement batch
            BufferedReader buffer = new BufferedReader(new FileReader(file));
            String line = buffer.readLine();
            while (line != null && line.trim().length() > 0) {
                ArrayList<String> errorMessage = new ArrayList<>();
                DataType data = dataType.getValid(line, errorMessage);
                if (data != null) {
                    stmt.addBatch(data.getInsertStatement(setID)); // Add to database
                }
                else {
                    if (errorMessage.size() > 0) {
                        invalidLines.add(line + " (" + errorMessage.get(0) + ")");
                    }
                }
                line = buffer.readLine();
            }

            stmt.executeBatch();
            connection.commit();
            return invalidLines;

        } catch (Exception e) {
            String message = "Failed uploading data.";
            ErrorController.createErrorMessage(message, false);
            return null;

        }
    }

    /**
     * Adds a new record to the database.
     * @param dataType DataType the type of data the new record is.
     * @param setName String the name of the set the data will be added to.
     * @return 'true' if record was successfully inserted into the database, 'false' otherwise
     */
    public static boolean addNewRecord(DataType dataType, String setName){
        try (Connection connection = DatabaseManager.connect();
             Statement stmt = connection.createStatement();
        ) {
            int setID = getSetID(setName, dataType, stmt);
            // Inserts the new record into the database
            stmt.executeUpdate(dataType.getInsertStatement(setID));
            connection.commit();
            return true;

        } catch (Exception e) {
            String message = "Unable to add new record.";
            ErrorController.createErrorMessage(message, false);
            return false;

        }
    }

    /**
     * Updates a given record in the database
     * @param dataType datatype to update, one of Airline, Airport, Route, FlightPath
     * @param setName set in which the record sits
     * @return true of record is updated successfully, false otherwise
     */
    public static boolean updateRecord(DataType dataType, String setName) {
        try (Connection connection = DatabaseManager.connect();
             Statement stmt = connection.createStatement();
             ) {
            int setID = getSetID(setName, dataType, stmt);
            // Inserts the new record into the database
            stmt.executeUpdate(dataType.getUpdateStatement(setID));
            connection.commit();
            return true;

        } catch (Exception e) {
            String message = "Unable to edit record.";
            ErrorController.createErrorMessage(message, false);
            return false;
        }
    }

    /**
     * Gets the ID of a set given the sets name and what data type it is.
     * @param setName String the name of the set.
     * @param dataType DataType the data type of the set.
     * @param stmt Statement the statement used to to execute the query.
     * @return int the ID of the set.
     * @throws SQLException SQL Exception
     */
    private static int getSetID(String setName, DataType dataType, Statement stmt) throws SQLException{
        String setIdQuery = "SELECT ID FROM " + dataType.getSetName() + " WHERE Name = '" + setName + "';";
        ResultSet rs = stmt.executeQuery(setIdQuery);
        rs.next();
        return rs.getInt("ID");
    }

    /**
     * Adds a route to the RoutesSelected database table
     * when the user clicks on its corresponding checkbox
     * The distance is also calculated via the Calculations
     * class before the route is inserted.
     *
     * @param route Route the route that has been selected
     *              with a checkbox
     */
    public static boolean addToRoutesSelectedDatabase(Route route) {
        try (Connection connection = DatabaseManager.connect();
             Statement stmt = connection.createStatement();
            ) {
            String between = "', '";

            String sourceAirport = route.getSourceAirportCode();
            String destAirport = route.getDestinationAirportCode();

            double distance = Calculations.calculateDistance(sourceAirport, destAirport, stmt);
            route.setDistance(distance);

            double carbonEmitted = Calculations.calculateEmissions(route);
            String query = "INSERT INTO RoutesSelected ('Airline', 'SourceAirport', 'DestinationAirport', 'Equipment', 'Distance', 'CarbonEmissions') "
                    + "VALUES ('"
                    + route.getAirlineCode().replaceAll("'", "''") + between
                    + route.getSourceAirportCode().replaceAll("'", "''") + between
                    + route.getDestinationAirportCode().replaceAll("'", "''") + between
                    + route.getPlaneTypeCode().replaceAll("'", "''") + between
                    + route.getDistance() + between
                    + carbonEmitted
                    + "');";
            stmt.executeUpdate(query);
            connection.commit();
            return true;
        } catch (Exception e) {
            ErrorController.createErrorMessage("This route has no associated airports so some key features " +
                    "such as map visualisation and emissions calculations cannot be done!", false);
            route.setSelect(false);
            return false;
        }
    }

    /**
     *  Removes a route from the RoutesSelected database table
     *  when its corresponding checkbox is unselected
     *
     * @param route Route the route to be removed
     * @return True if it passed
     */
    public static boolean removeFromRoutesSelectedDatabase(Route route) {
        try (Connection connection = DatabaseManager.connect();
             Statement stmt = connection.createStatement();
            ) {
        final String AND = "' and ";

        String query = "DELETE FROM RoutesSelected WHERE "
                + "Airline = '" + route.getAirlineCode().replaceAll("'", "''") + AND
                + "SourceAirport = '" + route.getSourceAirportCode().replaceAll("'", "''") + AND
                + "DestinationAirport = '" + route.getDestinationAirportCode().replaceAll("'", "''") + AND
                + "Equipment = '" + route.getPlaneTypeCode().replaceAll("'", "''") + "'";
            stmt.executeUpdate(query);
            connection.commit();
            return true;
        } catch (SQLException e) {
            ErrorController.createErrorMessage("Could not delete route:\n" + route.toString(), false);
            return false;
        }
    }

    /**
     * Adds a route to the AirportsSelected database table
     * when the user clicks on its corresponding checkbox
     *
     * @param airport Airport the airport that has been selected
     *              with a checkbox
     */
    public static void addToAirportsSelectedDatabase(Airport airport) {
        try (Connection connection = DatabaseManager.connect();
             Statement stmt = connection.createStatement();
            ) {
            final String BETWEEN = "', '";

            String query = "INSERT INTO AirportsSelected ('Name', 'Longitude', 'Latitude') "
                    + "VALUES ('"
                    + airport.getName().replaceAll("'", "''") + BETWEEN
                    + airport.getLongitude() + BETWEEN
                    + airport.getLatitude()
                    + "');";
            stmt.executeUpdate(query);
            connection.commit();
        } catch (SQLException e) {
            ErrorController.createErrorMessage("Could not select airport:\n" + airport.toString(), false);
            airport.setSelect(false);
        }
    }

    /**
     *  Removes an airport from the AirportsSelected database table
     *  when its corresponding checkbox is unselected
     *
     * @param airport Airport the airport to be removed
     * @return True if it passed
     */
    public static boolean removeFromAirportsSelectedDatabase(Airport airport) {
        try (Connection connection = DatabaseManager.connect();
             Statement stmt = connection.createStatement();
            ) {
            final String AND = "' and ";

            String query = "DELETE FROM AirportsSelected WHERE "
                    + "Name = '" + airport.getName() + AND
                    + "Longitude = '" + airport.getLongitude() + AND
                    + "Latitude = '" + airport.getLatitude() + "'";
            stmt.executeUpdate(query);
            connection.commit();
            return true;
        } catch (SQLException e) {
            ErrorController.createErrorMessage("Could not delete route:\n" + airport.toString(), false);
            return false;
        }
    }

    /**
     * Deletes a selected row from its corresponding database table
     * and the table view when the corresponding 'Delete' button
     * is clicked
     *
     * @param id int id of the object and record to be removed
     *           from the database table
     * @param table String the name of the database table where the record
     *              will be removed from
     * @return boolean true if delete successful, false otherwise
     */
    public static boolean deleteRecord(int id, String table) {
        String query = "Delete from " + table + " Where ID = " + id;
        try (Connection connection = DatabaseManager.connect();
             Statement stmt = connection.createStatement();
            ) {
            stmt.executeUpdate(query);
            connection.commit();
            return true;
        } catch (SQLException e) {
            String message = "Could not delete record with ID " + id + ".";
            ErrorController.createErrorMessage(message, false);
            return false;
        }
    }
}
