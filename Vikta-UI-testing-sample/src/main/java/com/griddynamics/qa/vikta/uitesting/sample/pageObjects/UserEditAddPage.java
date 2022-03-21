package com.griddynamics.qa.vikta.uitesting.sample.pageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class UserEditAddPage extends EditAddPage {

  private final String noPasswordMessageOnePart = "*Please provide your pass word";

  private final String noPasswordMessageSecondPart = "size must be between 1 and 288";

  @FindBy(id = "tbLoginName")
  private WebElement tbLoginName;

  @FindBy(id = "tbPassword")
  private WebElement tbPassword;

  @FindBy(id = "lPassword")
  private WebElement lPassword;

  @FindBy(id = "tbEmail")
  private WebElement tbEmail;

  @FindBy(id = "tbSurname")
  private WebElement tbSurname;

  @FindBy(id = "lSurname")
  private WebElement lSurname;

  @FindBy(id = "tbFirstName")
  private WebElement tbFirstName;

  @FindBy(id = "tbMiddleName")
  private WebElement tbMiddleName;

  @FindBy(id = "lMiddleName")
  private WebElement lMiddleName;

  @FindBy(id = "imgAvatar")
  private WebElement imgAvatar;

  public void typeLoginName(String value) {
    typeIn(tbLoginName, value);
  }

  public void typePassword(String value) {
    typeIn(tbPassword, value);
  }

  public void typeEmail(String value) {
    typeIn(tbEmail, value);
  }

  public void typeSurname(String value) {
    typeIn(tbSurname, value);
  }

  public void typeFirstName(String value) {
    typeIn(tbFirstName, value);
  }

  public void typeMiddleName(String value) {
    typeIn(tbMiddleName, value);
  }

  public String getNoPasswordDisplayedCommunicat() {
    return lPassword.getText();
  }

  public String getNoPasswordMessageFirstPart() {
    return noPasswordMessageOnePart;
  }

  public String getNoPasswordMessageSecondPart() {
    return noPasswordMessageSecondPart;
  }

  public boolean avatarIsDisplayed() {
    return imgAvatar.isDisplayed();
  }
}
