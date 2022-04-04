package com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions;

import static org.assertj.core.api.Assertions.assertThat;

import com.griddynamics.qa.vikta.uitesting.sample.config.TestDataConfiguration;
import com.griddynamics.qa.vikta.uitesting.sample.config.TestSetupConfiguration;
import com.griddynamics.qa.vikta.uitesting.sample.pageObjects.LoginPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Login functionality related steps.
 */
public class LoginSteps extends BaseSteps {

  public LoginSteps(
      TestSetupConfiguration properties,
      TestDataConfiguration testData,
      WebDriver driver) {
    super(properties, testData, driver);
  }

  @Step
  public void openLoginPage() {
    driver.get(properties.getLoginPageUrl());
  }

  @Step
  public void login(String username, String password) {
    page().login(username, password);
  }

  @Step
  public void loginAsRegularUser() {
    page().login(testData.getUserName(), testData.getUserPassword());
  }

  @Step
  public void loginAsAdmin() {
    page().login(testData.getAdminName(), testData.getAdminPassword());
  }

  @Step
  public void verifyCurrentPageIsHomePageForUser(String userName) {
    verifyCurrentPageIsHomePageForTheUser(userName, UserType.USER);
  }

  @Step
  public void verifyCurrentPageIsHomePageForTheRegularUser() {
    verifyCurrentPageIsHomePageForTheUser(testData.getUserName(), UserType.USER);
  }

  @Step
  public void verifyCurrentPageIsHomePageForTheAdmin() {
    verifyCurrentPageIsHomePageForTheUser(testData.getAdminName(), UserType.ADMIN);
  }

  @Step
  public void verifyErrorMessage(String text) {
    getWait().until(ExpectedConditions.visibilityOf(page().getErrorWebElement()));
    // Have a look at https://assertj.github.io/doc/
    assertThat(page().getErrorMessage().trim())
      .as("Error message was nor shown or had unexpected content.")
      .contains(text);
  }

  private LoginPage page() {
    return getPage(LoginPage.class);
  }
}