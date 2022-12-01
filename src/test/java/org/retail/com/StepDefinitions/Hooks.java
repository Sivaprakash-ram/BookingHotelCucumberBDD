package org.retail.com.StepDefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import org.junit.Before;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.retail.com.Utilities.BaseUtilities;

public class Hooks extends BaseUtilities {


    @Before
    public void loadEnvProp(Scenario scenario) {
        loadingPropFile(scenario);
    }

    @io.cucumber.java.Before("@UITest")
    public void beforeScenario(Scenario scenario) {
        createWebDriver(scenario);
        createSceenshotFolder(scenario);
    }

    @After("@UITest")
    public void tearDown(Scenario scenario) {
        try {
            String screenshotName = scenario.getName().replaceAll("", "_");
            if (scenario.isFailed()) {
                TakesScreenshot ts = (TakesScreenshot) driver;
                byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", screenshotName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        driver.quit();
    }
}