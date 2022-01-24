package com.griddynamics.qa.vikta.uitesting.sample.tests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Feature: Log into the application
 *   As a user I should be able to login into the application
 */
public class LoginTest extends BaseTest {

  /**
   * Provides data cases for "Invalid credentials login" scenario.
   * Structure: <name>, <password>
   */
  @DataProvider(name = "invalidLoginDataCases")
  private Object[][] invalidLoginDataCases() {
    return new Object[][] {
      //<name>, <password>
      { "qq", "qwe" },
      { "qw", "123" },
      { "za", "az" },
      { "qwer", "tyuioiop" },
    };
  }

  /**
   * Scenario: Regular user is able to login
   */
  @Test(groups = { "smoke", "login" })
  public void testRegularUserIsAbleToLogin() {
    // Given user opens Login page
    loginSteps.openLoginPage();

    // When user logins as regular user
    loginSteps.loginAsRegularUser();

    // Then Home is displayed for the regular user
    loginSteps.verifyCurrentPageIsHomePageForTheRegularUser();
  }

  /**
   * Scenario: Admin user is able to login
   */
  @Test(groups = { "smoke", "login" })
  public void testAdminUserIsAbleToLogin() {
    // Given user opens Login page
    loginSteps.openLoginPage();

    // When user logins as ADMIN user
    loginSteps.loginAsAdmin();

    // Then Home is displayed for the ADMIN user
    loginSteps.verifyCurrentPageIsHomePageForTheAdmin();
  }

  /**
   * Scenario Outline: Cannon login using invalid username and/or password
   */
  @Test(groups = { "smoke", "login", "invalid" }, dataProvider = "invalidLoginDataCases")
  public void testInvalidCredentialsNotAbleToLogin(String name, String password) {
    // Given user opens Login page
    loginSteps.openLoginPage();

    // When user types '<name>' as loginname and '<password>' as password and hits Submit
    loginSteps.login(name, password);

    // Then login error message ... is displayed.
    loginSteps.verifyErrorMessage("Login name or Password invalid, please verify");
  }
}
