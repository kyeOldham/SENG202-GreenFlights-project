package seng202.team4.model;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test the validity of different data inputs for uploading files and adding records
 * into the application.
 */

public class ValidateTest {

    /**
     * Test whether numeric data input is a valid integer.
     */
    @Test
    public void testIsNumeric1() {
        String num1 = "123";
        Assert.assertTrue(Validate.isInteger(num1));
    }

    /**
     * Test whether numeric data input is a valid float.
     */
    @Test
    public void testIsNumeric2() {
        String num2 = "1.23";
        Assert.assertTrue(Validate.isFloat(num2));
    }

    /**
     * Test that negative integer is a valid integer.
     */
    @Test
    public void testIsNumeric3() {
        String num3 = "-123";
        Assert.assertTrue(Validate.isInteger(num3));
    }

    /**
     * Test that negative floats are valid floats.
     */
    @Test
    public void testIsNumeric4() {
        String num4 = "-1.23";
        Assert.assertTrue(Validate.isFloat(num4));
    }

    /**
     * Test that float must not contain non numeric character.
     */
    @Test
    public void testIsNumeric5() {
        String num5 = "a.23";
        Assert.assertFalse(Validate.isFloat(num5));
    }

    /**
     * Test that input integer should not be null.
     */
    @Test
    public void testIsNumeric6() {
        String num6 = null;
        Assert.assertFalse(Validate.isInteger(num6));
    }

    /**
     * Test that input float should not be null.
     */
    @Test
    public void testIsNumeric7() {
        String num6 = null;
        Assert.assertFalse(Validate.isFloat(num6));
    }

    /**
     * Test that one letter input is allowed.
     */
    @Test
    public void testIsAlpha1() {
        String str1 = "A";
        Assert.assertTrue(Validate.isAlpha(str1));
    }

    /**
     * Test that input with space characters are allowed.
     */
    @Test
    public void testIsAlpha2() {
        String str2 = "Air Port";
        Assert.assertTrue(Validate.isAlpha(str2));
    }

    /**
     * Test input should have at least one letter character.
     */
    @Test
    public void testIsAlpha3() {
        String str3 = "";
        Assert.assertFalse(Validate.isAlpha(str3));
    }

    /**
     * Test such that numeric characters are not allowed.
     */
    @Test
    public void testIsAlpha4() {
        String str4 = "Airport 4";
        Assert.assertFalse(Validate.isAlpha(str4));
    }

    /**
     * Test input should have at least one letter character.
     */
    @Test
    public void testIsAlpha5() {
        String str5 = "   ";
        Assert.assertFalse(Validate.isAlpha(str5));
    }

    /**
     * Test that input should not be null.
     */
    @Test
    public void testIsAlpha6() {
        String str6 = null;
        Assert.assertFalse(Validate.isAlpha(str6));
    }

    /**
     * Test such that alphanumeric inputs can contain
     * alpha numeric characters.
     */
    @Test
    public void testIsAlphaNumeric1() {
        String st1 = "Airport 123";
        Assert.assertTrue(Validate.isAlphaNumeric(st1));
    }

    /**
     * Test such that alphanumeric inputs should not contain
     * alphanumeric characters.
     */
    @Test
    public void testIsAlphaNumeric2() {
        String st2 = "Airport 123!$%";
        Assert.assertFalse(Validate.isAlphaNumeric(st2));
    }

    /**
     * Test to check whether null inputs are disallowed.
     */
    @Test
    public void testIsAlphaNumeric3() {
        String st3 = null;
        Assert.assertFalse(Validate.isAlphaNumeric(st3));
    }

    /**
     * Test to ensure airport IATAs contain 3 multilingual letter characters.
     */
    @Test
    public void testIsValidIATA1() {
        String iata1 = "ABC";
        Assert.assertTrue(Validate.isAirportIATA(iata1));
    }

    /**
     * Test to ensure airline IATAs contain 2 multilingual letter characters.
     */
    @Test
    public void testIsValidIATA2() {
        String iata2 = "AA";
        Assert.assertTrue(Validate.isAirlineIATA(iata2));
    }

    /**
     * Test to ensure airport IATAs contain 3 multilingual letter characters.
     */
    @Test
    public void testIsValidIATA3() {
        String iata3 = "";
        Assert.assertTrue(Validate.isAirportIATA(iata3));
    }

