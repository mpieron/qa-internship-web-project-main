package com.griddynamics.qa.vikta.uitesting.sample.pageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object of Vikta's Registration page
 */
public class RegistrationPage extends BasePage {

  @FindBy(id = "tbLoginName")
  private WebElement tbLoginName;

  @FindBy(id = "tbSurname")
  private WebElement tbSurname;

  @FindBy(id = "tbFirstName")
  private WebElement tbFirstName;

  @FindBy(id = "tbMiddleName")
  private WebElement tbMiddleName;

  //TODO: Add missing elements.

  @FindBy(id = "tbPassword")
  private WebElement tbPassword;

  @FindBy(id = "btnSubmitGoToHome")
  private WebElement btnGoToHome;

  @FindBy(id = "tSuccessMessage")
  private WebElement tSuccessMessage;

  //TODO: Add [Register User] button support.

  public void typeInLoginname(String value) {
    typeIn(value, tbLoginName);
  }

  public void typeInSurname(String value) {
    typeIn(value, tbSurname);
  }

  public void typeInFirstname(String value) {
    typeIn(value, tbFirstName);
  }

  public void typeInPatronim(String value) {
    typeIn(value, tbMiddleName);
  }

  public void typeInPassword(String value) {
    typeIn(value, tbPassword);
  }

  private void typeIn(String value, WebElement targetElement) {
    targetElement.clear();
    targetElement.sendKeys(value);
  }

  public String getMessageText() {
    return tSuccessMessage.getText();
  }

  public WebElement getMessageWebElement() {
    return tSuccessMessage;
  }
}
