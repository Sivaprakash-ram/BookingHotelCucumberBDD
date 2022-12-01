package org.retail.com.TestRunner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import static org.retail.com.Utilities.Constants.*;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {FEATURES_DIR},
        glue={STEP_DEFINITIONS_DIR},
        plugin = {"pretty", "html:target/cucumber-report.html"},
        tags = "@UITest"

)

public class runCucumberTest {
}