package seng202.team4;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(glue="seng202/team4/steps",
        features = "src/main/resources",
        plugin = {"pretty", "html:target/cucumber.html"},
        snippets = CucumberOptions.SnippetType.CAMELCASE)
public class RunCucumberTest {
}
