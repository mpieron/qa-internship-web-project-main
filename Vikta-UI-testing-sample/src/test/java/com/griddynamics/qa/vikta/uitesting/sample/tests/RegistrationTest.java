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
    val password = registrationSteps.typeRandomValueInto(RegistrationSteps.FieldName.PASSWORD);
    registrationSteps.typeRandomValueInto(RegistrationSteps.FieldName.EMAIL);
    // TODO: uncomment, debug, implement - make it working.
//    I think that typeRandomValueInto is ok
//    registrationSteps.typeInto(RegistrationSteps.FieldName.EMAIL);
//    registrationSteps.typeInto(RegistrationSteps.FieldName.PASSWORD);
    // And user hits the Register User button
    registrationSteps.clickRegisterButton();
    // Then Registration page the current one
    registrationSteps.verifyCurrentPageIsRegistration();
    // Then Successful registration message is displayed
    registrationSteps.verifySuccessfulRegistrationMessageIsDisplayed();
    // Then Successful registration message contains the login name used
    registrationSteps.verifySuccessfulRegistrationMessageContainsNewUsername(loginName);

    // Develop login as new user
    // when try login as just registered user, following message will appear:
    // "Login name or Password invalid, please verify", probably account must be verified (by email) or something
    // same when I try manually - is that ok, should be that communicate?
    loginSteps.openLoginPage();
    loginSteps.login(loginName, password);
    loginSteps.verifyCurrentPageIsHomePageForUser(loginName);
}

  /**
   * Scenario: Guest user is NOT able to register regular user account using some existing user account's name
   */
  @Test()
  public void testImpossibleToReUseEmailForRegistration() {
    // Given user opens Registration page
    registrationSteps.openRegistrationPage();

    // When login name is same as existing user
    val loginName = registrationSteps.typeExistingUserNameInto();
    // When user types in some random values.
    registrationSteps.typeRandomValueInto(RegistrationSteps.FieldName.SURNAME);
    registrationSteps.typeRandomValueInto(RegistrationSteps.FieldName.FIRSTNAME);
    registrationSteps.typeRandomValueInto(RegistrationSteps.FieldName.PATRONIM);
    registrationSteps.typeRandomValueInto(RegistrationSteps.FieldName.PASSWORD);
    registrationSteps.typeRandomValueInto(RegistrationSteps.FieldName.EMAIL);

    // And user hits the Register User button
    registrationSteps.clickRegisterButton();

    // Then Registration page the current one
    registrationSteps.verifyCurrentPageIsRegistration();

    // Then Failed registration message is displayed
    registrationSteps.verifyFailedRegistrationMessageIsDisplayed();
  }
}
