Feature: Verify a Booking can be made and validate booking message

Description: To launch the application

 @UITest @testcaseID=TC_01
 Scenario Outline: TC_01: To verify if an Expensive Deluxe Room can be booked
  Given user navigates to the login page
  And user selects future date days "<days>", and number of rooms and start the booking process
  And user selects Under Deluxe Apartment, select the most expensive package
  And user selects any 2 add ons
  And user validates all details – Date, no of nights, room type, rate, add on (extra services charges), total
  And user adds traveler details and payment method to CC
  When user uses a dummy Visa CC and complete payment
  Then user validates Booking complete msg

  Examples:
  |days|
  |01  |
  |30  |
  |60  |
  |100 |
  |200 |