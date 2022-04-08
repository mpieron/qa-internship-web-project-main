package com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions;

import static org.assertj.core.api.Assertions.assertThat;

import com.griddynamics.qa.vikta.uitesting.sample.auxiliary.DriverManager;
import com.griddynamics.qa.vikta.uitesting.sample.config.TestDataConfiguration;
import com.griddynamics.qa.vikta.uitesting.sample.pageObjects.LoginPage;
import com.griddynamics.qa.vikta.uitesting.sample.utils.GenericWebActions;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Login functionality related steps.
 */
public class LoginSteps extends GenericWebActions {

  @Autowired
  LoginPage loginPage;
  @Autowired
  private DriverManager driverManager;
  @Autowired
  private TestDataConfiguration testData;

  protected WebDriver getDriver(){
    return driverManager.get();
  }


  @Step
  public void openLoginPage() {
    getDriver().get(properties.getLoginPageUrl());
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
    getWait().until(ExpectedConditions.visibilityOf(loginPage.getErrorWebElement()));
    // Have a look at https://assertj.github.io/doc/
    assertThat(loginPage.getErrorMessage().trim())
        .as("Error message was nor shown or had unexpected content.")
        .contains(text);
  }
}
