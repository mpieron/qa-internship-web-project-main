package com.griddynamics.qa.vikta.uitesting.sample.pageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object of Vikta's Login page
 */
public class LoginPage {

  @FindBy(id = "tfLoginname")
  private WebElement txtLoginname;

  @FindBy(id = "tfPassword")
  private WebElement txtPassword;

  @FindBy(id = "btnSubmitLogin")
  private WebElement btnSubmitLogin;

  @FindBy(id = "btnSubmitGoToHome")
  private WebElement btnGoToHome;

  @FindBy(xpath = "//div[@class='login']//p[contains(@style, 'color: #FF1C19')]")
  private WebElement lblError;

  //TODO: Add [Sign-up] button.

  public HomePage login(String username, String password) {
    tryLogin(username, password);
    return new HomePage();
  }

  public void tryLogin(String username, String password) {
    txtLoginname.clear();
    txtLoginname.sendKeys(username);

    txtPassword.clear();
    txtPassword.sendKeys(password);

    btnSubmitLogin.click();
  }

  public HomePage gotoHome() {
    btnGoToHome.click();

    return new HomePage();
  }

  public String getErrorMessage() {
    return lblError.getText();
  }

  public WebElement getErrorWebElement() {
    return lblError;
  }
}
