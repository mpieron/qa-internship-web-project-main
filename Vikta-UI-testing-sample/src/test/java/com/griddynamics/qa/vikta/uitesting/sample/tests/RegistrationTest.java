package com.griddynamics.qa.vikta.uitesting.sample.tests;

import com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions.RegistrationSteps;
import lombok.val;
import org.testng.annotations.Test;

/**
 * Feature: User registration
 *   As a guest user
 *   I should be able to register new user account(sign-up) and use it to login into the application
 */
public class RegistrationTest extends BaseTest {

  /**
   * Scenario: Regular user is able to login
   */
  @Test(groups = { "smoke", "signup" })
  public void testRegularUserIsAbleToLogin() {
    // Given user opens Registration page
    registrationSteps.openRegistrationPage();

    // When user types in some random values.
    val loginName = registrationSteps.typeRandomValueInto(RegistrationSteps.FieldName.LOGINNAME);
    registrationSteps.typeRandomValueInto(RegistrationSteps.FieldName.SURNAME);
    registrationSteps.typeRandomValueInto(RegistrationSteps.FieldName.FIRSTNAME);
    registrationSteps.typeRandomValueInto(RegistrationSteps.FieldName.PATRONIM);
    // TODO: uncomment, debug, implement - make it working.
    //registrationSteps.typeInto(RegistrationSteps.FieldName.EMAIL);
    //registrationSteps.typeInto(RegistrationSteps.FieldName.PASSWORD);
    // TODO: And user hits the Register User button

    // Then Registration page the current one
    registrationSteps.verifyCurrentPageIsRegistration();
    // TODO: Then Successful registration message is displayed
    // Then Successful registration message contains the login name used
    registrationSteps.verifySuccessfulRegistrationMessageContainsNewUsername(loginName);
    // TODO: Develop the rest of the scenario. E.g. login as new user etc.
  }

  /**
   * Scenario: Guest user is NOT able to register regular user account using some existing user account's name
   */
  @Test(enabled = false)
  public void testImpossibleToReUseEmailForRegistration() {
    //# TODO: Develop the rest of the scenario.
  }
}
