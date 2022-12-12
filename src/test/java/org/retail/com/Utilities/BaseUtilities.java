package org.retail.com.Utilities;

import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class BaseUtilities implements Constants {

    /*
     * Initialization of Driver and Hooks Method Initialization goes here
     */

    public static WebDriver driver;
    public static String environment;
    public static Scenario scenario;
    public static Properties envConfig;
    public static String runID;
    public static Properties configProp = getProps(Constants.CONFIG_DIR, "config");
    public static String scenarioRunID;

    public void loadingPropFile(Scenario scenario) {
        BaseUtilities.scenario = scenario;
        environment = System.getProperty("env" , "CONFIG");
        System.out.println("environment:" + environment);
        if (environment.equalsIgnoreCase("CONFIG")){
            envConfig = configProp;
        }
    }

    public void createWebDriver(Scenario scenario) {
        BaseUtilities.scenario = scenario;
        String webdriver = System.getProperty("browser", "edge");
        try {
            if ("seleniumhubchrome".equals(webdriver)) {
                WebDriverManager.chromedriver().setup();
                DesiredCapabilities cap = new DesiredCapabilities();
                cap.setCapability("browserName" , "chrome");
                driver = new RemoteWebDriver(new URL("http://172.31.240.1:4444/wd/hub"), cap);
                driver.manage().window().maximize();
                driver.manage().deleteAllCookies();
                driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
                return;
            } else if ("chrome".equals(webdriver)) {
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                driver.manage().window().maximize();
                driver.manage().deleteAllCookies();
                driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
                return;
            } else if ("seleniumhubedge".equals(webdriver)) {
                WebDriverManager.edgedriver().setup();
                DesiredCapabilities cap = new DesiredCapabilities();
                cap.setCapability("browserName" , "MicrosoftEdge");
                driver = new RemoteWebDriver(new URL("http://172.31.240.1:4444/wd/hub"), cap);
                driver.manage().window().maximize();
                driver.manage().deleteAllCookies();
                driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
                return;
            }
            else if ("edge".equals(webdriver)) {
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                driver.manage().window().maximize();
                driver.manage().deleteAllCookies();
                driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
                return;
            }
            throw new RuntimeException("Unsupported webdriver: " + webdriver);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static Properties getProps(final String FILE_PATH, final String FILE_NAME) {
        Properties properties = null;
        try {
            File file = new File(FILE_PATH, FILE_NAME + ".properties");
            properties = new Properties();
            //Reading the properties file
            properties.load(new FileInputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static String getPropValue(Properties properties, String key) {

        //Returing the Property value depends on the Key
        return properties.getProperty(key);
    }

    public void logInfo(String msg, String info) {
        {
            scenario.attach(msg, "text/plain" , info);
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

    public void createSceenshotFolder(Scenario scenario) {
        runID = getCurretDateTime("dd_MM_yyyy_HH_mm_ss", "BST");
        String testcaseID = "Missing_TestCase_ID";
        if (scenario.getSourceTagNames() != null) {
            for (String tag : scenario.getSourceTagNames()) {
                if (tag.contains("@testcaseID=")) {
                    testcaseID = tag.split("testcaseID=")[1];
                    break;
                }
            }
        } else {
            logInfo("Tag not present ", "Tag not present ");
        }
        scenarioRunID = testcaseID + "-" + runID;
        try
        {
           final File htmlTemplateFile = new File(screenshotFolderPath + "screenshotTemplate_Sample.html");
           File newHtmlFile = new File(screenshotFolderPath + scenarioRunID + "/" + scenarioRunID + ".html");
           String newhtmlStr = FileUtils.readFileToString(htmlTemplateFile, "UTF-8");
           newhtmlStr = newhtmlStr.replace("$testcaseID$", testcaseID);
           newhtmlStr = newhtmlStr.replace("$SName$", scenario.getName());
           newhtmlStr = newhtmlStr.replace("$Etime$", runID.replace("_", " "));
           FileUtils.writeStringToFile(newHtmlFile, newhtmlStr, "UTF-8");
        } catch (IOException io) {
            logFail("Failed to create screenshot file");
        }
    }

    public void takeScreenshot(String message) throws IOException {
        String scrImage = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        File newHtmlFile = new File(screenshotFolderPath + scenarioRunID + "/" + scenarioRunID + ".html");
        String htmlString = FileUtils.readFileToString(newHtmlFile, "UTF-8");
        String newDiv = "<div>   <h3>" + message + "</h3><img src=\"data:image/png;base64, " + scrImage + "\" ></div>--End--";
        htmlString = htmlString.replace("--End--", newDiv);
        FileUtils.writeStringToFile(newHtmlFile, htmlString, "UTF-8");
    }

    public String getCurretDateTime(String format, String timeZoneID) {
        String dateTime = "";
        try {
            DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern(format);
            Instant inst = Instant.now();
            if (timeZoneID.equalsIgnoreCase("BST")) {
                timeZoneID = "Europe/London";
            }
            ZoneId id = ZoneId.of(timeZoneID);
            ZonedDateTime zdt = inst.atZone(id);
            dateTime = zdt.format(dateTimeFormat);
        } catch (Exception e) {
            logFail(e.getMessage());
        }
        return dateTime;
    }

}