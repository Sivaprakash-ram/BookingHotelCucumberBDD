package org.retail.com.Pages;

import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HomePage extends BasePage {

    //Constructor
    public HomePage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public String newDate;

    @FindBy (xpath = "//input[@id='date-start']")
    public WebElement dateStart;

    @FindBy (xpath = "//input[@id='to-place']")
    public WebElement noOfNights;

    @FindBy (xpath = "//input[@value='Book now !']")
    public WebElement bookNow;

    @FindBy (xpath = "//div[@class='panel-body']//following::div[@class='col-sm-2 h4']")
    public  List<WebElement> addOns;

    @FindBy (xpath = "//div[@class='panel-body']//following::div[@class='col-sm-2 h4'][1]")
    public  WebElement addOns1;

    @FindBy (xpath = "//h2[contains(text(),'Deluxe')]//following::table//h4[contains(text(),'Rack Rate Standard')]//following::span/a")
    public WebElement deluxeRoom;

    @FindBy (xpath = "//input[@value='Add the selected services >>']")
    public WebElement addSelectedServices;

    @FindBy (xpath = " //h2[contains(text(),'Clock PMS+ Demo Hotel Web Reservation System.')]")
    public WebElement addOnsTitle;
    @FindBy (xpath = "//input[@id='booking_guest_attributes_e_mail']")
    public WebElement email;

    @FindBy (xpath = "//input[@id='booking_guest_attributes_last_name']")
    public WebElement lastName;

    @FindBy (xpath = "//input[@id='booking_guest_attributes_first_name']")
    public WebElement firstName;

    @FindBy (xpath = "//input[@id='booking_guest_attributes_phone_number']")
    public WebElement phoneNumber;

    @FindBy (xpath = "//input[@id='booking_payment_service_credit_card_collect']")
    public WebElement payOption;

    @FindBy (xpath = "//input[@id='booking_agreed']")
    public WebElement agreeBooking;

    @FindBy (xpath = "//input[@value='Create Booking']")
    public WebElement createBooking;

    @FindBy (xpath = "//input[@id='cardNumber']")
    public WebElement cardNumber;

    @FindBy (xpath = "//input[@id='ccName']")
    public WebElement ccName;
    @FindBy (xpath = "//input[@id='credit_card_collect_purchase_address']")
    public WebElement purchaseAddress;

    @FindBy (xpath = "//input[@id='credit_card_collect_purchase_zip']")
    public WebElement purchaseZip;

    @FindBy (xpath = "//input[@id='credit_card_collect_purchase_city']")
    public WebElement purchaseCity;

    @FindBy (xpath = "//input[@id='credit_card_collect_purchase_state']")
    public WebElement purchaseState;

    @FindBy (xpath = "//button[@type='submit']")
    public WebElement submitBooking;

    @FindBy (xpath = "//div[@id='common_alert']//following::h1")
    public WebElement bookingAlert;

    public void navigateToApplicationUrl() throws IOException {
        String applicationURL = getPropValue(configProp, "ApplicationBaseURL");
        driver.get(applicationURL);
        logPass("Application URL is launched");
    }
    public void bookingDateSelection(String days) throws IOException {
        // create instance of the SimpleDateFormat that matches the current date and add +7 Days
        LocalDate dateRedeemed = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-YYYY");
        newDate = dateRedeemed.plusDays(Long.parseLong(days)).format(formatter);
        System.out.println(newDate+" is the Booking Date");
        dateStart.sendKeys(newDate);
        noOfNights.clear();
        noOfNights.sendKeys("4");
        takeScreenshot("Booking Details");
        bookNow.click();
        logPass("Booking Date is: " + newDate);

    }

    public void selectRoomType() throws IOException {
        try {
            driver.switchTo().frame(0);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement myElement = wait.until(ExpectedConditions.visibilityOf(deluxeRoom));
            myElement.click();
            driver.switchTo().defaultContent();
            logPass("Deluxe Appartment is available for the Booking Date: " + newDate);
        } catch (Exception e) {
            System.out.println("Deluxe Appartment is not Available for the selected Booking Date");
            logFail("Deluxe Appartment is not Available for the selected Booking Date: " + newDate + e.getMessage());
            e.printStackTrace();
        }
    }

    public void selectTwoAddOns() throws IOException {
        int size = driver.findElements(By.tagName("iframe")).size();
        for(int i=1; i<=size; i++){
            driver.switchTo().frame("clock_pms_iframe_1");
        }
        WebDriverWait wait = new WebDriverWait (driver, Duration.ofSeconds(10));
        List<WebElement> webElementList = addOns;
        for(int i=3; i <= webElementList.size();i++) {
            WebElement myElement = wait.until(ExpectedConditions.visibilityOf(addOns1));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", myElement);
                driver.findElement(By.xpath("//div[@class='panel-body']//following::div[@class='col-sm-2 h4']["+i+"]")).click();
                driver.findElement(By.xpath("//div[@class='panel-body']//following::div[@class='col-sm-2 h4']["+i+"]/input")).sendKeys(Keys.UP);
                takeScreenshot("Addon selection");
        }
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();",  addOnsTitle);
        addSelectedServices.click();
        logPass("Add-ons are added to the checkout");
    }

    public void validateBookingDetails() {
        String bookingNights = "4";
        Assert.assertEquals(bookingNights, driver.findElement(By.xpath("//b[contains(text(),'Stay')]//following::div")).getText());
        logPass("Booking Nights is: 4");
    }

    public void addTravellerDetails() throws IOException {
        email.sendKeys("tester@test.com");
        lastName.sendKeys("Syrus");
        firstName.sendKeys("Miley");
        phoneNumber.sendKeys("01234567890");
        payOption.click();
        agreeBooking.click();
        takeScreenshot("Traveller Details");
        createBooking.click();
        logPass("Added Traveller Details");
    }

    public void completePayment() throws IOException {
        cardNumber.sendKeys("4242 4242 4242 4242");
        Select cardType = new Select(driver.findElement(By.id("credit_card_collect_purchase_brand")));
        cardType.selectByVisibleText("VISA");
        ccName.sendKeys("Miley Syrus");
        Select expiryMonth = new Select(driver.findElement(By.id("cardExpirationMonth")));
        expiryMonth.selectByVisibleText("06");
        Select expiryYear = new Select(driver.findElement(By.id("cardExpirationYear")));
        expiryYear.selectByVisibleText("2026");
        purchaseAddress.sendKeys("32");
        purchaseZip.sendKeys("M33 7XE");
        purchaseCity.sendKeys("Manchester");
        purchaseState.sendKeys("Cheshire");
        Select countryName = new Select(driver.findElement(By.id("credit_card_collect_purchase_country")));
        countryName.selectByVisibleText("United Kingdom");
        takeScreenshot("Payment Details");
        isDisplayedThenClick(submitBooking, "Booking Buttion");
        logPass("Completed payment for the selected Booking Date: " + newDate);
    }

    public void validateBookingMessage() {
    String bookingMessage = "Thank you for your booking!";
    Assert.assertEquals(bookingMessage, bookingAlert.getText());
    logPass("Booking is Confirmed");
    }
}