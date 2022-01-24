package com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions;

import static org.assertj.core.api.Assertions.assertThat;

import com.griddynamics.qa.vikta.uitesting.sample.pageObjects.LoginPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Login functionality related steps.
 */
public class LoginSteps extends BaseSteps {

  public LoginSteps(WebDriver driver) {
    super(driver);
  }

  @Step
  public void openLoginPage() {
    getDriver().get(getData().loginPageUrl());
  }

  @Step
  public void login(String username, String password) {
    page().login(username, password);
  }

  @Step
  public void loginAsRegularUser() {
    page().login(getData().userName(), getData().userPassword());
  }

  @Step
  public void loginAsAdmin() {
    page().login(getData().adminName(), getData().adminPassword());
  }

  @Step
  public void verifyCurrentPageIsHomePageForTheRegularUser() {
    verifyCurrentPageIsHomePageForTheUser(getData().userName());
  }

  @Step
  public void verifyCurrentPageIsHomePageForTheAdmin() {
    verifyCurrentPageIsHomePageForTheUser(getData().adminName());
  }

  @Step
  public void verifyErrorMessage(String text) {
    getWait().until(ExpectedConditions.visibilityOf(page().getErrorWebElement()));
    // Have a look at https://assertj.github.io/doc/
    assertThat(page().getErrorMessage().trim())
      .as("Error message was nor shown or had unexpected content.")
      .contains(text);
  }

  //TODO: Think about generics etc instead of this.
  private LoginPage page() {
    return getPage(LoginPage.class);
  }
}
