package org.retail.com.StepDefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Given;
import org.retail.com.Pages.BasePage;
import org.retail.com.Pages.HomePage;
import org.retail.com.Utilities.BaseUtilities;

import java.io.IOException;

public class MyStepdefs extends BaseUtilities {
    HomePage homePage = new HomePage(BasePage.driver);
    @Given("user navigates to the login page")
    public void user_navigates_to_the_login_page() throws IOException {
        homePage.navigateToApplicationUrl();
    }

    @And("user selects future date days {string}, and number of rooms and start the booking process")
    public void userSelectAValidDateAndNumberOfRoomsAndStartTheBookingProcess(String days) throws IOException {
        homePage.bookingDateSelection(days);
    }

    @And("user selects Under Deluxe Apartment, select the most expensive package")
    public void userSelectsUnderDeluxeApartmentSelectTheMostExpensivePackage() throws IOException {
        homePage.selectRoomType();
    }

    @And("user selects any {int} add ons")
    public void userSelectsAnyAddOns(int arg0) throws IOException {
        homePage.selectTwoAddOns();
    }

    @And("user validates all details – Date, no of nights, room type, rate, add on \\(extra services charges), total")
    public void userValidatesAllDetailsDateNoOfNightsRoomTypeRateAddOnExtraServicesChargesTotal() throws InterruptedException {
        homePage.validateBookingDetails();
    }

    @And("user adds traveler details and payment method to CC")
    public void userAddsTravelerDetailsAndPaymentMethodToCC() throws IOException {
        homePage.addTravellerDetails();
    }

    @When("user uses a dummy Visa CC and complete payment")
    public void userUsesADummyVisaCCAndCompletePayment() throws IOException {
        homePage.completePayment();
    }

    @Then("user validates Booking complete msg")
    public void userValidatesBookingCompleteMsg() {
        homePage.validateBookingMessage();
    }

}
