package com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions;

import static org.assertj.core.api.Assertions.assertThat;

import com.griddynamics.qa.vikta.uitesting.sample.auxiliary.DriverManager;
import com.griddynamics.qa.vikta.uitesting.sample.config.TestDataConfiguration;
import com.griddynamics.qa.vikta.uitesting.sample.pageObjects.LoginPage;
import com.griddynamics.qa.vikta.uitesting.sample.utils.GenericWebActions;
import io.qameta.allure.Step;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Login functionality related steps.
 */

public class LoginSteps{

  @Autowired
  LoginPage loginPage;
  @Autowired
  private DriverManager driverManager;
  @Autowired
  private TestDataConfiguration testData;
  @Autowired
  private GenericWebActions genericWebActions;

  protected WebDriver getDriver(){
    return driverManager.get();
  }

  @Step
  public void openLoginPage() {
    getDriver().get(genericWebActions.getProperties().getLoginPageUrl());
  }

  @Step
  public void login(String username, String password) {
    loginPage.login(username, password);
  }

  @Step
  public void loginAsRegularUser() {
    loginPage.login(testData.getUserName(), testData.getUserPassword());
  }

  @Step
  public void loginAsAdmin() {
    loginPage.login(testData.getAdminName(), testData.getAdminPassword());
  }

  @Step
  public void verifyCurrentPageIsHomePageForUser(String userName) {
    genericWebActions.verifyCurrentPageIsHomePageForTheUser(userName, GenericWebActions.UserType.USER);
  }

  @Step
  public void verifyCurrentPageIsHomePageForTheRegularUser() {
    genericWebActions.verifyCurrentPageIsHomePageForTheUser(testData.getUserName(), GenericWebActions.UserType.USER);
  }

  @Step
  public void verifyCurrentPageIsHomePageForTheAdmin() {
    genericWebActions.verifyCurrentPageIsHomePageForTheUser(testData.getAdminName(), GenericWebActions.UserType.ADMIN);
  }

  @Step
  public void verifyErrorMessage(String text) {
    genericWebActions.getWait().until(ExpectedConditions.visibilityOf(loginPage.getErrorWebElement()));
    // Have a look at https://assertj.github.io/doc/
    assertThat(loginPage.getErrorMessage().trim())
        .as("Error message was nor shown or had unexpected content.")
        .contains(text);
  }
}
