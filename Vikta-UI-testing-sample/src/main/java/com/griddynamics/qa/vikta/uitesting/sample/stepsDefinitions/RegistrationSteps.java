package com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions;

import static org.assertj.core.api.Assertions.assertThat;

import com.griddynamics.qa.vikta.uitesting.sample.pageObjects.RegistrationPage;
import com.griddynamics.qa.vikta.uitesting.sample.utils.Utilities;
import io.qameta.allure.Step;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Registration functionality related steps.
 */
public class RegistrationSteps extends BaseSteps {

  @Autowired
  private RegistrationPage registrationPage;

  private static String SUCCESSFUL_REGISTRATION_MESSAGE_PREFIX =
      "User has been registered successfully: ";

  private static String FAILED_REGISTRATION_MESSAGE_PREFIX =
      "There is already a user registered with the loginname provided";

  @Step
  public void openRegistrationPage() {
    driver.get(properties.getRegistrationPageUrl());
  }

  @Step
  public String typeRandomValueInto(FieldName fieldName) {
    String valueToReturn;
    switch (fieldName) {
      case LOGINNAME:
        valueToReturn = Utilities.generateLoginName();
        registrationPage.typeInLoginname(valueToReturn);
        break;
      case SURNAME:
        valueToReturn = Utilities.generateSurname();
        registrationPage.typeInSurname(valueToReturn);
        break;
      case FIRSTNAME:
        valueToReturn = Utilities.generateName();
        registrationPage.typeInFirstname(valueToReturn);
        break;
      case PATRONIM:
        valueToReturn = Utilities.generateName();
        registrationPage.typeInPatronim(valueToReturn);
        break;
      case PASSWORD:
        valueToReturn = Utilities.generatePassword();
        registrationPage.typeInPassword(valueToReturn);
        break;
      case EMAIL:
        valueToReturn = Utilities.generateEmail();
        registrationPage.typeInEmail(valueToReturn);
        break;
      default:
        throw new IllegalArgumentException(
            "Unsupported Registration page field name: " + fieldName
        );
    }

    return valueToReturn;
  }

  @Step
  public void clickRegisterButton() {
    registrationPage.clickRegisterButton();
  }

  @Step
  public String typeExistingUserNameInto() {
    String loginName = testData.getUserName();
    registrationPage.typeInLoginname(loginName);
    return loginName;
  }

  @Step
  public void verifyCurrentPageIsRegistration() {
    assertCurrentPageUrl(
        properties.getRegistrationPageUrl(),
        "Registration page was expected to be the current one."
    );
  }

  @Step
  public void verifySuccessfulRegistrationMessageIsDisplayed() {
    getWait().until(ExpectedConditions.visibilityOf(registrationPage.getMessageWebElement()));
    // Have a look at https://assertj.github.io/doc/
    assertThat(registrationPage.getMessageText().trim())
        .as("Successful registration message was nor shown or had unexpected content.")
        .startsWith(SUCCESSFUL_REGISTRATION_MESSAGE_PREFIX);
  }

  @Step
  public void verifyFailedRegistrationMessageIsDisplayed() {
    getWait()
        .until(ExpectedConditions.visibilityOf(registrationPage.getFailedRegistrationMessageWebElement()));
    assertThat(registrationPage.getRegisteredUserExistMessageText().trim())
        .as("Failed registration message was nor shown or had unexpected content.")
        .startsWith(FAILED_REGISTRATION_MESSAGE_PREFIX);
  }

  @Step
  public void verifySuccessfulRegistrationMessageContainsNewUsername(String loginnameUsed) {
    // Have a look at https://assertj.github.io/doc/
    assertThat(registrationPage.getMessageText().trim())
        .as("Successful registration message was expected to contain the new username used.")
        .contains(loginnameUsed);
  }

  public enum FieldName {
    LOGINNAME,
    SURNAME,
    FIRSTNAME,
    PATRONIM,
    PASSWORD,
    EMAIL,
  }
}
