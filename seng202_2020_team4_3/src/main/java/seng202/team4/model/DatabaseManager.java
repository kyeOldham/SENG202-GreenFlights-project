package seng202.team4.model;

import seng202.team4.controller.ErrorController;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Performs basic interaction with the database that bypasses
 * the errors thrown by the database. Creation of database tables,
 * connection and disconnection to the database and the set up of a
 * database directory on the users machine are performed by
 * the DatabaseManager class.
 */
public abstract class DatabaseManager {

    /**
     * General DDL statement for creation of
     * each data types set table
     */
    private static final String SET_TABLE =
            "(" +
            "\"ID\" INTEGER NOT NULL UNIQUE," +
            "\"Name\" STRING NOT NULL," +
            "PRIMARY KEY(\"ID\" AUTOINCREMENT)" +
            ")";

    /**
     * String for creating the Airline set table.
     */
    private static final String AIRLINE_SET_TABLE = "CREATE TABLE \"AirlineSet\" " + SET_TABLE;
    /**
     * String with DDL statement for creating the Airline table in SQLite.
     */
    private static final String AIRLINE_TABLE =
            "CREATE TABLE \"Airline\" (" +
            "\"ID\" INTEGER NOT NULL UNIQUE," +
            "\"Name\" STRING," +
            "\"Alias\" STRING," +
            "\"IATA\" STRING," +
            "\"ICAO\" STRING," +
            "\"Callsign\" STRING," +
            "\"Country\" STRING," +
            "\"RecentlyActive\" STRING," +
            "\"SetID\" INTEGER NOT NULL," +
            "FOREIGN KEY (SetID) REFERENCES AirlineSet (SetID)," +
            "PRIMARY KEY(\"ID\" AUTOINCREMENT)" +
            ")";

    /**
     * String for creating the Airport set table.
     */
    private static final String AIRPORT_SET_TABLE = "CREATE TABLE \"AirportSet\" " + SET_TABLE;
    /**
     * String with DDL statement for creating the Airport table in SQLite.
     */
    private static final String AIRPORT_TABLE =
            "CREATE TABLE \"Airport\" (" +
            "\"ID\" INTEGER NOT NULL UNIQUE," +
            "\"Name\" STRING," +
            "\"City\" STRING," +
            "\"Country\" STRING," +
            "\"IATA\" STRING," +
            "\"ICAO\" STRING," +
            "\"Latitude\" DOUBLE," +
            "\"Longitude\" DOUBLE," +
            "\"Altitude\" DOUBLE," +
            "\"Timezone\" DOUBLE," +
            "\"DST\" STRING," +
            "\"TzDatabaseTime\" STRING," +
            "\"SetID\" INTEGER NOT NULL," +
            "FOREIGN KEY (SetID) REFERENCES AirportSet (SetID)," +
            "PRIMARY KEY(\"ID\" AUTOINCREMENT)" +
            ")";

    /**
     * String for creating the Route set table.
     */
    private static final String ROUTE_SET_TABLE = "CREATE TABLE \"RouteSet\" " + SET_TABLE;
    /**
     * String with DDL statement for creating the Route table in SQLite.
     */
    private static final String ROUTE_TABLE =
            "CREATE TABLE \"Route\" (" +
            "\"ID\" INTEGER NOT NULL UNIQUE," +
            "\"Airline\" STRING," +
            "\"SourceAirport\" STRING," +
            "\"DestinationAirport\" STRING," +
            "\"Codeshare\" STRING," +
            "\"Stops\" INTEGER," +
            "\"Equipment\" STRING," +
            "\"Distance\" INTEGER," +
            "\"SetID\" INTEGER NOT NULL," +
            "FOREIGN KEY (SetID) REFERENCES RouteSet (SetID)," +
            "PRIMARY KEY(\"ID\" AUTOINCREMENT)" +
            ")";

