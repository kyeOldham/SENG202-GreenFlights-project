package seng202.team4.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import seng202.team4.model.Calculations;
import seng202.team4.model.Route;

public class StepDefinitions {
    Route route;

    @Given("there is a new Route")
    public void thereIsANewRoute() {
        route = new Route();
    }
    @When("I set the distance to {double}")
    public void iSetTheDistanceTo(double distance) {
        route.setDistance(distance);
    }
    @Then("the calculated carbon emissions should be {double}")
    public void theCalculatedCarbonEmissionsShouldBe(Double carbonEmissions) {
        Double emissions = Calculations.calculateEmissions(route);
        route.setCarbonEmissions(emissions);
        Assert.assertEquals(carbonEmissions, route.getCarbonEmissions(), 0.01);
    }
    @And("the dollar offset should be {double}")
    public void theDollarOffsetShouldBe(Double dollars) {
        Double dollarOffset = Calculations.calculateDollarOffset(route);
        route.setDollarOffset(dollarOffset);
        Assert.assertEquals(dollars, route.getDollarOffset(), 0.01);
    }
    @And("the trees equivalent should be {int}")
    public void theTreesEquivalentShouldBe(int trees) {
        Assert.assertEquals(trees, Calculations.calculateTreesEquivalent(route));
    }
}
