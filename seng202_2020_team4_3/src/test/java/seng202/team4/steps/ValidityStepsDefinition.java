package seng202.team4.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import seng202.team4.model.Airline;
import seng202.team4.model.Airport;
import seng202.team4.model.Route;

import java.util.ArrayList;

public class ValidityStepsDefinition {

    String[] testAirline;

    @Given("I am adding new airline data")
    public void iAmAddingNewAirlineData() {
        testAirline = new String[]{"New Air", "New Air", "NA", "NEA", "NEWAIR", "Pluto", "Y"};
    }

    @When("I enter {string} airline name")
    public void iEnterAirlineName(String name) {
        testAirline[0] = name;
    }

    @When("I enter {string} airline IATA")
    public void iEnterAirlineIATA(String iata) {
        testAirline[2] = iata;
    }

    @When("I enter {string} airline ICAO")
    public void iEnterAirlineICAO(String icao) {
        testAirline[3] = icao;
    }

    @When("I enter {string} airline country")
    public void iEnterAirlineCountry(String country) {
        testAirline[5] = country;
    }

    @Then("airline should be valid")
    public void airlineShouldBeValid() {
        Assert.assertNotNull(new Airline().getValid(testAirline, new ArrayList<>()));
    }

    @Then("airline should not be valid")
    public void airlineShouldNotBeValid() {
        Assert.assertNull(new Airline().getValid(testAirline, new ArrayList<>()));
    }

    String[] testAirport;

    @Given("I am adding new airport data")
    public void iAmAddingNewAirportData() {
        testAirport = new String[]{"Name","City","Country","IAT","ICAO","76.53","-68.70","251","-4","E","Area/Location"};
    }

    @When("I enter {string} airport IATA")
    public void iEnterAirportIATA(String iata) {
        testAirport[3] = iata;
    }

    @When("I enter {string} airport name")
    public void iEnterAirportName(String name) {
        testAirport[0] = name;
    }

    @When("I enter {string} airport latitude")
    public void iEnterAirportLatitude(String lat) {
        testAirport[5] = lat;
    }

    @When("I enter {string} airport timezone")
    public void iEnterAirportTimezone(String timezone) {
        testAirport[8] = timezone;
    }

    @When("I enter {string} airport DST")
    public void iEnterAirportDST(String dst) {
        testAirport[9] = dst;
    }

    @When("I enter {string} airport Tz database time")
    public void iEnterAirportTzDatabaseTime(String tzdb) {
        testAirport[10] = tzdb;
    }

    @Then("airport should be valid")
    public void airportShouldBeValid() {
        Assert.assertNotNull(new Airport().getValid(testAirport, new ArrayList<>()));
    }

    @Then("airport should not be valid")
    public void airportShouldNotBeValid() {
        Assert.assertNull(new Airport().getValid(testAirport, new ArrayList<>()));
    }

    String testRoute[];

    @Given("I am adding new route data")
    public void iAmAddingNewRouteData() {
        testRoute = new String[]{"2B","EGO","KGD","Y","0","CR2"};
    }

    @When("I enter {string} route airline")
    public void iEnterRouteAirline(String airline) {
        testRoute[0] = airline;
    }

    @Then("route should be valid")
    public void routeShouldBeValid() {
        Assert.assertNotNull(new Route().getValid(testRoute, new ArrayList<>()));
    }

    @Then("route should not be valid")
    public void routeShouldNotBeValid() {
        Assert.assertNull(new Route().getValid(testRoute, new ArrayList<>()));
    }

    @When("I enter {string} route source")
    public void iEnterRouteSource(String src) {
        testRoute[1] = src;
    }

    @When("I enter {string} route destination")
    public void iEnterRouteDestination(String dst) {
        testRoute[2] = dst;
    }

    @When("I enter {string} route code share")
    public void iEnterRouteCodeShare(String cs) {
        testRoute[3] = cs;
    }

    @When("I enter {string} route stops")
    public void iEnterRouteStops(String stops) {
        testRoute[4] = stops;
    }
}
