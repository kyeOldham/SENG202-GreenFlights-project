package seng202.team4.model;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Performs the tests for DataLoader class.
 * Simple setters and getters are not tested.
 */

public class DataLoaderTest {

    /**
     * Copies the provided file to a target file.
     *
     * @param fileName String name of source file
     * @return File the final file with the source files contents
     * @throws IOException if an exception occurs during the copy operation
     */
    public File copyToFolder(String fileName) throws IOException {
        InputStream initialStream = (this.getClass().getResourceAsStream(fileName));
        File targetFile = new File(Path.DIRECTORY + fileName);
        java.nio.file.Files.copy(initialStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return targetFile;
    }

    /**
     * Set up the database to be used for the test.
     */
    @BeforeClass
    public static void setup() {
        DatabaseManager.setUp();
    }

    /**
     * Deletes database after each test
     */
    @AfterClass
    public static void teardown() {
        Main.deleteDatabase();
    }

    /**
     * Uploads a file full of valid airline data
     * to the database using the uploadData method
     *
     * Tests whether 0 invalid lines are received
     *
     * @throws IOException exception to catch error
     * in copyToFolder
     */
    @Test
    public void uploadDataTestValid() throws IOException {
        ArrayList<String> invalidMsg;
        File testData = copyToFolder(Path.AIRLINE_TEST_RSC_VALID);
        invalidMsg = DataLoader.uploadData("Default", testData, new Airline());
        Assert.assertEquals(invalidMsg.size(), 0);
    }

    /**
     * Uploads a file full of invalid airline data
     * to the database using the uploadData method
     *
     * Tests whether 1 invalid lines are received
     * upload should stop when error encountered
     *
     * @throws IOException exception to catch error
     * in copyToFolder
     */
    @Test
    public void uploadDataTestInvalid() throws IOException {
        ArrayList<String> invalidLines;
        File testDataWrong = copyToFolder(Path.AIRLINE_TEST_RSC_INVALID);
        invalidLines = DataLoader.uploadData("Default", testDataWrong, new Airline());
        Assert.assertEquals(invalidLines.size(), 1);
    }

    /**
     * Tests whether a new record object can be added to
     * the database. A dataset is created first for the
     * record to be added too. addNewRecord should return
     * true if the record is succesfully added
     *
     * @throws IOException exception to catch error
     * in copyToFolder
     */
    @Test
    public void addNewRecordTest() throws IOException {
        File testData = copyToFolder(Path.AIRLINE_TEST_RSC_RECORD);
        DataLoader.uploadData("Default", testData, new Airline());
        Airline toInsert = new Airline("213 Flight Unit","test","","TFU","","Russia",false);
        boolean insertResult = DataLoader.addNewRecord(toInsert, "Default");
        Assert.assertEquals(insertResult, true);
    }

    /**
     * Test whether a route object can be added to
     * the RoutesSelected table. The RoutesSelected
     * table is queried after the addToRoutesSelectedDatabase
     * method is called. Returns true if add is succesful
     *
     * @throws SQLException exception to catch database errors
     */
    @Test
    public void addRouteToSelectedTest() throws IOException {
        File airport = copyToFolder(Path.AIRPORT_TEST_RSC_VALID);
        DataLoader.uploadData("Default", airport, new Airport());
        Route toAdd = new Route("CZ","AKL","CAN",false,0,"787");
        boolean addResult = DataLoader.addToRoutesSelectedDatabase(toAdd);
        Assert.assertEquals(addResult, true);
    }

    /**
     * Test whether a route object can be deleted from
     * the RoutesSelected table. The RoutesSelected
     * table is queried after the addToRoutesSelectedDatabase
     * method and then removeFromRoutesSelected method are called.
     * Count should be 0
     *
     * @throws SQLException exception to catch database errors
     */
    @Test
    public void deleteRouteFromSelectedTest() throws SQLException {
        Route toAdd = new Route("CZ","AKL","CAN",false,0,"787");
        DataLoader.addToRoutesSelectedDatabase(toAdd);
        DataLoader.removeFromRoutesSelectedDatabase(toAdd);
        String countCheck = "SELECT count(*) from RoutesSelected where Airline = '" + toAdd.getAirlineCode() +
                "' and SourceAirport = '" + toAdd.getSourceAirportCode() + "' and DestinationAirport = '" +
                toAdd.getDestinationAirportCode() + "';";
        try (Connection connection = DatabaseManager.connect();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(countCheck)) {
            int count = rs.getInt("count(*)");
            Assert.assertEquals(count, 0);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(false);
        }
    }

    /**
     * Test whether an airport object can be added to
     * the AirportsSelected table. The AirportsSelected
     * table is queried after the addToAirportsSelectedDatabase
     * method is called. Count should be 1
     *
     * @throws SQLException exception to catch database errors
     */
    @Test
    public void addAirportToSelectedTest() throws SQLException {
        Airport toAdd = new Airport("Egilsstadir","Egilsstadir","Iceland","EGS","BIEG",65.283333,-14.401389,Integer.parseInt("76"),Float.parseFloat("0"),"N".charAt(0),"Atlantic/Reykjavik");
        DataLoader.addToAirportsSelectedDatabase(toAdd);
        String countCheck = "SELECT count(*) from AirportsSelected where name = '" + toAdd.getName() +
                "';";
        try (Connection connection = DatabaseManager.connect();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(countCheck)) {
            int count = rs.getInt("count(*)");
            Assert.assertEquals(count, 1);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(false);
        }
    }

    /**
     * Test whether an airport object can be deleted from
     * the AirportsSelected table. The AirportsSelected
     * table is queried after the addToRoutesSelectedDatabase
     * method and then removeFromAirportsSelected method are called.
     * Count should be 0
     *
     * @throws SQLException exception to catch database errors
     */
    @Test
    public void deleteAirportFromSelectedTest() throws SQLException {
        Airport toAdd = new Airport("Egilsstadir","Egilsstadir","Iceland","EGS","BIEG",65.283333,-14.401389,Integer.parseInt("76"),Float.parseFloat("0"),"N".charAt(0),"Atlantic/Reykjavik");
        DataLoader.addToAirportsSelectedDatabase(toAdd);
        DataLoader.removeFromAirportsSelectedDatabase(toAdd);
        String countCheck = "SELECT count(*) from AirportsSelected where name = '" + toAdd.getName() +
                "';";
        try (Connection connection = DatabaseManager.connect();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(countCheck)) {
            int count = rs.getInt("count(*)");
            Assert.assertEquals(count, 0);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(false);
        }
    }

    /**
     * Tests that a record can be deleted from the
     * database tables. A datatable is first populated
     * with data. deleteRecord will return true if a
     * record has been successfully deleted
     *
     * @throws IOException exception to catch error
     * in copyToFolder
     */
    @Test
    public void deleteRecordTest() throws IOException {
        File testData = copyToFolder(Path.AIRLINE_TEST_RSC_DELETE);
        DataLoader.uploadData("Default", testData, new Airline());
        boolean deleteResult = DataLoader.deleteRecord(10, "Airline");
        Assert.assertEquals(deleteResult, true);
    }

    /**
     * Tests that a record can be updated for the
     * database tables. A datatable is first populated
     * with data. updateRecord will return true if a
     * record has been successfully updated
     *
     * @throws IOException exception to catch error
     * in copyToFolder
     */
    @Test
    public void updateRecordTest() throws IOException {
        File testData = copyToFolder(Path.AIRLINE_TEST_RSC_UPDATE);
        DataLoader.uploadData("Default", testData, new Airline());
        Airline toUpdate = new Airline("213 Flight Unit","test","","TFU","","Russia",false);
        boolean updateResult = DataLoader.updateRecord(toUpdate, "Default");
        Assert.assertEquals(updateResult, true);
    }
}
