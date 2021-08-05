package seng202.team4.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Performs the tests for Airline class.
 * Setters and getters are not tested.
 */

public class AirlineTest {

    /**
     * Tests whether getInsertStatement returns the correct statement
     * that adds the given airline values to the data base.
     */
    @Test
    public void testInsertStatement() {
        Airline airline = new Airline("Rainbow Air Canada","Rainbow Air CAN","RY","RAY","Rainbow CAN","Canada",true);
        String statement = airline.getInsertStatement(10);
        Assert.assertEquals("Insert into  " + airline.getTypeName() + " ('Name', 'Alias', 'IATA', 'ICAO', 'CallSign', 'Country', 'RecentlyActive', 'SetId') "
                + "Values ('Rainbow Air Canada', 'Rainbow Air CAN', 'RY', 'RAY', 'Rainbow CAN', 'Canada', 'true', '10');", statement);
    }

    /**
     * Test whether getValid returns a null value when given an invalid airline name.
     */
    @Test
    public void testGetValidName() {
        Airline airline = Airline.getValid("~","Rainbow Air CAN","RY","RAY","Rainbow CAN","Canada","Y", new ArrayList<>());
        Assert.assertEquals(null, airline);
    }

    /**
     * Test whether getValid returns a null value when given an invalid airline code.
     */
    @Test
    public void testGetValidCode() {
        Airline airline = Airline.getValid("Rainbow Air Canada","r@inb0w 4!r~","RY","RAY","Rainbow CAN","Canada","Y", new ArrayList<>());
        Assert.assertEquals(null, airline);
    }

    /**
     * Test whether getValid returns a null value when given an invalid IATA code.
     */
    @Test
    public void testGetValidIATA() {
        Airline airline = Airline.getValid("Rainbow Air Canada","Rainbow Air CAN","RYAN~","RAY","Rainbow CAN","Canada","Y", new ArrayList<>());
        Assert.assertEquals(null, airline);
    }

    /**
     * Test whether getValid returns a null value when given an invalid ICAO code.
     */
    @Test
    public void testGetValidICAO() {
        Airline airline = Airline.getValid("Rainbow Air Canada","Rainbow Air CAN","RY","RAYAB~","Rainbow CAN","Canada","Y", new ArrayList<>());
        Assert.assertEquals(null, airline);
    }

    /**
     * Test whether getValid returns a null value when given an invalid airline callsign.
     */
    @Test
    public void testGetValidCallSign() {
        Airline airline = Airline.getValid("Rainbow Air Canada","Rainbow Air CAN","RY","RAY","Ra!nbow C4N~","Canada","Y", new ArrayList<>());
        Assert.assertEquals(null, airline);
    }

    /**
     * Test whether getValid returns a null value when given an invalid country name.
     */
    @Test
    public void testGetValidCountry() {
        Airline airline = Airline.getValid("Rainbow Air Canada","Rainbow Air CAN","RY","RAY","Rainbow CAN","C4nada~","Y", new ArrayList<>());
        Assert.assertEquals(null, airline);
    }

    /**
     * Test whether the getValid returns a null value when given an invalid activity status.
     * Should only accept Y or N.
     */
    @Test
    public void testGetValidActivity() {
        Airline airline = Airline.getValid("Rainbow Air Canada","Rainbow Air CAN","RY","RAY","Rainbow CAN","Canada","S", new ArrayList<>());
        Assert.assertEquals(null, airline);
    }

    /**
     * Test whether the getValid returns an airline object when given a valid input
     * and that the object has the correct attributes.
     */
    @Test
    public void testGetValid() {
        Airline airline = new Airline();
        Airline newline = (Airline) airline.getValid(new String[]{"Rainbow Air Canada","Rainbow Air CAN","RY","RAY","Rainbow CAN","Canada","Y"}, new ArrayList<>());
        Assert.assertTrue(newline.equalsTest(new Airline("Rainbow Air Canada", "Rainbow Air CAN", "RY", "RAY", "Rainbow CAN", "Canada", true)));
    }
}