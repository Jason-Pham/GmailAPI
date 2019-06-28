package _runners;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.TestNGCucumberRunner;
import org.testng.annotations.Test;

@CucumberOptions(
        features = "src/test/resources/features",
        tags = "@API",
        plugin = {"pretty", "com.vimalselvam.cucumber.listener.ExtentCucumberFormatter:target/Gmail-reports/report.html"},
        glue = "step_definitions")

public class TestRunner {
    @Test()
    public void scenario() {
        new TestNGCucumberRunner(getClass()).runCukes();
    }
}

