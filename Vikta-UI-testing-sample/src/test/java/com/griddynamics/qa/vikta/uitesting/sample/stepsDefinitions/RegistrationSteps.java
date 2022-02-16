package com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions;

import static org.assertj.core.api.Assertions.assertThat;

import com.griddynamics.qa.vikta.uitesting.sample.pageObjects.RegistrationPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.griddynamics.qa.vikta.uitesting.sample.utils.Utilities;

/**
 * Registration functionality related steps.
 */
public class RegistrationSteps extends BaseSteps {

  private static String SUCCESSFUL_REGISTRATION_MESSAGE_PREFIX =
    "User has been registered successfully: ";

  private static String FAILED_REGISTRATION_MESSAGE_PREFIX =
          "There is already a user registered with the loginname provided";

  public RegistrationSteps(WebDriver driver) {
    super(driver);
  }

  public enum FieldName {
    LOGINNAME,
    SURNAME,
    FIRSTNAME,
    PATRONIM,
    PASSWORD,
    EMAIL
  }

  @Step
  public void openRegistrationPage() {
    getDriver().get(getData().registrationPageUrl());
  }

  @Step
  public String typeRandomValueInto(FieldName fieldName) {
    Utilities utilities = new Utilities();
    String valueToReturn;
    switch (fieldName) {
      case LOGINNAME:
        valueToReturn = utilities.generateLoginName();
        page().typeInLoginname(valueToReturn);
        break;
      case SURNAME:
        valueToReturn = utilities.generateSurname();
        page().typeInSurname(valueToReturn);
        break;
      case FIRSTNAME:
        valueToReturn = utilities.generateName();
        page().typeInFirstname(valueToReturn);
        break;
      case PATRONIM:
        valueToReturn = utilities.generateName();
        page().typeInPatronim(valueToReturn);
        break;
      case PASSWORD:
        valueToReturn = utilities.generatePassword();
        page().typeInPassword(valueToReturn);
        break;
      case EMAIL:
        valueToReturn = utilities.generateEmail();
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
  public void clickRegisterButton(){
    page().clickRegisterButton();
  }

  @Step
  public String typeExistingUserNameInto() {
    String loginName = getData().userName();
    page().typeInLoginname(loginName);
    return loginName;
  }

  @Step
  public void verifyCurrentPageIsRegistration() {
    assertCurrentPageUrl(
      getData().registrationPageUrl(),
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
    getWait().until(ExpectedConditions.visibilityOf(page().getFailedRegistrationMessageWebElement()));
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
