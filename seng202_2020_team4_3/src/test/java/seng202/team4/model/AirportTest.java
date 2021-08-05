package seng202.team4.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Performs the tests for Airline class.
 * Simple setters and getters are not tested.
 */

public class AirportTest {

    /**
     * Test whether setCoordinates sets the latitude and longitude in the
     * expected format.
     */
    @Test
    public void testSetCoordinates() {
        Airport airport = new Airport();
        airport.setCoordinates(42.0, 13.5);
        String coordinates = airport.getCoordinates();
        Assert.assertEquals("42.00, 13.50", coordinates);
    }

    /**
     * Tests whether getInsertStatement returns the correct statement
     * that adds the given airport values to the database.
     */
    @Test
    public void testInsertStatement() {
        Airport airport = new Airport("Thule Air Base","Thule","Greenland","THU","BGTL",76.531203,-68.703161,251,-4,'E',"America/Thule");
        String statement = airport.getInsertStatement(5);
        Assert.assertEquals("Insert into  " + airport.getTypeName() + " ('Name', 'City', 'Country', 'IATA', 'ICAO', 'Latitude', 'Longitude', 'Altitude', 'TimeZone', 'DST', 'TzDatabaseTime', 'SetId') "
                + "Values ('Thule Air Base', 'Thule', 'Greenland', 'THU', 'BGTL', '76.531203', '-68.703161', '251', '-4.0', 'E', 'America/Thule', '5');", statement);
    }

    /**
     * Test whether getValid returns a null value when given an invalid airport name.
     */
    @Test
    public void testGetValidName() {
        Airport airport = Airport.getValid("~","Thule","Greenland","THU","BGTL","76.531203","-68.703161","251","-4","E","America/Thule", new ArrayList<>());
        Assert.assertEquals(null, airport);
    }

    /**
     * Test whether getValid returns a null value when given an invalid city name.
     */
    @Test
    public void testGetValidCity() {
        Airport airport = Airport.getValid("Thule Air Base","~","Greenland","THU","BGTL","76.531203","-68.703161","251","-4","E","America/Thule", new ArrayList<>());
        Assert.assertEquals(null, airport);
    }

    /**
     * Test whether getValid returns a null value when given an invalid country name.
     */
    @Test
    public void testGetValidCountry() {
        Airport airport = Airport.getValid("Thule Air Base","Thule","~","THU","BGTL","76.531203","-68.703161","251","-4","E","America/Thule", new ArrayList<>());
        Assert.assertEquals(null, airport);
    }

    /**
     * Test whether getValid returns a null value when given an invalid IATA code.
     */
    @Test
    public void testGetValidIATA() {
        Airport airport = Airport.getValid("Thule Air Base","Thule","Greenland","T","BGTL","76.531203","-68.703161","251","-4","E","America/Thule", new ArrayList<>());
        Assert.assertEquals(null, airport);
    }

    /**
     * Test whether getValid returns a null value when given an invalid ICAO code.
     */
    @Test
    public void testGetValid5ICAO() {
        Airport airport = Airport.getValid("Thule Air Base","Thule","Greenland","THU","B","76.531203","-68.703161","251","-4","E","America/Thule", new ArrayList<>());
        Assert.assertEquals(null, airport);
    }

    /**
     * Test whether getValid returns a null value when given an invalid latitude.
     */
    @Test
    public void testGetValidLatitude() {
        Airport airport = Airport.getValid("Thule Air Base","Thule","Greenland","THU","BGTL","76.a","-68.703161","251","-4","E","America/Thule", new ArrayList<>());
        Assert.assertEquals(null, airport);
    }

    /**
     * Test whether getValid returns a null value when given an invalid longitude.
     */
    @Test
    public void testGetValidLongitude() {
        Airport airport = Airport.getValid("Thule Air Base","Thule","Greenland","THU","BGTL","76.531203","-68fb","251","-4","E","America/Thule", new ArrayList<>());
        Assert.assertEquals(null, airport);
    }

    /**
     * Test whether getValid returns a null value when given an invalid altitude.
     */
    @Test
    public void testGetValidAltitude() {
        Airport airport = Airport.getValid("Thule Air Base","Thule","Greenland","THU","BGTL","76.531203","-68.703161","1d","-4","E","America/Thule", new ArrayList<>());
        Assert.assertEquals(null, airport);
    }

    /**
     * Test whether getValid returns a null value when given an invalid timezone.
     */
    @Test
    public void testGetValidTimeZone() {
        Airport airport = Airport.getValid("Thule Air Base","Thule","Greenland","THU","BGTL","76.531203","-68.703161","251","-u","E","America/Thule", new ArrayList<>());
        Assert.assertEquals(null, airport);
    }

    /**
     * Test whether getValid returns a null value when given an invalid daylight savings time.
     */
    @Test
    public void testGetValidDST() {
        Airport airport = Airport.getValid("Thule Air Base","Thule","Greenland","THU","BGTL","76.531203","-68.703161","251","-4","h","America/Thule", new ArrayList<>());
        Assert.assertEquals(null, airport);
    }

    /**
     * Test whether getValid returns a null value when given an invalid Tz database time.
     */
    @Test
    public void testGetValidTZDB() {
        Airport airport = Airport.getValid("Thule Air Base","Thule","Greenland","THU","BGTL","76.531203","-68.703161","251","-4","h","America", new ArrayList<>());
        Assert.assertEquals(null, airport);
    }

    /**
     * Test whether the getValid returns an airport object when given a valid input
     * and that the object has the correct attributes.
     */
    @Test
    public void testGetValid() {
        Airport airport = new Airport();
        Airport newport = (Airport) airport.getValid(new String[]{"Thule Air Base","Thule","Greenland","THU","BGTL","76.531203","-68.703161","251","-4","E","America/Thule"}, new ArrayList<>());
        Assert.assertTrue(newport.equalsTest(new Airport("Thule Air Base", "Thule", "Greenland", "THU", "BGTL", 76.531203, -68.703161, 251, -4, 'E', "America/Thule")));
    }

}