    /**
     * Test to ensure airport IATAs are not null.
     */
    @Test
    public void testIsValidIATA4() {
        String iata4 = null;
        Assert.assertFalse(Validate.isAirportIATA(iata4));
    }

    /**
     * Test to ensure airport IATAs contain 3 multilingual letter characters.
     */
    @Test
    public void testIsValidIATA5() {
        String iata5 = "ABCD";
        Assert.assertFalse(Validate.isAirportIATA(iata5));
    }

    /**
     * Test to ensure airport ICAOs contain 4 multilingual letter characters.
     */
    @Test
    public void testIsValidICAO1() {
        String icao1 = "ABCD";
        Assert.assertTrue(Validate.isAirportICAO(icao1));
    }

    /**
     * Test to ensure airport ICAOs contain more than 2 multilingual characters.
     */
    @Test
    public void testIsValidICAO2() {
        String icao2 = "AB";
        Assert.assertFalse(Validate.isAirportICAO(icao2));
    }

    /**
     * Test to ensure airport ICAOs contain at least 1 multilingual characters.
     */
    @Test
    public void testIsValidICAO3() {
        String icao3 = "";
        Assert.assertTrue(Validate.isAirportICAO(icao3));
    }

    /**
     * Test to ensure airport ICAOs is not null.
     */
    @Test
    public void testIsValidICAO4() {
        String icao4 = null;
        Assert.assertFalse(Validate.isAirportICAO(icao4));
    }

    /**
     * Test to ensure airline ICAOs contain 3 multilingual letter characters.
     */
    @Test
    public void testIsValidICAO5() {
        String icao5 = "ABC";
        Assert.assertTrue(Validate.isAirlineICAO(icao5));
    }

    /**
     * Test to ensure Tz Database Time is in Olson format.
     */
    @Test
    public void testIsValidTZDB1() {
        String tzdb1 = "America/Thule";
        Assert.assertTrue(Validate.isValidTZDB(tzdb1));
    }

    /**
     * Test to ensure Tz Database time contains both area and location.
     */
    @Test
    public void testIsValidTZDB2() {
        String tzdb2 = "America";
        Assert.assertFalse(Validate.isValidTZDB(tzdb2));
    }

    /**
     * Test to ensure Tz Database time contains valid area and location names.
     */
    @Test
    public void testIsValidTZDB3() {
        String tzdb3 = "Amer1ca/Thul3";
        Assert.assertFalse(Validate.isValidTZDB(tzdb3));
    }

    /**
     * Test to ensure Tz Database time is not null.
     */
    @Test
    public void testIsValidTZDB4() {
        String tzdb4 = null;
        Assert.assertFalse(Validate.isValidTZDB(tzdb4));
    }

    /**
     * Test to ensure timezone is numeric.
     */
    @Test
    public void testIsValidTimeZone1() {
        String tz = "aa";
        Assert.assertFalse(Validate.isValidTimeZone(tz));
    }

    /**
     * Test to ensure timezone exists.
     */
    @Test
    public void testIsValidTimeZone2() {
        String tz = "-13";
        Assert.assertFalse(Validate.isValidTimeZone(tz));
    }

    /**
     * Test to ensure timezone exists.
     */
    @Test
    public void testIsValidTimeZone3() {
        String tz = "15";
        Assert.assertFalse(Validate.isValidTimeZone(tz));
    }

    /**
     * Test to valid timezones get a pass.
     */
    @Test
    public void testIsValidTimeZone4() {
        String tz = "3";
        Assert.assertTrue(Validate.isValidTimeZone(tz));
    }

    /**
     * Test to ensure timezone is not null.
     */
    @Test
    public void testIsValidTimeZone5() {
        String tz = null;
        Assert.assertFalse(Validate.isValidTimeZone(tz));
    }

    /**
     * Test to multilingual characters are allowed.
     */
    @Test
    public void testIsValidAlphaMulti() {
        String alpha = "Hornafjörður";
        Assert.assertTrue(Validate.isAlphaMultiLanguage(alpha));
    }

    /**
     * Test to ascii characters are allowed.
     */
    @Test
    public void testIsAscii() {
        String str = "Dfs%#:)";
        Assert.assertTrue(Validate.isAsciiOrNull(str));
    }
}