package runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/InvoiceFlow.feature",
        glue = "stepDefinitions",
        plugin = {
                "pretty",
                "html:target/cucumber-reports.html", // Ini Auto-Generated Report
                "json:target/cucumber.json"
        },
        monochrome = true
)
public class TestRunner {
}