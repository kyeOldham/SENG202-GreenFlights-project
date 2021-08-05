package seng202.team4.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Performs the tests for Airline class.
 * Setters and getters are not tested.
 */

public class RouteTest {

    /**
     * Tests whether getInsertStatement returns the correct statement
     * that adds the given airport values to the database.
     */
    @Test
    public void testInsertStatement() {
        Route route = new Route("BA","SIN","MEL",true,0,"744");
        String statement = route.getInsertStatement(10);
        Assert.assertEquals("Insert into  " + route.getTypeName() + " ('Airline', 'SourceAirport', 'DestinationAirport', 'Codeshare', 'Stops', 'Equipment', 'Distance', 'SetID') "
                + "Values ('BA', 'SIN', 'MEL', 'true', '0', '744', '0.0', '10');", statement);
    }

    /**
     * Test whether getValid returns a null value when given an invalid airline IATA or ICAO code.
     */
    @Test
    public void testGetValidAirline() {
        Route route = Route.getValid("BA~","SIN","MEL","Y","0","744", new ArrayList<>());
        Assert.assertEquals(null, route);
    }

    /**
     * Test whether getValid returns a null value when given an invalid source airport IATA or ICAO code.
     */
    @Test
    public void testGetValidSrc() {
        Route route = Route.getValid("BA","SIN!!","MEL","Y","0","744", new ArrayList<>());
        Assert.assertEquals(null, route);
    }

    /**
     * Test whether getValid returns a null value when given an invalid destination airport IATA or ICAO code.
     */
    @Test
    public void testGetValidDest() {
        Route route = Route.getValid("BA","SIN","MEL!!","Y","0","744", new ArrayList<>());
        Assert.assertEquals(null, route);
    }

    /**
     * Test whether getValid returns a null value when given an invalid codeshare.
     */
    @Test
    public void testGetValidCodeShare() {
        Route route = Route.getValid("BA","SIN","MEL","S","0","744", new ArrayList<>());
        Assert.assertEquals(null, route);
    }

    /**
     * Test whether getValid returns a null value when given an invalid number of stops.
     */
    @Test
    public void testGetValidStops() {
        Route route = Route.getValid("BA","SIN","MEL","Y","0a","744", new ArrayList<>());
        Assert.assertEquals(null, route);
    }

    /**
     * Test whether getValid returns a null value when given an invalid equipment codes.
     */
    @Test
    public void testGetValidEquipment() {
        Route route = Route.getValid("BA","SIN","MEL","Y","0","744@", new ArrayList<>());
        Assert.assertEquals(null, route);
    }

    /**
     * Test whether the getValid returns a route object when given a valid input
     * and that the object has the correct attributes.
     */
    @Test
    public void testGetValid() {
        Route route = new Route();
        Route newroute = (Route) route.getValid(new String[]{"BA","SIN","MEL","Y","0","744"}, new ArrayList<>());
        Assert.assertTrue(newroute.equalsTest(new Route("BA", "SIN", "MEL", true, 0, "744")));
    }

}