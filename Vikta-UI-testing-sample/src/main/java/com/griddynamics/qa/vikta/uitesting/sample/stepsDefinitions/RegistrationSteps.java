package com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions;

import static org.assertj.core.api.Assertions.assertThat;

import com.griddynamics.qa.vikta.uitesting.sample.config.TestDataConfiguration;
import com.griddynamics.qa.vikta.uitesting.sample.config.TestSetupConfiguration;
import com.griddynamics.qa.vikta.uitesting.sample.pageObjects.RegistrationPage;
import com.griddynamics.qa.vikta.uitesting.sample.utils.Utilities;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Registration functionality related steps.
 */
public class RegistrationSteps extends BaseSteps {

  private static String SUCCESSFUL_REGISTRATION_MESSAGE_PREFIX =
    "User has been registered successfully: ";

  private static String FAILED_REGISTRATION_MESSAGE_PREFIX =
    "There is already a user registered with the loginname provided";


  public RegistrationSteps(TestSetupConfiguration properties, TestDataConfiguration testData, WebDriver driver) {
    super(properties, testData, driver);
  }

  public enum FieldName {
    LOGINNAME,
    SURNAME,
    FIRSTNAME,
    PATRONIM,
    PASSWORD,
    EMAIL,
  }

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
        page().typeInLoginname(valueToReturn);
        break;
      case SURNAME:
        valueToReturn = Utilities.generateSurname();
        page().typeInSurname(valueToReturn);
        break;
      case FIRSTNAME:
        valueToReturn = Utilities.generateName();
        page().typeInFirstname(valueToReturn);
        break;
      case PATRONIM:
        valueToReturn = Utilities.generateName();
        page().typeInPatronim(valueToReturn);
        break;
      case PASSWORD:
        valueToReturn = Utilities.generatePassword();
        page().typeInPassword(valueToReturn);
        break;
      case EMAIL:
        valueToReturn = Utilities.generateEmail();
        page().typeInEmail(valueToReturn);
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
    page().clickRegisterButton();
  }

  @Step
  public String typeExistingUserNameInto() {
    String loginName = testData.getUserName();
    page().typeInLoginname(loginName);
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
    getWait().until(ExpectedConditions.visibilityOf(page().getMessageWebElement()));
    // Have a look at https://assertj.github.io/doc/
    assertThat(page().getMessageText().trim())
      .as("Successful registration message was nor shown or had unexpected content.")
      .startsWith(SUCCESSFUL_REGISTRATION_MESSAGE_PREFIX);
  }

  @Step
  public void verifyFailedRegistrationMessageIsDisplayed() {
    getWait()
      .until(ExpectedConditions.visibilityOf(page().getFailedRegistrationMessageWebElement()));
    assertThat(page().getRegisteredUserExistMessageText().trim())
      .as("Failed registration message was nor shown or had unexpected content.")
      .startsWith(FAILED_REGISTRATION_MESSAGE_PREFIX);
  }

  @Step
  public void verifySuccessfulRegistrationMessageContainsNewUsername(String loginnameUsed) {
    // Have a look at https://assertj.github.io/doc/
    assertThat(page().getMessageText().trim())
      .as("Successful registration message was expected to contain the new username used.")
      .contains(loginnameUsed);
  }

  private RegistrationPage page() {
    return getPage(RegistrationPage.class);
  }
}