    /**
     * String for creating the Flight path set table.
     */
    private static final String FLIGHT_PATH_SET_TABLE = "CREATE TABLE \"FlightPathSet\" " + SET_TABLE;
    /**
     * String with DDL statement for creating the Flight path table in SQLite.
     */
    private static final String FLIGHT_PATH_TABLE =
            "CREATE TABLE \"FlightPath\" (" +
            "\"ID\"\tINTEGER NOT NULL UNIQUE," +
            "\"Type\" STRING," +
            "\"FlightPathID\" INTEGER," +
            "\"Altitude\" INTEGER," +
            "\"Latitude\" DOUBLE," +
            "\"Longitude\" DOUBLE," +
            "\"SetID\" INTEGER NOT NULL," +
            "FOREIGN KEY (SetID) REFERENCES FlightPathSet (SetID)," +
            "PRIMARY KEY(\"ID\" AUTOINCREMENT)" +
            ")";

    /**
     * String with DDL statement for creating the Routes selected table in SQLite.
     */
    private static final String ROUTES_SELECTED_TABLE =
            "CREATE TABLE \"RoutesSelected\" (" +
            "\"ID\"\tINTEGER NOT NULL UNIQUE," +
            "\"Airline\" STRING," +
            "\"SourceAirport\" STRING," +
            "\"DestinationAirport\" STRING," +
            "\"Equipment\" STRING," +
            "\"Distance\" INTEGER," +
            "\"CarbonEmissions\" INTEGER," +
            "PRIMARY KEY(\"ID\")" +
            ")";

    /**
     * String with DDL statement for creating the Airports selected table in SQLite.
     */
    private static final String AIRPORTS_SELECTED_TABLE =
            "CREATE TABLE \"AirportsSelected\" (" +
            "\"ID\"\tINTEGER NOT NULL UNIQUE," +
            "\"Name\" STRING," +
            "\"Longitude\" DOUBLE," +
            "\"Latitude\" DOUBLE," +
            "PRIMARY KEY(\"ID\" AUTOINCREMENT)" +
            ")";

    /**
     * Gets a new {@link java.sql.Connection Connection}
     * @throws SQLException exception to catch database access errors.
     * @return a new {@link java.sql.Connection Connection} if connection does not throw an error, otherwise 'null'
     */
    public static Connection connect() throws SQLException {

        try {
            Class.forName("org.sqlite.JDBC"); // TODO: Is this needed?
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection connection= DriverManager.getConnection(Path.DATABASE_CONNECTION);
        connection.setAutoCommit(false);
        return connection;
    }

    /**
     * Initial setup for the application. Creates a
     * new directory for the application's database
     * and a new database if they do not exist.
     */
    static void setUp() {
        // Creates a new directory and database if the directory doesn't exist.
        File directory = new File(Path.DIRECTORY);
        if (!directory.exists()) {
            directory.mkdir();
            newDatabase();
        }

        else {
            // If directory exists but database does not a new database is created.
            File database = new File(Path.DATABASE);
            if (!database.exists()) {
                newDatabase();
            }
        }
    }

    /**
     * Creates a new database inserting all the tables
     * required for the database.
     */
    private static void newDatabase () {
        try (Connection connection = DatabaseManager.connect();
             Statement stmt = connection.createStatement();
             ) {
            stmt.addBatch(AIRLINE_SET_TABLE);
            stmt.addBatch(AIRPORT_SET_TABLE);
            stmt.addBatch(ROUTE_SET_TABLE);
            stmt.addBatch(FLIGHT_PATH_SET_TABLE);

            stmt.addBatch(AIRLINE_TABLE);
            stmt.addBatch(AIRPORT_TABLE);
            stmt.addBatch(ROUTE_TABLE);
            stmt.addBatch(FLIGHT_PATH_TABLE);

            stmt.addBatch(ROUTES_SELECTED_TABLE);
            stmt.addBatch(AIRPORTS_SELECTED_TABLE);
            stmt.executeBatch();
            connection.commit();

        } catch (SQLException e) {
            String message = "Unable to connect to create database, application will exit.";
            ErrorController.createErrorMessage(message, true);
        }
    }
}
