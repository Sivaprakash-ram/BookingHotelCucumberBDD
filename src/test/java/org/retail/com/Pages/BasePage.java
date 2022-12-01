package org.retail.com.Pages;

import org.junit.Assert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.retail.com.Utilities.BaseUtilities;
import java.util.concurrent.TimeUnit;

public class BasePage extends BaseUtilities {
    public static final long TIMEOUT = 30;

    //Constructor
    public BasePage(WebDriver driver) {
        BaseUtilities.driver = driver;

    }

    public void isDisplayedThenClick(WebElement locator, String elementName) {
        if (locator.isDisplayed())
        {
            locator.click();
            logPass(elementName + " is Dispayed and Clicked on it");
        } else {
            logFail(elementName + " is Not Displayed");
        }
    }

    public void logPass(String msg) {
        {
            scenario.attach(msg, "text/plain" , msg);
        }
    }

    public void logFail(String msg) {
        {
            scenario.attach(msg, "text/plain" , msg);
            if(driver!=null) {
                scenario.attach(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES), "image/png", "Failed Screenshot");
            }
            Assert.fail(msg);
        }
    }
}